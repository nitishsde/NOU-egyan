package com.lmsapp.lms.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lmsapp.lms.model.Enquiry;

public interface EnquiryRepo extends JpaRepository<Enquiry, Integer> {

}
