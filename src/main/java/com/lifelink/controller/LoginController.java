package com.lifelink.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lifelink.entity.Role;
import com.lifelink.entity.Users;
import com.lifelink.service.BloodDonationService;
import com.lifelink.service.BloodRequestService;
import com.lifelink.service.OrganDonationService;
import com.lifelink.service.OrganRequestService;
import com.lifelink.service.UserService;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;
@Autowired
private BloodDonationService bloodDonationService;
@Autowired
private OrganDonationService organDonationService;
@Autowired
private BloodRequestService bloodRequestService;
@Autowired
private OrganRequestService organRequestService;
    // ===== DASHBOARD =====
    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal Object principal) {
        String username = "Guest";
        String profilePicUrl = "/uploads/blank_profile.jpg";
        Users user = null;
        Role role = Role.USER;

        if (principal instanceof UserDetails userDetails) {
            user = userService.findByEmail(userDetails.getUsername());
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

            profilePicUrl = oauthUser.getAttribute("picture");
            if (profilePicUrl == null || profilePicUrl.isEmpty())
                profilePicUrl = oauthUser.getAttribute("avatar_url");
            if (profilePicUrl == null || profilePicUrl.isEmpty())
                profilePicUrl = "/uploads/blank_profile.jpg";

            user = userService.findByEmail(email);
            if (user == null) {
                user = new Users();
                user.setEmail(email);
                user.setFullName(username);
                user.setProfileImagePath(profilePicUrl);
                user.setRole(Role.USER);
                userService.registerOAuthUser(user); // save new OAuth user
            }
        }

        if (user != null) {
            username = user.getFullName();
            profilePicUrl = user.getProfileImagePath();
            role = user.getRole() != null ? user.getRole() : Role.USER;
        }
        int totalUsers = userService.countAllUsers();
        int totalBloodDonors = bloodDonationService.countAllDonors();
        int totalOrganDonors = organDonationService.countAllDonors();
        int totalRequests = bloodRequestService.countAllRequests() + organRequestService.countAllRequests();

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalBloodDonors", totalBloodDonors);
        model.addAttribute("totalOrganDonors", totalOrganDonors);
        model.addAttribute("totalRequests", totalRequests);

       
        model.addAttribute("username", username);
        model.addAttribute("profilePicUrl", profilePicUrl);
        model.addAttribute("user", user);
        model.addAttribute("role", role);
        model.addAttribute("isAdmin", role == Role.ADMIN);

        // Return template based on role
        return switch (role) {
            case ADMIN -> "adminDashboard";
            case HOSPITAL -> "hospitalDashboard";
            default -> "dashboard";
        };
    }

    // ===== POST LOGIN REDIRECT =====
    @GetMapping("/postLogin")
    public String postLoginRedirect(Authentication authentication) {
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/dashboard";
        } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_HOSPITAL"))) {
            return "redirect:/dashboard";
        } else {
            return "redirect:/dashboard";
        }
    }

    // ===== LOGIN PAGE =====
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
        model.addAttribute("activeDonors", userService.countByRole(Role.USER)); // adapt if needed
        model.addAttribute("bloodUnits", 2000); // fetch real data
        model.addAttribute("organRequests", 50); // fetch real data
        model.addAttribute("recentUsers", userService.findAllByRole(Role.USER).subList(0,5));

        model.addAttribute("donationMonths", List.of("Jan","Feb","Mar","Apr","May"));
        model.addAttribute("bloodUnitsPerMonth", List.of(10,20,15,25,30));
        model.addAttribute("registrationMonths", List.of("Jan","Feb","Mar","Apr","May"));
        model.addAttribute("newUsersPerMonth", List.of(5,15,10,20,25));

        return "adminDashboard";
    }
    @GetMapping("/")
    public String mainPage() {
        return "MainPage";
    }
}
