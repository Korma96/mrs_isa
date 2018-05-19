package com.jvm.isa.service;

import java.util.ArrayList;

import com.jvm.isa.domain.RegisteredUser;
import com.jvm.isa.domain.User;

public interface UserService {

	boolean registrate(User user);
	
	boolean exists(String username);
	
	User getUser(String username, String password);
	
	User getUser(String username);
	
	int correctUser(User oldUser, String username, String oldPassword, String newPassword, String repeatNewPassword, String firstName, String lastName, String email, String city, String phoneNumber);

	ArrayList<String> getPeople(RegisteredUser ru);

	int correctChangepassword(User user, String oldPassword, String newPassword, String repeatNewPassword);
	
}
