package com.lifelink.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class OrganDonation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String donorName;
    private String donorContact;
    private String organType;
    private int age;
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDonorName() {
		return donorName;
	}

	public void setDonorName(String donorName) {
		this.donorName = donorName;
	}

	public String getDonorContact() {
		return donorContact;
	}

	public void setDonorContact(String donorContact) {
		this.donorContact = donorContact;
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

	public String getAddress() {
		return address;
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

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
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

	private String gender;
    private String address;
    private String message;
    private String bloodGroup;

    @Enumerated(EnumType.STRING)
    private DonationType donationType; // FREE or PAID

    private String status; // PENDING, APPROVED, REJECTED

    @ManyToOne
    private Users user;

    @ManyToOne
    private Users hospital;

    // getters/setters
}
