package com.lifelink.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.lifelink.entity.Role;
import com.lifelink.entity.Users;
import com.lifelink.service.BloodDonationService;
import com.lifelink.service.OrganDonationService;
import com.lifelink.service.UserService;

@Controller
public class AdminController {

    @Autowired private UserService userService;
    @Autowired private BloodDonationService bloodDonationService;
    @Autowired private OrganDonationService organDonationService;
    @GetMapping ("/admin/dashboard")
    public String dashboard(@AuthenticationPrincipal Object principal,  Model model) {
        if (!isAdmin(principal)) return "redirect:/login?error=admin-only";

        List<Users> hospitals = userService.findByRole(Role.HOSPITAL);

        System.out.println("🔍 DEBUG: Found " + hospitals.size() + " hospitals");
        hospitals.forEach(h -> System.out.println("Hospital: " + h.getFullName()));

        model.addAttribute("hospitals", hospitals);
        model.addAttribute("totalHospitals", hospitals.size());
        model.addAttribute("totalBloodDonors", bloodDonationService.countAllDonors());
        model.addAttribute("totalOrganDonors", organDonationService.countAllDonors());

        // Add profile info for navbar
        addProfileInfo(model, principal);

        return "adminDashboard";
    }
    @GetMapping("/admin/stats")
    public String adminStats(Model model, @AuthenticationPrincipal Object principal) {
        if (!isAdmin(principal)) return "redirect:/login?error=admin-only";

        List<Users> hospitals = userService.findByRole(Role.HOSPITAL);

        System.out.println("🔍 DEBUG: Found " + hospitals.size() + " hospitals");
        hospitals.forEach(h -> System.out.println("Hospital: " + h.getFullName()));

        model.addAttribute("hospitals", hospitals);
        model.addAttribute("totalHospitals", hospitals.size());
        model.addAttribute("totalBloodDonors", bloodDonationService.countAllDonors());
        model.addAttribute("totalOrganDonors", organDonationService.countAllDonors());

        // Add profile info for navbar
        addProfileInfo(model, principal);

        return "adminDashboard";
    }

    @PostMapping("/admin/hospital-details")
    public String hospitalDetails(@RequestParam("hospitalId") Long hospitalId, Model model, @AuthenticationPrincipal Object principal) {
        if (!isAdmin(principal)) return "redirect:/login?error=admin-only";

        Users hospital = userService.findById(hospitalId);
        if (hospital != null) {
            model.addAttribute("selectedHospital", hospital);

            // ✅ COUNTS - Make sure these methods exist in your services
            model.addAttribute("bloodApproved", bloodDonationService.countByHospitalAndStatus(hospital, "APPROVED"));
            model.addAttribute("bloodPending", bloodDonationService.countByHospitalAndStatus(hospital, "PENDING"));
            model.addAttribute("bloodRejected", bloodDonationService.countByHospitalAndStatus(hospital, "REJECTED"));

            model.addAttribute("organApproved", organDonationService.countByHospitalAndStatus(hospital, "APPROVED"));
            model.addAttribute("organPending", organDonationService.countByHospitalAndStatus(hospital, "PENDING"));
            model.addAttribute("organRejected", organDonationService.countByHospitalAndStatus(hospital, "REJECTED"));

            // ✅ ONLY Approved donors shown in template - others optional
            model.addAttribute("bloodDonorsApproved", bloodDonationService.findByHospitalAndStatus(hospital, "APPROVED"));
        }

        // Always reload main lists
        List<Users> hospitals = userService.findByRole(Role.HOSPITAL);
        model.addAttribute("hospitals", hospitals);
        model.addAttribute("totalHospitals", hospitals.size());
        model.addAttribute("totalBloodDonors", bloodDonationService.countAllDonors());
        model.addAttribute("totalOrganDonors", organDonationService.countAllDonors());

        // Add profile info for navbar
        addProfileInfo(model, principal);

        return "adminDashboard";
    }
    private void addProfileInfo(Model model, @AuthenticationPrincipal Object principal) {
        String email = getUserEmail(principal);
        if (email != null) {
            Users user = userService.findByEmail(email);
            if (user != null) {
                model.addAttribute("username", user.getFullName());

                // ✅ FIXED: Handle String path correctly
                String profilePath = user.getProfileImagePath();
                if (profilePath != null && !profilePath.isEmpty()) {
                    // If path is absolute or already a full URL, use as-is
                    model.addAttribute("profilePicUrl", profilePath);
                } else {
                    // Default fallback
                    model.addAttribute("profilePicUrl", "/uploads/blank_profile.jpg");
                }
            }
        }
    }


    private boolean isAdmin(Object principal) {
        String email = getUserEmail(principal);
        if (email == null) return false;
        Users user = userService.findByEmail(email);
        return user != null && user.getRole() == Role.ADMIN;
    }

    private String getUserEmail(Object principal) {
        try {
            if (principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
                return userDetails.getUsername();
            } else if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User oauthUser) {
                return oauthUser.getAttribute("email");
            }
        } catch (Exception e) {
            System.err.println("Error getting user email: " + e.getMessage());
        }
        return null;
    }
}
