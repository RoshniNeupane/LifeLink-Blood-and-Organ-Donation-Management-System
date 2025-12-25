package com.lifelink.service;

import com.lifelink.entity.*;
import com.lifelink.repository.BloodDonationRepository;
import com.lifelink.repository.OrganDonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HospitalDashboardService {

    @Autowired
    private BloodDonationRepository bloodDonationRepository;

    @Autowired
    private OrganDonationRepository organDonationRepository;

    /** Get combined list of all donors (blood + organ) */
    public List<DonorDTO> getAllDonorsCombined(Users hospital) {
        List<BloodDonation> blood = bloodDonationRepository.findByHospitalId(hospital.getId());
        List<OrganDonation> organ = organDonationRepository.findByHospitalId(hospital.getId());

        List<DonorDTO> combined = new ArrayList<>();
        blood.forEach(b -> combined.add(new DonorDTO(b)));
        organ.forEach(o -> combined.add(new DonorDTO(o)));

        return combined;
    }

    /** Get pending FREE donors only */
    public List<DonorDTO> getPendingFreeDonors(Users hospital) {
        List<BloodDonation> blood = bloodDonationRepository
                .findByHospitalIdAndDonationTypeAndStatus(hospital.getId(), DonationType.FREE, "PENDING");
        List<OrganDonation> organ = organDonationRepository
                .findByHospitalIdAndDonationTypeAndStatus(hospital.getId(), DonationType.FREE, "PENDING");

        List<DonorDTO> pending = new ArrayList<>();
        blood.forEach(b -> pending.add(new DonorDTO(b)));
        organ.forEach(o -> pending.add(new DonorDTO(o)));

        return pending;
    }

    /** Get total donors (blood + organ) */
    public long getTotalDonors(Users hospital) {
        long totalBlood = bloodDonationRepository.findByHospitalId(hospital.getId()).size();
        long totalOrgan = organDonationRepository.findByHospitalId(hospital.getId()).size();
        return totalBlood + totalOrgan;
    }

    /** Get total approved FREE donors (blood + organ) */
    public long getTotalFreeDonorsApproved(Users hospital) {
        long approvedBlood = bloodDonationRepository
                .findByHospitalIdAndDonationTypeAndStatus(hospital.getId(), DonationType.FREE, "APPROVED").size();
        long approvedOrgan = organDonationRepository
                .findByHospitalIdAndDonationTypeAndStatus(hospital.getId(), DonationType.FREE, "APPROVED").size();
        return approvedBlood + approvedOrgan;
    }

}
