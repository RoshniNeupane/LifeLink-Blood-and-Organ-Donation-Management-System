package com.lifelink.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lifelink.entity.BloodRequest;
import com.lifelink.entity.DonationType;
import com.lifelink.entity.Users;

@Repository
public interface BloodRequestRepository extends JpaRepository<BloodRequest, Long> {
	int countByStatus(String status);
}
