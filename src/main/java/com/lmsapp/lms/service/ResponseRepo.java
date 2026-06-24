package com.lmsapp.lms.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lmsapp.lms.model.Response;

public interface ResponseRepo extends JpaRepository<Response, Integer> {

	@Query("SELECT r from Response r where r.responsetype = : responsetype")
	List<Response> FindResponseByResponseType(@Param("responsetype") String string);

}
