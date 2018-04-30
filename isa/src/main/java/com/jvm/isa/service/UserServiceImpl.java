package com.jvm.isa.service;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jvm.isa.domain.User;
import com.jvm.isa.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
    private UserRepository userRepository;
	
	@Override
	public boolean registrate(User user) {
		userRepository.save(user);
		
		return true;
	}

	@Override
	public boolean exists(String username) {
		return userRepository.findByUsername(username) != null;
	}

	@Override
	public User getUser(String username, String password) {
		User user = userRepository.findByUsernameAndPassword(username, password);
		
		return user;
	}
	
	
	/*
	 * return value:
	 *  0 - All fields are not filled
	 *  1 - This username already exists
	 *  2 - You did not enter the correct old password
	 *  3 - You are not the first to enter the same new password the second time
	 *  4 - Incorrect email
	 *  5 - Incorrect phone number
	 *  6 - Everything is right...
	 * */
	@Override
	public int correctUser(User oldUser, String username, String oldPassword, String newPassword, String repeatNewPassword, String firstName, String lastName, String email, String city, String phoneNumber) {
		if(username.equals("") || newPassword.equals("") || repeatNewPassword.equals("") || firstName.equals("") || lastName.equals("") || email.equals("") || city.equals("") || phoneNumber.equals("")) return 0;
		
		if(oldPassword != null) {
			if(oldPassword.equals("")) return 0;
			if(!oldUser.getPassword().equals(oldPassword)) return 2;
		}
		
		if(oldUser != null) {
			if(exists(username) && !oldUser.getUsername().equals(username)) return 1;
		}
		else {
			if(exists(username)) return 1;
		}
		
		if(!newPassword.equals(repeatNewPassword)) return 3;
		if(!EmailValidator.getInstance().isValid(email)) return 4;
		
		try { Integer.parseInt(phoneNumber); }
		catch(Exception e) { return 5; }
		
		return 6;
		
	}
	
	/*@Override
	public boolean logout(String username) {
		User user = userRepository.findByUsername(username);
		
		if(user == null) return false;
		
		return true;
	}*/

	
}
