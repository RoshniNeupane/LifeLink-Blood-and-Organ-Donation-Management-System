package com.lifelink.controller;

import java.util.List;

import com.lifelink.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.lifelink.entity.BloodDonation;
import com.lifelink.entity.Users;
@Controller
@RequestMapping("/hospital")
public class HospitalDashboardController {
    @Autowired private HospitalDashboardService dashboardService;
    @Autowired private BloodDonationService bloodService;
    @Autowired private OrganDonationService organService;
    @Autowired private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal Object principal) {
        Users hospital = getHospital(principal);
        if (hospital == null) return "redirect:/login";

        model.addAttribute("username", hospital.getFullName());
        model.addAttribute("profilePicUrl", hospital.getProfileImagePath());

        // ✅ ALL DATA
        model.addAttribute("totalBloodDonors", dashboardService.getTotalBloodDonors(hospital.getId()));
        model.addAttribute("totalOrganDonors", dashboardService.getTotalOrganDonors(hospital.getId()));
        model.addAttribute("pendingFreeDonors", dashboardService.getPendingFreeDonors(hospital));
        model.addAttribute("pendingPaidDonors", dashboardService.getPendingPaidDonors(hospital));
        model.addAttribute("approvedBloodDonors", dashboardService.getApprovedBloodDonors(hospital));
        model.addAttribute("approvedOrganDonors", dashboardService.getApprovedOrganDonors(hospital));

        return "hospitalDashboard";
    }

    @PostMapping("/donor/approve")
    public String approve(@RequestParam Long donorId, @RequestParam String donorType) {
        if ("BLOOD".equalsIgnoreCase(donorType)) {
            bloodService.findById(donorId).ifPresent(d -> {
                d.setStatus("APPROVED");
                bloodService.save(d);
            });
        } else if ("ORGAN".equalsIgnoreCase(donorType)) {
            organService.findById(donorId).ifPresent(d -> {
                d.setStatus("APPROVED");
                organService.save(d);
            });
        }
        return "redirect:/hospital/dashboard";
    }

    @PostMapping("/donor/remove")
    public String remove(@RequestParam Long donorId, @RequestParam String donorType) {
        if ("BLOOD".equalsIgnoreCase(donorType)) {
            bloodService.deleteById(donorId);
        } else if ("ORGAN".equalsIgnoreCase(donorType)) {
            organService.deleteById(donorId);
        }
        return "redirect:/hospital/dashboard";
    }

    private Users getHospital(Object principal) {
        if (principal instanceof UserDetails ud) {
            return userService.findByEmail(ud.getUsername());
        } else if (principal instanceof OAuth2User ou) {
            return userService.findByEmail(ou.getAttribute("email"));
        }
        return null;
    }

}

