package com.jvm.isa.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jvm.isa.domain.RegisteredUser;
import com.jvm.isa.domain.Administrator;
import com.jvm.isa.domain.User;
import com.jvm.isa.domain.UserStatus;
import com.jvm.isa.domain.UserType;
import com.jvm.isa.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
    private UserRepository userRepository;
	
	@Override
	public boolean registrate(User user) {
		try {
			userRepository.save(user);
		}
		catch(Exception e) {
			return false;
		}
		
		return true;
	}

	@Override
	public boolean exists(String username) {
		return userRepository.findByUsername(username) != null;
	}

	@Override
	public User getUser(String username) {
		User user = userRepository.findByUsernameAndUserStatus(username, UserStatus.ACTIVATED);
		if(user.getUserType() != UserType.REGISTERED_USER)
		{
			RegisteredUser ru = (RegisteredUser)user;
			Administrator admin = new Administrator(username, ru.getPassword(), ru.getFirstName(), ru.getLastName(), ru.getEmail(), ru.getUserType(), ru.getUserStatus());
			return admin;
		}
		return user;
	}
	
	@Override
	public User getUserWithoutProxy(String username) {
		// doesnt check if user is activated
		User user = userRepository.findByUsername(username);
		if(user.getUserStatus() == UserStatus.DEACTIVATED)
			return null;
		if(user.getUserType() != UserType.REGISTERED_USER)
		{
			RegisteredUser ru = (RegisteredUser)user;
			Administrator admin = new Administrator(username, ru.getPassword(), ru.getFirstName(), ru.getLastName(), ru.getEmail(), ru.getUserType(), ru.getUserStatus());
			return admin;
		}
		return user;
	}
	
	@Override
	public User getUser(String username, String password) {
		// iz nekog razloga kad vrati usera uvek ga kastuje u RegisteredUsera
		User user = userRepository.findByUsernameAndPassword(username, password);
		if(user.getUserType() != UserType.REGISTERED_USER)
		{
			RegisteredUser ru = (RegisteredUser)user;
			Administrator admin = new Administrator(username, password, ru.getFirstName(), ru.getLastName(), ru.getEmail(), ru.getUserType(), ru.getUserStatus());
			return admin;
		}
		
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
	 *  6 - Everything is right
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
	
	@Override
	public ArrayList<String> getPeople(RegisteredUser ru) {
		ArrayList<String> people = new ArrayList<String>();
	
		List<User> registeredUsers = userRepository.findByUserTypeAndUserStatus(UserType.REGISTERED_USER, UserStatus.ACTIVATED);
		
		for (User user : registeredUsers) {
			if (user.equals(ru)) continue;
			
			people.add(((RegisteredUser) user).toString());
		}
		
		return people;
	}
	
}
