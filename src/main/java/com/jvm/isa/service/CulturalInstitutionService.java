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
	
	boolean showingExists(CulturalInstitution culturalInstitution, String name);
	
	boolean deleteShowing(Showing s);
	
	boolean save(Showing showing, CulturalInstitution culturalInstitution);
	
	boolean delete(CulturalInstitution ci);

	boolean auditoriumExists(String name);

	boolean saveAuditorium(Auditorium a);

	Auditorium getAuditorium(String oldName);

	boolean deleteAuditorium(String name);

	boolean save(Showing s);

}
