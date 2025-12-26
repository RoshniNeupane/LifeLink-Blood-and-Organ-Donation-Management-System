package com.lifelink.service;

import com.lifelink.entity.BloodRequest;
import com.lifelink.repository.BloodRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BloodRequestService {

    @Autowired
    private BloodRequestRepository repository;

    public BloodRequest save(BloodRequest req) {
        return repository.save(req);
    }

    public int countAllRequests() {
        return (int) repository.count();
    }

    public List<BloodRequest> getAllRequests() {
        return repository.findAll();
    }

    public long countByStatus(String status) {
        return repository.countByStatus(status);  // bloodRequestRepo or organRequestRepo
    }

}
