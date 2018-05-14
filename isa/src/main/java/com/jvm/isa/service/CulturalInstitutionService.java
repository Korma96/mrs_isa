package com.jvm.isa.service;

import java.util.List;

import com.jvm.isa.domain.CulturalInstitution;
import com.jvm.isa.domain.CulturalInstitutionType;

public interface CulturalInstitutionService
{
	List<CulturalInstitution> getCulturalInstitutionsByType(CulturalInstitutionType type);
}
