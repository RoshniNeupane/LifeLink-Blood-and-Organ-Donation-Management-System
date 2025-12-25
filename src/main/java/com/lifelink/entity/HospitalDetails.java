package com.lifelink.entity;

import jakarta.persistence.*;

@Entity
public class HospitalDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hospitalName;
    private String hospitalAddress;
    private String hospitalContact;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getHospitalName() { return hospitalName; }
    public void setHospitalName(String hospitalName) { this.hospitalName = hospitalName; }

    public String getHospitalAddress() { return hospitalAddress; }
    public void setHospitalAddress(String hospitalAddress) { this.hospitalAddress = hospitalAddress; }

    public String getHospitalContact() { return hospitalContact; }
    public void setHospitalContact(String hospitalContact) { this.hospitalContact = hospitalContact; }

    public Users getUser() { return user; }
    public void setUser(Users user) { this.user = user; }
}
