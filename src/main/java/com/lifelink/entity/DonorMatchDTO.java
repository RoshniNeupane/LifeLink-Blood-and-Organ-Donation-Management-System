package com.lifelink.entity;

public class DonorMatchDTO {
    private String donorName, donorContact, bloodGroup, organType;
    private DonationType donationType;
    private String hospitalName, hospitalContact, hospitalAddress;
    private Long hospitalId;

    public DonorMatchDTO() {}

    public DonorMatchDTO(BloodDonation donation, Users hospital) {
        this.donorName = donation.getDonorName();
        this.donorContact = donation.getDonorContact();
        this.bloodGroup = donation.getBloodGroup();
        this.donationType = donation.getDonationType();
        this.hospitalName = hospital.getFullName();
        this.hospitalContact = hospital.getPhoneNumber();
        this.hospitalAddress = hospital.getHospitalDetails() != null ?
                hospital.getHospitalDetails().getHospitalAddress() : "N/A";
        this.hospitalId = hospital.getId();
    }

    public DonorMatchDTO(OrganDonation donation, Users hospital) {
        this.donorName = donation.getDonorName();
        this.donorContact = donation.getDonorContact();
        this.organType = donation.getOrganType();
        this.donationType = donation.getDonationType();
        this.hospitalName = hospital.getFullName();
        this.hospitalContact = hospital.getPhoneNumber();
        this.hospitalAddress = hospital.getHospitalDetails() != null ?
                hospital.getHospitalDetails().getHospitalAddress() : "N/A";
        this.hospitalId = hospital.getId();
    }

    // All getters/setters
    public String getDonorName() { return donorName; }
    public void setDonorName(String donorName) { this.donorName = donorName; }
    public String getDonorContact() { return donorContact; }
    public void setDonorContact(String donorContact) { this.donorContact = donorContact; }
    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public String getOrganType() { return organType; }
    public void setOrganType(String organType) { this.organType = organType; }
    public DonationType getDonationType() { return donationType; }
    public void setDonationType(DonationType donationType) { this.donationType = donationType; }
    public String getHospitalName() { return hospitalName; }
    public void setHospitalName(String hospitalName) { this.hospitalName = hospitalName; }
    public String getHospitalContact() { return hospitalContact; }
    public void setHospitalContact(String hospitalContact) { this.hospitalContact = hospitalContact; }
    public String getHospitalAddress() { return hospitalAddress; }
    public void setHospitalAddress(String hospitalAddress) { this.hospitalAddress = hospitalAddress; }
    public Long getHospitalId() { return hospitalId; }
    public void setHospitalId(Long hospitalId) { this.hospitalId = hospitalId; }
}
