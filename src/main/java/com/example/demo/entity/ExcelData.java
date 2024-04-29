package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "excel_data")
public class ExcelData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCandidate_name() {
		return candidate_name;
	}
	public void setCandidate_name(String candidate_name) {
		this.candidate_name = candidate_name;
	}
	public ExcelData() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		return "ExcelData [id=" + id + ", candidate_name=" + candidate_name + ", joining_month=" + joining_month
				+ ", phone_number=" + phone_number + ", reference=" + reference + ", bp_name=" + bp_name
				+ ", communication=" + communication + ", technical=" + technical + ", status=" + status
				+ ", scholarship=" + scholarship + ", enquired_by=" + enquired_by + "]";
	}
	public String getJoining_month() {
		return joining_month;
	}
	public void setJoining_month(String joining_month) {
		this.joining_month = joining_month;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getBp_name() {
		return bp_name;
	}
	public void setBp_name(String bp_name) {
		this.bp_name = bp_name;
	}
	public String getCommunication() {
		return communication;
	}
	public void setCommunication(String communication) {
		this.communication = communication;
	}
	public String getTechnical() {
		return technical;
	}
	public void setTechnical(String technical) {
		this.technical = technical;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getScholarship() {
		return scholarship;
	}
	public void setScholarship(String scholarship) {
		this.scholarship = scholarship;
	}
	public String getEnquired_by() {
		return enquired_by;
	}
	public void setEnquired_by(String enquired_by) {
		this.enquired_by = enquired_by;
	}
	   @Column(name = "candidate_name")
	private String candidate_name;
	private String joining_month;
	private String phone_number;
	private String reference;
	private String bp_name;
	private String communication;
	private String technical;
	private String status;
	private String scholarship;
	private String enquired_by;
	
	
    // Add more fields as needed for your Excel columns

    // Getters and setters
    // Constructors
    // Other methods
	public ExcelData(Long id, String candidate_name, String joining_month, String phone_number, String reference,
            String bp_name, String communication, String technical, String status, String scholarship,
            String enquired_by) {
this.id = id;
this.candidate_name = candidate_name;
this.joining_month = joining_month;
this.phone_number = phone_number;
this.reference = reference;
this.bp_name = bp_name;
this.communication = communication;
this.technical = technical;
this.status = status;
this.scholarship = scholarship;
this.enquired_by = enquired_by;
}
}
