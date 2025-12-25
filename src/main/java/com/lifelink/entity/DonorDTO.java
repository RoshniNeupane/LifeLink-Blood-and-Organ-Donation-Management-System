package com.lifelink.entity;

import com.lifelink.entity.BloodDonation;
import com.lifelink.entity.OrganDonation;
import com.lifelink.entity.DonationType;

public class DonorDTO {
    private Long id;
    private String username;
    private String bloodGroup;
    private String organType;
    private boolean freeDonor;
    private String type; // "BLOOD" or "ORGAN"
    private DonationType donationType;

    public DonorDTO(BloodDonation b) {
        this.id = b.getId();
        this.username = b.getUser().getFullName();
        this.bloodGroup = b.getBloodGroup();
        this.organType = null;
        this.type = "BLOOD";
        this.donationType = b.getDonationType();
    }

    public DonorDTO(OrganDonation o) {
        this.id = o.getId();
        this.username = o.getUser().getFullName();
        this.bloodGroup = null;
        this.organType = o.getOrganType();
        this.type = "ORGAN";
        this.donationType = o.getDonationType();
    }

    // getter
    public DonationType getDonationType() { return donationType; }


    // getters only (Thymeleaf needs them)
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getBloodGroup() { return bloodGroup; }
    public String getOrganType() { return organType; }
    public boolean isFreeDonor() { return freeDonor; }
    public String getType() { return type; }
   
}
