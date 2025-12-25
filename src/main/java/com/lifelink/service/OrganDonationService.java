package com.lifelink.service;

import com.lifelink.entity.OrganDonation;
import com.lifelink.entity.Users;
import com.lifelink.entity.DonationType;
import com.lifelink.repository.OrganDonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganDonationService {

    @Autowired
    private OrganDonationRepository organDonationRepo;

    public OrganDonation save(OrganDonation donation) {
        if (donation.getStatus() == null) donation.setStatus("PENDING");
        return organDonationRepo.save(donation);
    }

    public List<OrganDonation> findAll() {
        return organDonationRepo.findAll();
    }

    public List<OrganDonation> findByHospital(Users hospital) {
        return organDonationRepo.findByHospital(hospital);
    }

    // Fixed: Use donationType FREE instead of freeDonor
    public List<OrganDonation> findPendingFreeDonors(Users hospital) {
        return organDonationRepo.findByHospitalAndDonationTypeAndStatus(hospital, DonationType.FREE, "PENDING");
    }

    public int countApprovedFreeDonors(Users hospital) {
        return (int) organDonationRepo.findByHospitalAndDonationTypeAndStatus(hospital, DonationType.FREE, "APPROVED").size();
    }

    public int countAllDonors() {
        return (int) organDonationRepo.count();
    }

    public int countAllDonorsByHospital(Users hospital) {
        return (int) organDonationRepo.countByHospital(hospital);
    }

    // Fixed: Use donationType FREE instead of freeDonor
    public int countFreeDonorsByHospital(Users hospital) {
        return (int) organDonationRepo.findByHospitalAndDonationTypeAndStatus(hospital, DonationType.FREE, "APPROVED").size();
    }

    public Optional<OrganDonation> findById(Long id) {
        return organDonationRepo.findById(id);
    }

    public void deleteById(Long id) {
        organDonationRepo.deleteById(id);
    }
}
