package com.lmsapp.lms.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lmsapp.lms.model.Material;

public interface MaterialRepo extends JpaRepository<Material, Integer> {

	
    @Query("select m from Material m where m.program=:program and m.branch=:branch and m.year =:year and m.materialtype =:materialtype")
	List<Material> getMaterial(String program, String branch, String year, String materialtype);

}
