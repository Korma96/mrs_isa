package com.jvm.isa.service;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jvm.isa.domain.Administrator;
import com.jvm.isa.repository.AdminRepository;

@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	private AdminRepository adminRepository;

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
	public Administrator getUser(String username, String password) {
		
		return adminRepository.findByUsernameAndPassword(username, password);
	}

	@Override
	public int validAdmin(Administrator oldAdmin, String username, String oldPassword, String newPassword,
			String repeatNewPassword, String firstName, String lastName, String email) {

		if(username.equals("") || newPassword.equals("") || repeatNewPassword.equals("") || firstName.equals("") || lastName.equals("") || email.equals("")) return 0;
		
		if(oldPassword != null) {
			if(oldPassword.equals("")) return 0;
			if(!oldAdmin.getPassword().equals(oldPassword)) return 2;
		}
		
		if(oldAdmin != null) {
			if(exists(username) && !oldAdmin.getUsername().equals(username)) return 1;
		}
		else {
			if(exists(username)) return 1;
		}
		
		if(!newPassword.equals(repeatNewPassword)) return 3;
		if(!EmailValidator.getInstance().isValid(email)) return 4;
		
		return 5;
	}

}
