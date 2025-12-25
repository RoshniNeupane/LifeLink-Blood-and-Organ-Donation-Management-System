package com.lifelink.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.lifelink.entity.BloodDonation;
import com.lifelink.entity.BloodRequest;
import com.lifelink.entity.DonationType;
import com.lifelink.entity.OrganDonation;
import com.lifelink.entity.OrganRequest;
import com.lifelink.entity.Role;
import com.lifelink.entity.Users;
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

    // --- Donate Blood ---
    @GetMapping("/donate-blood")
    public String showBloodDonateForm(Model model) {
    	model.addAttribute("donationRequest", new BloodDonation());
    	 List<Users> hospitals = userService.findAllByRole(Role.HOSPITAL);
    	    model.addAttribute("hospitals", hospitals);
        return "donateBloodForm";
    }
    @PostMapping("/donate-blood")
    public String submitBloodDonation(@ModelAttribute BloodDonation donation,
                                      @AuthenticationPrincipal UserDetails userDetails) {

        Users user = userService.findByEmail(userDetails.getUsername());
        donation.setUser(user);

        donation.setStatus("PENDING");

        // Only set default if null
        if (donation.getDonationType() == null) {
            donation.setDonationType(DonationType.FREE);
        }

        if (donation.getHospital() != null && donation.getHospital().getId() != null) {
            Users hospital = userService.findById(donation.getHospital().getId());
            donation.setHospital(hospital);
        }

        bloodDonationService.save(donation);
        return "redirect:/ThankYou";
    }

    // --- Donate Organ ---
    @GetMapping("/donate-organ")
    public String showOrganDonateForm(Model model) {
    	model.addAttribute("donationRequest", new OrganDonation());
    	 List<Users> hospitals = userService.findAllByRole(Role.HOSPITAL);
    	    model.addAttribute("hospitals", hospitals);
        return "donateOrganForm";
    }
    @PostMapping("/donate-organ")
    public String submitOrganDonation(@ModelAttribute OrganDonation donation,
                                      @AuthenticationPrincipal UserDetails userDetails) {

        Users user = userService.findByEmail(userDetails.getUsername());
        donation.setUser(user);

        donation.setStatus("PENDING");

        if (donation.getDonationType() == null) {
            donation.setDonationType(DonationType.FREE);
        }

        if (donation.getHospital() != null && donation.getHospital().getId() != null) {
            Users hospital = userService.findById(donation.getHospital().getId());
            donation.setHospital(hospital);
        }

        organDonationService.save(donation);
        return "redirect:/ThankYou";
    }

    @GetMapping("ThankYou")
    public String ThanksForDonation() {
    	return "ThankYou";
    }

    // --- Request Blood ---
    @GetMapping("/request-blood")
    public String showBloodRequestForm(Model model) {
    	model.addAttribute("bloodRequest", new BloodRequest());

        return "requestBloodForm";
    }
   


    // --- Request Organ ---
    @GetMapping("/request-organ")
    public String showOrganRequestForm(Model model) {
    	model.addAttribute("organRequest", new OrganRequest());
    	
        return "requestOrganForm";
    }
    @PostMapping("/request-blood")
    public String submitBloodRequest(@ModelAttribute BloodRequest req,
                                     @AuthenticationPrincipal UserDetails userDetails,
                                     Model model) {

        req.setStatus("PENDING");
        if (userDetails != null) {
            Users user = userService.findByEmail(userDetails.getUsername());
            req.setUser(user);
        }
        bloodRequestService.save(req);

        // Get best blood matches using BestMatchService
        List<BloodDonation> bloodMatches = bestMatchService.getBestBloodMatches(req.getRequiredBloodGroup());
        model.addAttribute("bloodMatches", bloodMatches);

        return "bloodMatchResult"; 
    }

    @PostMapping("/request-organ")
    public String submitOrganRequest(@ModelAttribute OrganRequest req,
                                     @AuthenticationPrincipal UserDetails userDetails,
                                     Model model) {

        req.setStatus("PENDING");
        if (userDetails != null) {
            Users user = userService.findByEmail(userDetails.getUsername());
            req.setUser(user);
        }
        organRequestService.save(req);

        // Get best organ matches using BestMatchService
        List<OrganDonation> organMatches = bestMatchService.getBestOrganMatches(req.getOrganType());
        model.addAttribute("organMatches", organMatches);

        return "organMatchResult"; 
    }



}
