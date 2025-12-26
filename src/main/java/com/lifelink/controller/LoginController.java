package com.lifelink.controller;

import java.util.List;
import java.util.ArrayList;

import com.lifelink.entity.*;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lifelink.service.BloodDonationService;
import com.lifelink.service.BloodRequestService;
import com.lifelink.service.OrganDonationService;
import com.lifelink.service.OrganRequestService;
import com.lifelink.service.UserService;

@Controller
public class LoginController {

    @Autowired private UserService userService;
    @Autowired private BloodDonationService bloodDonationService;
    @Autowired private OrganDonationService organDonationService;
    @Autowired private BloodRequestService bloodRequestService;
    @Autowired private OrganRequestService organRequestService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal Object principal) {
        String username = "Guest";
        String profilePicUrl = "/uploads/blank_profile.jpg";
        Users user = null;
        Role role = Role.USER;

        if (principal instanceof UserDetails userDetails) {
            user = userService.findByEmail(userDetails.getUsername());
            if (user != null) {
                username = user.getFullName();
                profilePicUrl = (user.getProfileImagePath() != null && !user.getProfileImagePath().isEmpty()
                        && !user.getProfileImagePath().equals("/uploads/blank_profile.jpg"))
                        ? user.getProfileImagePath()
                        : "/uploads/blank_profile.jpg";
                role = user.getRole() != null ? user.getRole() : Role.USER;
            }
        } else if (principal instanceof OAuth2User oauthUser) {
            String email = oauthUser.getAttribute("email");
            String login = oauthUser.getAttribute("login");
            username = oauthUser.getAttribute("name");

            if (username == null) username = login;
            if (username == null) username = "OAuthUser";

            if (email == null || email.isEmpty()) {
                email = (login != null ? login : java.util.UUID.randomUUID().toString().substring(0, 8))
                        .toLowerCase() + "@oauthuser.com";
            }

            String picture = oauthUser.getAttribute("picture");
            String finalProfilePicUrl = "/uploads/blank_profile.jpg";

            if (picture != null && !picture.isEmpty()) {
                finalProfilePicUrl = picture.startsWith("http") ? picture : "https://lh3.googleusercontent.com" + picture;
            }

            user = userService.findByEmail(email);
            if (user == null) {
                user = new Users();
                user.setEmail(email);
                user.setFullName(username);
                user.setProfileImagePath(finalProfilePicUrl);
                user.setRole(Role.USER);
                userService.registerOAuthUser(user);
            } else {
                user.setProfileImagePath(finalProfilePicUrl);
                userService.save(user);
            }

            profilePicUrl = finalProfilePicUrl;
            username = user.getFullName();
            role = user.getRole() != null ? user.getRole() : Role.USER;
        }

        List<Users> hospitals = userService.findByRole(Role.HOSPITAL);
        int totalDonors = userService.countAllUsers();  // All registered users
        long totalBloodUnits = bloodDonationService.countByStatus("APPROVED");  // Only approved blood donations
        long totalOrgansDonated = organDonationService.countByStatus("APPROVED");  // Only approved organ donations
        long totalBloodRequests = bloodRequestService.countByStatus("PENDING");  // Pending blood requests
        long totalOrganRequests = organRequestService.countByStatus("PENDING");  // Pending organ requests

        List<BloodDonation> freeBloodDonors = bloodDonationService.findByStatusAndDonationType("APPROVED", DonationType.FREE);
        List<OrganDonation> freeOrganDonors = organDonationService.findByStatusAndDonationType("APPROVED", DonationType.FREE);
        model.addAttribute("hospitals", hospitals);
        model.addAttribute("totalHospitals", hospitals.size());
        model.addAttribute("totalBloodDonors", bloodDonationService.countAllDonors());
        model.addAttribute("totalOrganDonors", organDonationService.countAllDonors());
        model.addAttribute("totalDonors", totalDonors);
        model.addAttribute("totalBloodUnits", totalBloodUnits);  // ✅ REAL DATA
        model.addAttribute("totalOrgansDonated", totalOrgansDonated);  // ✅ REAL DATA
        model.addAttribute("totalBloodRequests", totalBloodRequests);
        model.addAttribute("totalOrganRequests", totalOrganRequests);
        model.addAttribute("freeBloodDonors", freeBloodDonors);  // ✅ For cards
        model.addAttribute("freeOrganDonors", freeOrganDonors);  // ✅ For cards

        model.addAttribute("username", username);
        model.addAttribute("profilePicUrl", profilePicUrl);
        model.addAttribute("user", user);
        model.addAttribute("role", role);
        model.addAttribute("isAdmin", role == Role.ADMIN);

        return switch (role) {
            case ADMIN -> "adminDashboard";
            case HOSPITAL -> "redirect:/hospital/dashboard";
            default -> "dashboard";
        };
    }

    @GetMapping("/postLogin")
    public String postLoginRedirect(Authentication authentication) {
        return "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) model.addAttribute("error", "Invalid email or password.");
        return "login";
    }

    @GetMapping("/adminDashboard")
    public String getAdminDashboard(Model model, @AuthenticationPrincipal Object principal) {
        Users admin = null;
        if (principal instanceof UserDetails userDetails) {
            admin = userService.findByEmail(userDetails.getUsername());
        } else if (principal instanceof OAuth2User oauthUser) {
            String email = oauthUser.getAttribute("email");
            admin = userService.findByEmail(email);
        }

        model.addAttribute("username", admin != null ? admin.getFullName() : "Admin");
        model.addAttribute("profilePicUrl", admin != null ? admin.getProfileImagePath() : "/uploads/blank_profile.jpg");

        model.addAttribute("totalUsers", userService.countByRole(Role.USER));
        model.addAttribute("activeDonors", userService.countByRole(Role.USER));
        model.addAttribute("bloodUnits", 2000);
        model.addAttribute("organRequests", 50);
        model.addAttribute("recentUsers", userService.findAllByRole(Role.USER).subList(0, 5));

        model.addAttribute("donationMonths", List.of("Jan", "Feb", "Mar", "Apr", "May"));
        model.addAttribute("bloodUnitsPerMonth", List.of(10, 20, 15, 25, 30));
        model.addAttribute("registrationMonths", List.of("Jan", "Feb", "Mar", "Apr", "May"));
        model.addAttribute("newUsersPerMonth", List.of(5, 15, 10, 20, 25));

        return "adminDashboard";
    }

    @GetMapping("/")
    public String mainPage() {
        return "MainPage";
    }
}
