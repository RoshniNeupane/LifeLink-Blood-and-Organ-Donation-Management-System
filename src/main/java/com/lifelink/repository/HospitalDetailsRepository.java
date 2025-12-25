package com.lifelink.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lifelink.entity.HospitalDetails;

public interface HospitalDetailsRepository extends JpaRepository<HospitalDetails, Long> {
    List<HospitalDetails> findAll();
    HospitalDetails findByUserId(Long userId);
}
