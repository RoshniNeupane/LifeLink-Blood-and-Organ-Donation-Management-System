package com.lifelink.controller;

import java.util.List;

import com.lifelink.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;  // ✅ ADDED

import com.lifelink.service.BestMatchService;
import com.lifelink.service.BloodDonationService;
import com.lifelink.service.BloodRequestService;
import com.lifelink.service.OrganDonationService;
import com.lifelink.service.OrganRequestService;
import com.lifelink.service.UserService;

@Controller
public class UserDonationController {

    @Autowired private BloodDonationService bloodDonationService;
    @Autowired private OrganDonationService organDonationService;
    @Autowired private BloodRequestService bloodRequestService;
    @Autowired private OrganRequestService organRequestService;
    @Autowired private BestMatchService bestMatchService;
    @Autowired private UserService userService;

    private String getUserEmail(Object principal) {
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        } else if (principal instanceof OAuth2User oauthUser) {
            return oauthUser.getAttribute("email");
        }
        return null;
    }

    private Users getCurrentUser(Object principal) {
        String email = getUserEmail(principal);
        if (email == null) return null;
        return userService.findByEmail(email);
    }

    // --- Donate Blood ---
    @GetMapping("/donate-blood")
    public String showBloodDonateForm(Model model, @AuthenticationPrincipal Object principal) {
        model.addAttribute("donationRequest", new BloodDonation());
        List<Users> hospitals = userService.findAllByRole(Role.HOSPITAL);
        model.addAttribute("hospitals", hospitals);
        return "donateBloodForm";
    }

    @PostMapping("/donate-blood")
    public String submitBloodDonation(@ModelAttribute BloodDonation donation,
                                      @AuthenticationPrincipal Object principal,
                                      @RequestParam(value = "hospital.id", required = false) Long hospitalId) {  // ✅ FIXED

        Users user = getCurrentUser(principal);
        if (user == null) {
            return "redirect:/login?error=unauthorized";
        }

        donation.setUser(user);
        donation.setStatus("PENDING");

        if (donation.getDonationType() == null) {
            donation.setDonationType(DonationType.FREE);
        }

        // ✅ FIXED: Get hospital ID directly from @RequestParam
        System.out.println("🔍 Form hospital.id: " + hospitalId);

        if (hospitalId != null) {
            Users hospital = userService.findById(hospitalId);
            if (hospital != null) {
                donation.setHospital(hospital);
                System.out.println("✅ Blood donation assigned to Hospital ID: " + hospitalId + " (" + hospital.getFullName() + ")");
            } else {
                System.out.println("❌ Hospital not found: ID " + hospitalId);
            }
        } else {
            System.out.println("❌ No hospital selected in form");
        }

        bloodDonationService.save(donation);
        System.out.println("✅ Blood donation saved. Final hospital: " +
                (donation.getHospital() != null ? donation.getHospital().getId() : "NULL"));
        return "redirect:/ThankYou";
    }

    // --- Donate Organ ---
    @GetMapping("/donate-organ")
    public String showOrganDonateForm(Model model, @AuthenticationPrincipal Object principal) {
        model.addAttribute("donationRequest", new OrganDonation());
        List<Users> hospitals = userService.findAllByRole(Role.HOSPITAL);
        model.addAttribute("hospitals", hospitals);
        return "donateOrganForm";
    }

    @PostMapping("/donate-organ")
    public String submitOrganDonation(@ModelAttribute OrganDonation donation,
                                      @AuthenticationPrincipal Object principal,
                                      @RequestParam(value = "hospital.id", required = false) Long hospitalId) {  // ✅ FIXED

        Users user = getCurrentUser(principal);
        if (user == null) {
            return "redirect:/login?error=unauthorized";
        }

        donation.setUser(user);
        donation.setStatus("PENDING");

        if (donation.getDonationType() == null) {
            donation.setDonationType(DonationType.FREE);
        }

        // ✅ FIXED: Get hospital ID directly from @RequestParam
        System.out.println("🔍 Form hospital.id: " + hospitalId);

        if (hospitalId != null) {
            Users hospital = userService.findById(hospitalId);
            if (hospital != null) {
                donation.setHospital(hospital);
                System.out.println("✅ Organ donation assigned to Hospital ID: " + hospitalId + " (" + hospital.getFullName() + ")");
            } else {
                System.out.println("❌ Hospital not found: ID " + hospitalId);
            }
        } else {
            System.out.println("❌ No hospital selected in form");
        }

        organDonationService.save(donation);
        System.out.println("✅ Organ donation saved. Final hospital: " +
                (donation.getHospital() != null ? donation.getHospital().getId() : "NULL"));
        return "redirect:/ThankYou";
    }

    @GetMapping("/ThankYou")
    public String ThanksForDonation() {
        return "ThankYou";
    }

    // --- Request Blood ---
    @GetMapping("/request-blood")
    public String showBloodRequestForm(Model model, @AuthenticationPrincipal Object principal) {
        model.addAttribute("bloodRequest", new BloodRequest());
        return "requestBloodForm";
    }
    @PostMapping("/request-blood")
    public String submitBloodRequest(@ModelAttribute BloodRequest req,
                                     @AuthenticationPrincipal Object principal,
                                     Model model) {

        Users user = getCurrentUser(principal);
        if (user == null) {
            return "redirect:/login?error=unauthorized";
        }

        req.setStatus("PENDING");
        req.setUser(user);
        bloodRequestService.save(req);

        // ✅ ONLY APPROVED DONORS FROM ANY HOSPITAL
        List<DonorMatchDTO> bloodMatches = bestMatchService.getBestBloodMatchesGlobal(
                req.getRequiredBloodGroup());

        model.addAttribute("bloodMatches", bloodMatches);
        model.addAttribute("requiredBloodGroup", req.getRequiredBloodGroup());
        model.addAttribute("message", bloodMatches.isEmpty() ?
                "❌ No APPROVED matching donors found yet." :
                "✅ Found " + bloodMatches.size() + " APPROVED donors across all hospitals!");

        return "bloodMatchResult";
    }

    @PostMapping("/request-organ")
    public String submitOrganRequest(@ModelAttribute OrganRequest req,
                                     @AuthenticationPrincipal Object principal,
                                     Model model) {

        Users user = getCurrentUser(principal);
        if (user == null) {
            return "redirect:/login?error=unauthorized";
        }

        req.setStatus("PENDING");
        req.setUser(user);
        organRequestService.save(req);

        // ✅ ONLY APPROVED DONORS FROM ANY HOSPITAL
        List<DonorMatchDTO> organMatches = bestMatchService.getBestOrganMatchesGlobal(
                req.getOrganType());

        model.addAttribute("organMatches", organMatches);
        model.addAttribute("requiredOrgan", req.getOrganType());
        model.addAttribute("message", organMatches.isEmpty() ?
                "❌ No APPROVED organ donors found yet." :
                "✅ Found " + organMatches.size() + " APPROVED organ donors across all hospitals!");

        return "organMatchResult";
    }


    // --- Request Organ ---
    @GetMapping("/request-organ")
    public String showOrganRequestForm(Model model, @AuthenticationPrincipal Object principal) {
        model.addAttribute("organRequest", new OrganRequest());
        return "requestOrganForm";
    }

}
