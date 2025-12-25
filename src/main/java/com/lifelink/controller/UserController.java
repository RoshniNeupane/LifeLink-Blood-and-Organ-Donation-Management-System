//package com.lifelink.controller;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//
//import com.lifelink.entity.Users;
//import com.lifelink.service.UserService;
//
//@Controller
//public class UserController {
//
//    @Autowired
//    private UserService userService;
//
//    @GetMapping("/register")
//    public String showRegistrationForm(Model model) {
//        model.addAttribute("user", new Users());
//        return "register";
//    }
//
//    @PostMapping("/register")
//    public String registerUser(@ModelAttribute("user") Users user, Model model) {
//        try {
//            userService.registerUser(user);
//            model.addAttribute("success", "Registration successful! Check your email.");
//            return "register";
//        } catch (Exception e) {
//            model.addAttribute("error", e.getMessage());
//            return "register";
//        }
//    }
//}
