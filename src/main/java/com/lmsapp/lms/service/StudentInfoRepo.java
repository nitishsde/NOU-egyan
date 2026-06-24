package com.lmsapp.lms.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lmsapp.lms.model.StudentInfo;

public interface StudentInfoRepo extends JpaRepository<StudentInfo, String> {

}
