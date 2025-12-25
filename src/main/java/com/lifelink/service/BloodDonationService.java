package com.lifelink.service;

import com.lifelink.entity.BloodDonation;
import com.lifelink.entity.Users;
import com.lifelink.entity.DonationType;
import com.lifelink.repository.BloodDonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BloodDonationService {

    @Autowired
    private BloodDonationRepository bloodDonationRepo;

    public BloodDonation save(BloodDonation donation) {
        if (donation.getStatus() == null) donation.setStatus("PENDING");
        return bloodDonationRepo.save(donation);
    }

    public List<BloodDonation> findAll() {
        return bloodDonationRepo.findAll();
    }

    
    public List<BloodDonation> findByHospitalId(Long hospitalId) {
        return bloodDonationRepo.findByHospitalId(hospitalId);
    }

    public List<BloodDonation> findPendingFreeDonorsByHospitalId(Long hospitalId) {
        return bloodDonationRepo.findByHospitalIdAndDonationTypeAndStatus(hospitalId, DonationType.FREE, "PENDING");
    }

    public int countApprovedFreeDonorsByHospitalId(Long hospitalId) {
        return (int) bloodDonationRepo.findByHospitalIdAndDonationTypeAndStatus(hospitalId, DonationType.FREE, "APPROVED").size();
    }

    public int countAllDonorsByHospitalId(Long hospitalId) {
        return (int) bloodDonationRepo.findByHospitalId(hospitalId).size();
    }

    
    // Fixed: Use donationType FREE instead of freeDonor
    public List<BloodDonation> findPendingFreeDonors(Users hospital) {
        return bloodDonationRepo.findByHospitalAndDonationTypeAndStatus(hospital, DonationType.FREE, "PENDING");
    }

    public int countApprovedFreeDonors(Users hospital) {
        return (int) bloodDonationRepo.findByHospitalAndDonationTypeAndStatus(hospital, DonationType.FREE, "APPROVED").size();
    }

    public int countAllDonors() {
        return (int) bloodDonationRepo.count();
    }

    public int countAllDonorsByHospital(Users hospital) {
        return (int) bloodDonationRepo.countByHospital(hospital);
    }

    // Fixed: Use donationType FREE instead of freeDonor
    public int countFreeDonorsByHospital(Users hospital) {
        return (int) bloodDonationRepo.findByHospitalAndDonationTypeAndStatus(hospital, DonationType.FREE, "APPROVED").size();
    }

    public Optional<BloodDonation> findById(Long id) {
        return bloodDonationRepo.findById(id);
    }

    public void deleteById(Long id) {
        bloodDonationRepo.deleteById(id);
    }
}
