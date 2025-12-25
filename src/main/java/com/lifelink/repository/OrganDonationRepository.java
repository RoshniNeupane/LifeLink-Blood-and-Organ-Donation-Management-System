package com.lifelink.repository;

import com.lifelink.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrganDonationRepository extends JpaRepository<OrganDonation, Long> {

    // Get all donations for a hospital
    List<OrganDonation> findByHospital(Users hospital);
    List<OrganDonation> findByHospitalIdAndStatus(Long hospitalId, String status);
    
    List<OrganDonation> findByHospitalId(Long hospitalId);
    List<OrganDonation> findByHospitalIdAndDonationTypeAndStatus(Long hospitalId, DonationType type, String status);
    // Get donations by organ type and status
    List<OrganDonation> findByOrganTypeAndStatus(String organType, String status);

    // Get donations by hospital, type (FREE/PAID), and status
    List<OrganDonation> findByHospitalAndDonationTypeAndStatus(Users hospital, DonationType donationType, String status);
    long countByHospitalIdAndStatus(Long hospitalId, String status);
    // Get donations by organ type, type, and status
    List<OrganDonation> findByOrganTypeAndDonationTypeAndStatus(String organType, DonationType donationType, String status);
    // ✅ These methods MUST exist

    List<OrganDonation> findByHospitalIdAndStatusAndDonationType(Long hospitalId, String status, DonationType donationType);

    // Count donations per hospital
    long countByHospital(Users hospital);

    // Count donations per hospital of a certain type (FREE/PAID)
    long countByHospitalAndDonationType(Users hospital, DonationType donationType);
}
