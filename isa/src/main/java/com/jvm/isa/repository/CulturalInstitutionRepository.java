package com.jvm.isa.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import com.jvm.isa.domain.CulturalInstitution;
import com.jvm.isa.domain.CulturalInstitutionType;

public interface CulturalInstitutionRepository  extends Repository<CulturalInstitution, Long> {
	

	List<CulturalInstitution> findByType(CulturalInstitutionType type);
	
	List<CulturalInstitution> findAll();

	CulturalInstitution findByName(String name);
	
	CulturalInstitution save(CulturalInstitution culturalInstitution);
	
	void delete(CulturalInstitution ci);
}
