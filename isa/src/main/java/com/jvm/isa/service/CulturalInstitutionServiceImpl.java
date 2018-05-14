package com.jvm.isa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jvm.isa.domain.CulturalInstitution;
import com.jvm.isa.domain.CulturalInstitutionType;
import com.jvm.isa.repository.CulturalInstitutionRepository;

@Service
public class CulturalInstitutionServiceImpl implements CulturalInstitutionService
{
	@Autowired
	CulturalInstitutionRepository culturalInstitutionRepository;

	@Override
	public List<CulturalInstitution> getCulturalInstitutionsByType(CulturalInstitutionType type) 
	{
		return this.culturalInstitutionRepository.findByType(type);
	} 	
	
}
