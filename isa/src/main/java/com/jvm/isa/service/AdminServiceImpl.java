package com.jvm.isa.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jvm.isa.domain.Administrator;
import com.jvm.isa.domain.CulturalInstitution;
import com.jvm.isa.domain.Requisite;
import com.jvm.isa.repository.AdminRepository;
import com.jvm.isa.repository.RequisiteRepository;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private RequisiteRepository requisiteRepository;
	
	@Autowired
	private CulturalInstitutionService culturalInstitutionService; 

	@Override
	public boolean register(Administrator admin) {
		try {
			adminRepository.save(admin);

		} catch (Exception e) {

			return false;
		}
		return true;

	}

	@Override
	public boolean exists(String username) {

		return adminRepository.findByUsername(username) != null;

	}
	@Override
	public boolean existsRequisite(String name) {
		return requisiteRepository.findByName(name) != null;
	}

	@Override
	public Administrator getUser(String username, String password) {

		return adminRepository.findByUsernameAndPassword(username, password);
	}

	@Override
	public int validAdmin(Administrator oldAdmin, String username, String oldPassword, String newPassword,
			String repeatNewPassword, String firstName, String lastName, String email) {

		if (username.equals("") || newPassword.equals("") || repeatNewPassword.equals("") || firstName.equals("")
				|| lastName.equals("") || email.equals(""))
			return 0;

		if (oldPassword != null) {
			if (oldPassword.equals(""))
				return 0;
			if (!oldAdmin.getPassword().equals(oldPassword))
				return 2;
		}

		if (oldAdmin != null) {
			if (exists(username) && !oldAdmin.getUsername().equals(username))
				return 1;
		} else {
			if (exists(username))
				return 1;
		}

		if (!newPassword.equals(repeatNewPassword))
			return 3;
		if (!EmailValidator.getInstance().isValid(email))
			return 4;

		return 5;
	}

	@Override
	public int correctRequisite(String name, String description, String priceStr, String culturalInstitutionName,
			String showing) {
		if (name.equals("") || description.equals("") || priceStr.equals("") || culturalInstitutionName.equals("")
				|| showing.equals("")) {
			return 0;
		}

		if (exists(name)) {
			return 1;
		}

		double price;
		try {
			price = Double.parseDouble(priceStr);
		} catch (Exception e) {
			return 2;
		}

		if (price < 0) {
			return 3;
		}

		CulturalInstitution cInstitution = culturalInstitutionService.getCulturalInstitution(culturalInstitutionName);
		if (cInstitution == null) {
			return 4;
		}

		if (!cInstitution.containsShowing(showing)) {
			return 5;
		}

		return 6;

	}

	@Override
	public boolean addRequisite(Requisite requisite) {
		try {
			requisiteRepository.save(requisite);
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	@Override
	public CulturalInstitution getCulturalInstitution(String culturalInstitutionName) {
		return culturalInstitutionService.getCulturalInstitution(culturalInstitutionName);
	}

	@Override
	public ArrayList<String> getCulturalInstitutions() {
		return culturalInstitutionService.getCulturalInstitutions();
	}

	@Override
	public ArrayList<String> getShowings(String culturalInstitutionName) {
		return culturalInstitutionService.getShowings(culturalInstitutionName);
	}

	@Override
	public List<Requisite> getRequisites() {
		return requisiteRepository.findAll();
	}
}
