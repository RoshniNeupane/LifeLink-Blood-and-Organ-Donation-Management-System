package com.lifelink.entity;

import jakarta.persistence.*;

@Entity
public class OrganRequest {
    @Id @GeneratedValue
    private Long id;

    private String requesterName;
    private String requesterContact;
    public String getRequesterName() {
		return requesterName;
	}
    private String requiredOrgan;
	public String getRequiredOrgan() {
		return requiredOrgan;
	}

	public void setRequiredOrgan(String requiredOrgan) {
		this.requiredOrgan = requiredOrgan;
	}

	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
	}

	public String getRequesterContact() {
		return requesterContact;
	}

	public void setRequesterContact(String requesterContact) {
		this.requesterContact = requesterContact;
	}

	private String organType;
    private int age;
    private String gender;
    private String address;
    private String message;

    @Enumerated(EnumType.STRING)
    private DonationType donationType;

    private String status;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getOrganType() {
		return organType;
	}

	public void setOrganType(String organType) {
		this.organType = organType;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	  private String reason;
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getAddress() {
		return address;
	}
	private String requiredBloodGroup;
	
	public String getRequiredBloodGroup() {
		return requiredBloodGroup;
	}

	public void setRequiredBloodGroup(String requiredBloodGroup) {
		this.requiredBloodGroup = requiredBloodGroup;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public DonationType getDonationType() {
		return donationType;
	}

	public void setDonationType(DonationType donationType) {
		this.donationType = donationType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Users getHospital() {
		return hospital;
	}

	public void setHospital(Users hospital) {
		this.hospital = hospital;
	}

	@ManyToOne
    private Users user; // requester

    @ManyToOne
    private Users hospital; // hospital assigned
}
