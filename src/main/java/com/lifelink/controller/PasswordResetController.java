package com.lifelink.controller;

import com.lifelink.entity.Users;
import com.lifelink.service.EmailService;
import com.lifelink.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
public class PasswordResetController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(Model model) {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email,
                                        RedirectAttributes redirectAttributes,
                                        HttpServletRequest request) {
        Users user = userService.findByEmail(email);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "No user found with that email address.");
            return "redirect:/forgot-password";
        }

        String token = userService.createPasswordResetTokenForUser(user);
        String resetUrl = request.getScheme() + "://" + request.getServerName() + ":" +
                request.getServerPort() + "/reset-password?token=" + token;
        emailService.sendPasswordResetEmail(user.getEmail(), resetUrl);

        redirectAttributes.addFlashAttribute("message", "Password reset link sent to your email!");
        return "redirect:/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token,
                                        Model model,
                                        RedirectAttributes redirectAttributes) {
        Users user = userService.getUserByPasswordResetToken(token);
        if (user == null || user.getResetPasswordTokenExpiry() == null ||
                user.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("error", "Invalid or expired token.");
            return "redirect:/login";
        }

        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token,
                                       @RequestParam("password") String password,
                                       @RequestParam("confirmPassword") String confirmPassword,
                                       RedirectAttributes redirectAttributes) {

        if (!password.equals(confirmPassword)) {
            return "redirect:/reset-password?token=" + token + "&error=Passwords do not match.";
        }

        if (!isValidPassword(password)) {
            return "redirect:/reset-password?token=" + token + "&error=Password must have 8+ chars, uppercase, lowercase, digit, special char.";
        }

        Users user = userService.getUserByPasswordResetToken(token);
        if (user == null || user.getResetPasswordTokenExpiry() == null ||
                user.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("error", "Invalid or expired token.");
            return "redirect:/login";
        }

        userService.changeUserPassword(user, password);
        userService.clearPasswordResetToken(user);

        redirectAttributes.addFlashAttribute("message", "Password reset successfully! Please login.");
        return "redirect:/login";
    }

    private boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) return false;
        return password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[!@#$%^&*(),.?\":{}|<>\\[\\];'\\\\].*");
    }
}
