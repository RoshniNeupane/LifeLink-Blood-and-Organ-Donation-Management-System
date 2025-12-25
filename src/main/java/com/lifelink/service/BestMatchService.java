package com.lifelink.service;

import com.lifelink.entity.BloodDonation;
import com.lifelink.entity.OrganDonation;
import com.lifelink.entity.DonationType;
import com.lifelink.repository.BloodDonationRepository;
import com.lifelink.repository.OrganDonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BestMatchService {

    @Autowired
    private BloodDonationRepository bloodDonationRepo;

    @Autowired
    private OrganDonationRepository organDonationRepo;

    // --- Compatible blood types ---
    private List<String> getCompatibleBloodGroups(String bloodGroup) {
        return switch (bloodGroup) {
            case "A+" -> List.of("A+", "A-", "O+", "O-");
            case "A-" -> List.of("A-", "O-");
            case "B+" -> List.of("B+", "B-", "O+", "O-");
            case "B-" -> List.of("B-", "O-");
            case "AB+" -> List.of("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"); // universal receiver
            case "AB-" -> List.of("A-", "B-", "AB-", "O-");
            case "O+" -> List.of("O+", "O-");
            case "O-" -> List.of("O-");
            default -> List.of(bloodGroup);
        };
    }

    // --- Get best blood matches (compatible, FREE first, newest first) ---
    public List<BloodDonation> getBestBloodMatches(String bloodGroup) {
        List<String> compatible = getCompatibleBloodGroups(bloodGroup);

        List<BloodDonation> donors = bloodDonationRepo.findByStatus("APPROVED").stream()
                .filter(d -> compatible.contains(d.getBloodGroup()))
                .collect(Collectors.toList());

        // Sort: FREE first, then newest first
        donors.sort(
            Comparator.comparing((BloodDonation d) -> d.getDonationType() == DonationType.PAID)
                      .thenComparing(BloodDonation::getId, Comparator.reverseOrder())
        );

        return donors;
    }

    // --- Get best organ matches (FREE first, newest first) ---
    public List<OrganDonation> getBestOrganMatches(String organType) {
        List<OrganDonation> donors = organDonationRepo
                .findByOrganTypeAndStatus(organType, "APPROVED");

        // Sort: FREE first, then newest first
        donors.sort(
            Comparator.comparing((OrganDonation d) -> d.getDonationType() == DonationType.PAID)
                      .thenComparing(OrganDonation::getId, Comparator.reverseOrder())
        );

        return donors;
    }
}
