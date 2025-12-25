package com.lifelink.repository;

import com.lifelink.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BloodDonationRepository extends JpaRepository<BloodDonation, Long> {
    // ✅ These methods MUST exist

    List<BloodDonation> findByHospitalIdAndStatusAndDonationType(Long hospitalId, String status, DonationType donationType);

    
    List<BloodDonation> findByHospitalId(Long hospitalId);
    List<BloodDonation> findByHospitalIdAndDonationTypeAndStatus(Long hospitalId, DonationType type, String status);
    // Get donations by blood group and status
    List<BloodDonation> findByBloodGroupAndStatus(String bloodGroup, String status);
    List<BloodDonation> findByHospitalIdAndStatus(Long hospitalId, String status);
    // Get donations by hospital, type (FREE/PAID), and status
    List<BloodDonation> findByHospitalAndDonationTypeAndStatus(Users hospital, DonationType donationType, String status);
    long countByHospitalIdAndStatus(Long hospitalId, String status);
    // Get donations by blood group, type, and status
    List<BloodDonation> findByBloodGroupAndDonationTypeAndStatus(String bloodGroup, DonationType donationType, String status);

    // Get donations by status
    List<BloodDonation> findByStatus(String status);

    // Count donations per hospital
    long countByHospital(Users hospital);

    // Count donations per hospital of a certain type (FREE/PAID)
    long countByHospitalAndDonationType(Users hospital, DonationType donationType);
}
