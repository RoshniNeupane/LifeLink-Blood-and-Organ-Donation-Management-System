package com.lifelink.service;

import com.lifelink.entity.*;
import com.lifelink.repository.BloodDonationRepository;
import com.lifelink.repository.OrganDonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class BestMatchService {

    @Autowired private BloodDonationRepository bloodDonationRepo;
    @Autowired private OrganDonationRepository organDonationRepo;

    // ✅ APPROVED DONORS ONLY - Uses findByStatus("APPROVED")
    public List<DonorMatchDTO> getBestBloodMatchesGlobal(String bloodGroup) {
        List<String> compatible = getCompatibleBloodGroups(bloodGroup);
        List<DonorMatchDTO> matches = new ArrayList<>();

        // ✅ NOW WORKS - Only approved donors
        List<BloodDonation> approvedDonors = bloodDonationRepo.findByStatus("APPROVED");

        for (BloodDonation donation : approvedDonors) {
            if (compatible.contains(donation.getBloodGroup()) && donation.getHospital() != null) {
                matches.add(new DonorMatchDTO(donation, donation.getHospital()));
            }
        }

        matches.sort(Comparator.comparing((DonorMatchDTO m) -> m.getDonationType() == DonationType.PAID)
                .thenComparing(DonorMatchDTO::getHospitalName));
        return matches;
    }

    // ✅ APPROVED DONORS ONLY - Uses findByStatus("APPROVED")
    public List<DonorMatchDTO> getBestOrganMatchesGlobal(String organType) {
        List<DonorMatchDTO> matches = new ArrayList<>();

        // ✅ NOW WORKS - Only approved donors
        List<OrganDonation> approvedDonors = organDonationRepo.findByStatus("APPROVED");

        for (OrganDonation donation : approvedDonors) {
            if (organType.equalsIgnoreCase(donation.getOrganType()) && donation.getHospital() != null) {
                matches.add(new DonorMatchDTO(donation, donation.getHospital()));
            }
        }

        matches.sort(Comparator.comparing((DonorMatchDTO m) -> m.getDonationType() == DonationType.PAID)
                .thenComparing(DonorMatchDTO::getHospitalName));
        return matches;
    }

    private List<String> getCompatibleBloodGroups(String bloodGroup) {
        return switch (bloodGroup) {
            case "A+" -> List.of("A+", "A-", "O+", "O-");
            case "A-" -> List.of("A-", "O-");
            case "B+" -> List.of("B+", "B-", "O+", "O-");
            case "B-" -> List.of("B-", "O-");
            case "AB+" -> List.of("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
            case "AB-" -> List.of("A-", "B-", "AB-", "O-");
            case "O+" -> List.of("O+", "O-");
            case "O-" -> List.of("O-");
            default -> List.of(bloodGroup);
        };
    }
}
