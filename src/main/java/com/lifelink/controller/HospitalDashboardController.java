package com.lifelink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lifelink.entity.Users;
import com.lifelink.service.BloodDonationService;
import com.lifelink.service.HospitalDashboardService;
import com.lifelink.service.OrganDonationService;
import com.lifelink.service.UserService;

@Controller
@RequestMapping("/hospital")
public class HospitalDashboardController {

    @Autowired
    private HospitalDashboardService dashboardService;

    @Autowired
    private BloodDonationService bloodDonationService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrganDonationService organDonationService;
    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal User principal) {

        // Fetch hospital user from DB using email from principal
        Users hospital = userService.findByEmail(principal.getUsername());

        model.addAttribute("profilePicUrl", "/uploads/default-avatar.png");
        model.addAttribute("username", hospital.getFullName());

        model.addAttribute("totalDonors", dashboardService.getTotalDonors(hospital));
        model.addAttribute("totalFreeDonorsApproved", dashboardService.getTotalFreeDonorsApproved(hospital));
        model.addAttribute("allDonors", dashboardService.getAllDonorsCombined(hospital));
        model.addAttribute("pendingFreeDonors", dashboardService.getPendingFreeDonors(hospital));

        return "hospitalDashboard";
    }

    /** Approve donor (blood or organ) */
    @PostMapping("/donor/approve")
    public String approveDonor(@RequestParam Long donorId, @RequestParam String donorType) {
        if ("BLOOD".equalsIgnoreCase(donorType)) {
            bloodDonationService.findById(donorId).ifPresent(d -> {
                d.setStatus("APPROVED");
                bloodDonationService.save(d);
            });
        } else if ("ORGAN".equalsIgnoreCase(donorType)) {
            organDonationService.findById(donorId).ifPresent(d -> {
                d.setStatus("APPROVED");
                organDonationService.save(d);
            });
        }
        return "redirect:/hospital/dashbo  ard";
    }

    /** Remove donor (blood or organ) */
    @PostMapping("/donor/remove")
    public String removeDonor(@RequestParam Long donorId, @RequestParam String donorType) {
        if ("BLOOD".equalsIgnoreCase(donorType)) {
            bloodDonationService.deleteById(donorId);
        } else if ("ORGAN".equalsIgnoreCase(donorType)) {
            organDonationService.deleteById(donorId);
        }
        return "redirect:/hospital/dashboard";
    }
}
