//package com.lifelink.controller;
//import java.time.LocalDateTime;
//import java.time.temporal.ChronoUnit;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.lifelink.entity.Users;
//import com.lifelink.repository.UserRepository;
//import com.lifelink.service.EmailService;
//
//@Controller
//@RequestMapping("/auth")
//public class OtpController {
//
//    @Autowired private UserRepository userRepository;
//    @Autowired private EmailService emailService;
//    private final long OTP_EXPIRY_MINUTES = 10;
//
//    @GetMapping("/verify-otp-page")
//    public String showVerifyOtpPage(@RequestParam String email, Model model){
//        model.addAttribute("email", email);
//        return "verify";
//    }
//
//    @PostMapping("/verify-otp")
//    public String verifyOtp(@RequestParam String email,
//                            @RequestParam String code,
//                            Model model) {
//
//        Optional<Users> optionalUser = userRepository.findByEmail(email);
//
//        if(optionalUser.isPresent()){
//            Users user = optionalUser.get();
//
//            // Already verified → redirect directly
//            if(user.isVerified()) {
//                return roleRedirect(user);
//            }
//
//            // Check OTP expiry
//            long minutes = ChronoUnit.MINUTES.between(user.getCodeGeneratedAt(), LocalDateTime.now());
//            if(minutes > OTP_EXPIRY_MINUTES){
//                model.addAttribute("error","OTP expired. Please resend OTP.");
//                model.addAttribute("email", email);
//                return "verify";
//            }
//
//            // Check OTP correctness
//            if(user.getVerificationCode().equals(code)){
//                user.setVerified(true);          // mark user as verified
//                user.setVerificationCode(null);  // clear OTP
//                userRepository.save(user);
//
//                // Role-based redirect
//                return roleRedirect(user);
//
//            } else {
//                model.addAttribute("error","Invalid OTP code. Please try again.");
//                model.addAttribute("email", email);
//                return "verify";
//            }
//        }
//
//        model.addAttribute("error","Email not found.");
//        return "verify";
//    }
//
//    // Reusable role redirect method (same as in LoginController)
//    private String roleRedirect(Users user){
//        String role = user.getRole().toUpperCase();
//        switch(role){
//            case "ADMIN":
//                return "redirect:/admin-dashboard";
//            case "USER":
//                return "redirect:/user-dashboard";
//            case "HOSPITAL":
//                return "redirect:/hospital-dashboard";
//            default:
//                return "redirect:/dashboard";
//        }
//    }
//
//    @PostMapping("/resend-otp")
//    @ResponseBody
//    public Map<String,Object> resendOtp(@RequestParam String email){
//        Map<String,Object> resp = new HashMap<>();
//        Optional<Users> optionalUser = userRepository.findByEmail(email);
//        if(optionalUser.isPresent()){
//            Users user = optionalUser.get();
//            if(user.isVerified()){
//                resp.put("status","error");
//                resp.put("message","User already verified");
//                return resp;
//            }
//            String code = String.valueOf((int)(Math.random()*900000)+100000);
//            user.setVerificationCode(code);
//            user.setCodeGeneratedAt(LocalDateTime.now());
//            emailService.sendEmail(user.getEmail(),"Verify your account","OTP: "+code);
//            userRepository.save(user);
//            resp.put("status","success");
//            resp.put("message","OTP resent successfully");
//            return resp;
//        }
//        resp.put("status","error");
//        resp.put("message","Email not found");
//        return resp;
//    }
//}
