package com.jvm.isa.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.jvm.isa.domain.CulturalInstitution;
import com.jvm.isa.domain.RegisteredUser;
import com.jvm.isa.domain.Term;

public interface TermService {
	
	boolean save(Term term);
	
	Boolean bookSelectedSeats(String dateStr, String timeStr, String culturalInstitutionName, String showingName, String selectedSeats, RegisteredUser owner);
		
	 ArrayList<Integer> getIndexOfBusySeatsAndRowsCols(Term term);

	Term getTerm(String dateStr, String timeStr, String culturalInstitutionName, String showingName);
	
	ArrayList<String> getCulturalInstitutions();
	
	ArrayList<String> getShowings(String culturalInstitutionName);
	
	ArrayList<String> getDates(String culturalInstitutionName, String showingName);
	
	ArrayList<String> getAuditoriumAndTime(String culturalInstitutionName, String showingName, String dateStr);
	
	int thereAreRepetitionsOrNotPutYourself(Collection<String> values, String loggedUsername);
	
	List<String> getTermsByDateAndAuditoriumAndShowing(String date, String auditorium, String showing);
	
	boolean addTerm(String culturalInstitutionName, String date, String auditoriumName, String showingName, String time);
	
	boolean deleteTerm(String id);
	
}
