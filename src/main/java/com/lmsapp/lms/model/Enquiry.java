package com.lmsapp.lms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "enquiries")
public class Enquiry {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;

@Column(length = 50, nullable = false)
private String name ;

@Column(length=6, nullable=false)
private String gender ;

@Column(length=15, nullable=false)
private String contactno;

public String getContactno() {
	return contactno;
}

public void setContactno(String contactno) {
	this.contactno = contactno;
}

@Column(length=100, nullable=false)
private String emailaddress;

@Column(length=1000, nullable=false)
private String enquirytext ;

@Column(length=100, nullable=false)
private String posteddate;

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public String getGender() {
	return gender;
}

public void setGender(String gender) {
	this.gender = gender;
}

public String getEmailaddress() {
	return emailaddress;
}

public void setEmailaddress(String emailaddress) {
	this.emailaddress = emailaddress;
}

public String getEnquirytext() {
	return enquirytext;
}

public void setEnquirytext(String enquirytext) {
	this.enquirytext = enquirytext;
}

public String getPosteddate() {
	return posteddate;
}

public void setPosteddate(String posteddate) {
	this.posteddate = posteddate;
}

}
