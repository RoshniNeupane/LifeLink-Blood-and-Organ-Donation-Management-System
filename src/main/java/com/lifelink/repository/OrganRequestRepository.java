package com.lifelink.repository;

import com.lifelink.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrganRequestRepository extends JpaRepository<OrganRequest, Long> {
    List<OrganRequest> findByHospital(Users hospital);
    List<OrganRequest> findByDonationTypeAndStatus(DonationType type, String status);



    int countByHospitalAndDonationTypeAndStatus(
        Users hospital, DonationType donationType, String status
    );

    long countByHospital(Users hospital);
    long countByHospitalAndDonationType(Users hospital, DonationType donationType);
    List<OrganRequest> findByHospitalAndDonationTypeAndStatus(Users hospital, DonationType donationType, String status);

    long countByStatus(String status);
}
