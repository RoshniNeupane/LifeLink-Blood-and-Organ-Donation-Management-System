package com.lifelink.service;

import com.lifelink.entity.*;
import com.lifelink.repository.BloodDonationRepository;
import com.lifelink.repository.OrganDonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HospitalDashboardService {

    @Autowired private BloodDonationRepository bloodRepo;
    @Autowired private OrganDonationRepository organRepo;

    // ✅ COUNTS (APPROVED ONLY)
    public long getTotalBloodDonors(Long hospitalId) {
        return bloodRepo.countByHospitalIdAndStatus(hospitalId, "APPROVED");
    }

    public long getTotalOrganDonors(Long hospitalId) {
        return organRepo.countByHospitalIdAndStatus(hospitalId, "APPROVED");
    }

    // ✅ PENDING DONORS
    public List<DonorDTO> getPendingFreeDonors(Users hospital) {
        List<DonorDTO> pending = new ArrayList<>();
        bloodRepo.findByHospitalIdAndDonationTypeAndStatus(hospital.getId(), DonationType.FREE, "PENDING")
                .forEach(b -> pending.add(new DonorDTO(b)));
        organRepo.findByHospitalIdAndDonationTypeAndStatus(hospital.getId(), DonationType.FREE, "PENDING")
                .forEach(o -> pending.add(new DonorDTO(o)));
        return pending;
    }

    public List<DonorDTO> getPendingPaidDonors(Users hospital) {
        List<DonorDTO> pending = new ArrayList<>();
        bloodRepo.findByHospitalIdAndDonationTypeAndStatus(hospital.getId(), DonationType.PAID, "PENDING")
                .forEach(b -> pending.add(new DonorDTO(b)));
        organRepo.findByHospitalIdAndDonationTypeAndStatus(hospital.getId(), DonationType.PAID, "PENDING")
                .forEach(o -> pending.add(new DonorDTO(o)));
        return pending;
    }

    // ✅ APPROVED DONORS (SEPARATE TABLES)
    public List<DonorDTO> getApprovedBloodDonors(Users hospital) {
        return bloodRepo.findByHospitalIdAndStatus(hospital.getId(), "APPROVED")
                .stream().map(DonorDTO::new).collect(Collectors.toList());
    }

    public List<DonorDTO> getApprovedOrganDonors(Users hospital) {
        return organRepo.findByHospitalIdAndStatus(hospital.getId(), "APPROVED")
                .stream().map(DonorDTO::new).collect(Collectors.toList());
    }
}
