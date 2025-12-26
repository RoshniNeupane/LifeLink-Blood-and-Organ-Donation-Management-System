package com.lifelink.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.lifelink.entity.HospitalDetails;
import com.lifelink.entity.Role;
import com.lifelink.entity.Users;
import com.lifelink.service.HospitalDashboardService;
import com.lifelink.service.UserService;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private HospitalDashboardService hospitalDetailsService;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new Users());
        return "register";
    }
    @PostMapping("/register")
    public String register(@ModelAttribute Users user,
                           @RequestParam("image") MultipartFile imageFile,
                           @RequestParam("role") String role,
                           @RequestParam(value="hospitalName", required=false) String hospitalName,
                           @RequestParam(value="hospitalAddress", required=false) String hospitalAddress,
                           @RequestParam(value="hospitalContact", required=false) String hospitalContact,
                           Model model) {

        // Check if email already exists
        if (userService.findByEmail(user.getEmail()) != null) {
            model.addAttribute("error", "User with this email already exists.");
            model.addAttribute("user", user);
            return "register";
        }

        // Upload image to Cloudinary if present
        try {
            if (!imageFile.isEmpty()) {
                Map uploadResult = cloudinary.uploader().upload(
                        imageFile.getBytes(),
                        ObjectUtils.asMap("folder", "lifelink_profiles", "resource_type", "auto")
                );

                String imageUrl = uploadResult.get("secure_url").toString();
                user.setProfileImagePath(imageUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Image upload failed!");
            model.addAttribute("user", user);
            return "register";
        }

        // Set role
        if ("ADMIN".equalsIgnoreCase(role)) {
            user.setRole(Role.ADMIN);
        } else if ("HOSPITAL".equalsIgnoreCase(role)) {
            user.setRole(Role.HOSPITAL);

            // Create hospital details and set both sides of relationship
            HospitalDetails hd = new HospitalDetails();
            hd.setHospitalName(hospitalName);
            hd.setHospitalAddress(hospitalAddress);
            hd.setHospitalContact(hospitalContact);
            hd.setUser(user); // link back to user
            user.setHospitalDetails(hd); // link from user
        } else {
            user.setRole(Role.USER);
        }

        // Password complexity validation
        if (!user.getPassword().matches(".*[A-Z].*") || !user.getPassword().matches(".*[a-z].*") || !user.getPassword().matches(".*\\d.*") || !user.getPassword().matches(".*[!@#$%^&*(),.?\":{}|<>].*") || user.getPassword().length() < 8) {
            model.addAttribute("error", "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.");
            model.addAttribute("user", user);
            return "register";
        }

        // Save user (hospital details will be saved automatically because of cascade)
        userService.registerUser(user);
       
        return "redirect:/login";
    }
}