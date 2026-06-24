package com.lmsapp.lms.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lmsapp.lms.model.AdminLogin;

public interface AdminLoginRepo extends JpaRepository<AdminLogin, String> {

}
