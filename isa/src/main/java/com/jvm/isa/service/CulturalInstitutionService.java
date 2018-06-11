package com.jvm.isa.service;

import java.util.ArrayList;
import java.util.List;

import com.jvm.isa.domain.Auditorium;
import com.jvm.isa.domain.CulturalInstitution;
import com.jvm.isa.domain.CulturalInstitutionType;
import com.jvm.isa.domain.Showing;

public interface CulturalInstitutionService {
	
	boolean exists(String name);
	
	boolean save(CulturalInstitution ci);
	
	CulturalInstitution getCulturalInstitution(String name);
	
	List<Auditorium> getAuditoriums(String culturalInstitutionName);

	List<CulturalInstitution> getCulturalInstitutionsByType(CulturalInstitutionType type);
	
	ArrayList<String> getCulturalInstitutions();
	
	ArrayList<CulturalInstitution> getAllCulturalInstitutions();
	
	ArrayList<String> getShowings(String culturalInstitutionName);
	
	ArrayList<Showing> getShowings();
	
	Showing getShowing(String name);
	
	boolean showingExists(String name);
	
	boolean deleteShowing(String name);
	
	boolean save(Showing sh);
	
	boolean delete(CulturalInstitution ci);
}
