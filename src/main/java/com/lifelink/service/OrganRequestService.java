package com.lifelink.service;

import com.lifelink.entity.OrganRequest;
import com.lifelink.entity.Users;
import com.lifelink.entity.DonationType;
import com.lifelink.repository.OrganRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganRequestService {

    @Autowired
    private OrganRequestRepository organRequestRepo;

    public OrganRequest save(OrganRequest request) {
        if (request.getStatus() == null) request.setStatus("PENDING");
        return organRequestRepo.save(request);
    }

    public List<OrganRequest> findAll() {
        return organRequestRepo.findAll();
    }

  
    public int countAllRequests() {
        return (int) organRequestRepo.count();
    }

    public int countFreeRequests(Users hospital) {
        return (int) organRequestRepo.countByHospitalAndDonationType(hospital, DonationType.FREE);
    }

    public int countAllRequestsByHospital(Users hospital) {
        return (int) organRequestRepo.countByHospital(hospital);
    }

    public Optional<OrganRequest> findById(Long id) {
        return organRequestRepo.findById(id);
    }

    public void deleteById(Long id) {
        organRequestRepo.deleteById(id);
    }
    
    public List<OrganRequest> findByHospital(Users hospital) {
        return organRequestRepo.findByHospital(hospital);
    }

    public List<OrganRequest> findPendingFreeRequests(Users hospital) {
        return organRequestRepo.findByHospitalAndDonationTypeAndStatus(
                hospital, DonationType.FREE, "PENDING"
        );
    }

    public int countApprovedFreeRequests(Users hospital) {
        return organRequestRepo.countByHospitalAndDonationTypeAndStatus(
                hospital, DonationType.FREE, "APPROVED"
        );
    }

}
