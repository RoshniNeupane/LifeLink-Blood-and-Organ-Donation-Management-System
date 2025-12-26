package com.lifelink.entity;

public class DetailedHospitalStatsDTO {
    private String hospitalName;
    private Long hospitalId;
    private long bloodApproved, bloodPending, bloodRejected;
    private long organApproved, organPending, organRejected;
    private long totalApproved, totalPending, totalRejected, totalRequests;
    private double acceptanceRate;

    // All getters and setters
    public String getHospitalName() { return hospitalName; }
    public void setHospitalName(String hospitalName) { this.hospitalName = hospitalName; }
    public Long getHospitalId() { return hospitalId; }
    public void setHospitalId(Long hospitalId) { this.hospitalId = hospitalId; }
    public long getBloodApproved() { return bloodApproved; }
    public void setBloodApproved(long bloodApproved) { this.bloodApproved = bloodApproved; }
    public long getBloodPending() { return bloodPending; }
    public void setBloodPending(long bloodPending) { this.bloodPending = bloodPending; }
    public long getBloodRejected() { return bloodRejected; }
    public void setBloodRejected(long bloodRejected) { this.bloodRejected = bloodRejected; }
    public long getOrganApproved() { return organApproved; }
    public void setOrganApproved(long organApproved) { this.organApproved = organApproved; }
    public long getOrganPending() { return organPending; }
    public void setOrganPending(long organPending) { this.organPending = organPending; }
    public long getOrganRejected() { return organRejected; }
    public void setOrganRejected(long organRejected) { this.organRejected = organRejected; }
    public long getTotalApproved() { return totalApproved; }
    public void setTotalApproved(long totalApproved) { this.totalApproved = totalApproved; }
    public long getTotalPending() { return totalPending; }
    public void setTotalPending(long totalPending) { this.totalPending = totalPending; }
    public long getTotalRejected() { return totalRejected; }
    public void setTotalRejected(long totalRejected) { this.totalRejected = totalRejected; }
    public long getTotalRequests() { return totalRequests; }
    public void setTotalRequests(long totalRequests) { this.totalRequests = totalRequests; }
    public double getAcceptanceRate() { return acceptanceRate; }
    public void setAcceptanceRate(double acceptanceRate) { this.acceptanceRate = acceptanceRate; }
}
