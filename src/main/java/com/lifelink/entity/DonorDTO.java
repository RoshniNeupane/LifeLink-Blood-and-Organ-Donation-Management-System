package com.lifelink.entity;

public class DonorDTO {
    private Long id;
    private String name, contact, gender, type, bloodGroup, organType;
    private DonationType donationType;

    public DonorDTO() {}

    public DonorDTO(BloodDonation donation) {
        this.id = donation.getId();
        this.name = donation.getDonorName();
        this.contact = donation.getDonorContact();
        this.gender = donation.getGender();
        this.type = "BLOOD";
        this.donationType = donation.getDonationType();
        this.bloodGroup = donation.getBloodGroup();
    }

    public DonorDTO(OrganDonation donation) {
        this.id = donation.getId();
        this.name = donation.getDonorName();
        this.contact = donation.getDonorContact();
        this.gender = donation.getGender();
        this.type = "ORGAN";
        this.donationType = donation.getDonationType();
        this.organType = donation.getOrganType();
    }

    // All getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public String getOrganType() { return organType; }
    public void setOrganType(String organType) { this.organType = organType; }
    public DonationType getDonationType() { return donationType; }
    public void setDonationType(DonationType donationType) { this.donationType = donationType; }
}
