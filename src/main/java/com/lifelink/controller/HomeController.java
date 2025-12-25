//package com.lifelink.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import com.lifelink.service.UserService;
//import com.lifelink.service.DonationService;
//import com.lifelink.entity.Users;
//
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.UserDetails;
//
//@Controller
//public class HomeController {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private DonationService donationService; // service to get stats
//
//    @GetMapping("/")
//    public String homePage(Model model, @AuthenticationPrincipal Object principal) {
//
//        Users user = null;
//        String username = "";
//        String profilePicUrl = "/uploads/blank_profile.jpg";
//
//        if (principal instanceof UserDetails userDetails) {
//            user = userService.findByEmail(userDetails.getUsername());
//            if (user != null) {
//                username = user.getFullName();
//                profilePicUrl = user.getProfileImagePath() != null ? user.getProfileImagePath() : profilePicUrl;
//            }
//        }
//
//        // Add user info to model
//        model.addAttribute("user", user);
//        model.addAttribute("username", username);
//        model.addAttribute("profilePicUrl", profilePicUrl);
//
//        // Fetch dynamic stats from services
//        model.addAttribute("totalDonors", donationService.countActiveDonors());  // returns long
//        model.addAttribute("livesSaved", donationService.countLivesSaved());     // returns long
//        model.addAttribute("bloodUnits", donationService.countBloodUnits());     // returns long
//        model.addAttribute("organDonors", donationService.countOrganDonors());   // returns long
//        model.addAttribute("supportAvailable", "24/7");
//
//        return "home"; // Thymeleaf template
//    }
//}
