package com.jvm.isa.service;

import com.jvm.isa.domain.User;

public interface UserService {

	boolean registrate(User user);
	
	boolean exists(String username);
	
	User getUser(String username, String password);
	
	int correctUser(User oldUser, String username, String oldPassword, String newPassword, String repeatNewPassword, String firstName, String lastName, String email, String city, String phoneNumber);
	
}
