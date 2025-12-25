package com.lifelink.entity;

import jakarta.persistence.*;

@Entity
public class BloodRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String requesterName;
    private String requesterContact;
   
    private String status; 
    private Integer age;
    private String gender;
    public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Integer getAge() {
		return age;
	}
	private String reason;

    // getter
    public String getReason() {
        return reason;
    }

    // setter
    public void setReason(String reason) {
        this.reason = reason;
    }

	private String requiredBloodGroup;

    // getter
    public String getRequiredBloodGroup() {
        return requiredBloodGroup;
    }

    // setter
    public void setRequiredBloodGroup(String requiredBloodGroup) {
        this.requiredBloodGroup = requiredBloodGroup;
    }
	private String address;

    // getter
    public String getAddress() {
        return address;
    }

    // setter
    public void setAddress(String address) {
        this.address = address;
    }
	
	public void setAge(Integer age) {
		this.age = age;
	}
	@Enumerated(EnumType.STRING)
   
    @ManyToOne
    private Users user; 
    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRequesterName() { return requesterName; }
    public void setRequesterName(String requesterName) { this.requesterName = requesterName; }

    public String getRequesterContact() { return requesterContact; }
    public void setRequesterContact(String requesterContact) { this.requesterContact = requesterContact; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Users getUser() { return user; }
    public void setUser(Users user) { this.user = user; }
}
