package com.lifelink.entity;

import jakarta.persistence.*;

@Entity
public class BloodDonation {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    private String donorName;
    private String donorContact;
    private String bloodGroup;
    private int age;
    private String gender;
    private String address;
    private String message;

    @Enumerated(EnumType.STRING)
    private DonationType donationType; // FREE or PAID

    private String status; // PENDING, APPROVED, REJECTED

   

    @ManyToOne
    private Users user; // Donor (may be null if anonymous)

    @ManyToOne
    private Users hospital; // Hospital where donation registered

    // Getters & setters
    // ... (generate for all fields)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDonorName() { return donorName; }
    public void setDonorName(String donorName) { this.donorName = donorName; }

    public String getDonorContact() { return donorContact; }
    public void setDonorContact(String donorContact) { this.donorContact = donorContact; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public DonationType getDonationType() { return donationType; }
    public void setDonationType(DonationType donationType) { this.donationType = donationType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Users getUser() { return user; }
    public void setUser(Users user) { this.user = user; }

    public Users getHospital() { return hospital; }
    public void setHospital(Users hospital) { this.hospital = hospital; }
}
