package com.jvm.isa.service;

import java.util.ArrayList;
import java.util.List;

import com.jvm.isa.domain.Administrator;
import com.jvm.isa.domain.CulturalInstitution;
import com.jvm.isa.domain.Requisite;
import com.jvm.isa.domain.User;

public interface AdminService {
	
	boolean register(User admin);
	
	boolean exists(String username);
	
	public boolean existsRequisite(String name);
	
	//Administrator getUser(String username, String password);
	
	int validAdmin(Administrator oldAdmin, String username, String oldPassword, String newPassword, String repeatNewPassword, String firstName, String lastName, String email);
	
	int validSystemAdmin(User oldAdmin, String username, String oldPassword, String newPassword, String repeatNewPassword);
	
	int correctRequisite(String name, String description, String price, String culturalInstitution, String showing);

	boolean addRequisite(Requisite requisite);

	List<Requisite> getRequisites();
	
	CulturalInstitution getCulturalInstitution(String culturalInstitutionName);

	ArrayList<String> getCulturalInstitutions();

	ArrayList<String> getShowings(String culturalInstitutionName);
	


}
