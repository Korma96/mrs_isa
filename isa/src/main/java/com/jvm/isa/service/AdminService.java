package com.jvm.isa.service;

import com.jvm.isa.domain.Administrator;

public interface AdminService {
	
	boolean register(Administrator admin);
	
	boolean exists(String username);
	
	Administrator getUser(String username, String password);
	
	int validAdmin(Administrator oldAdmin, String username, String oldPassword, String newPassword, String repeatNewPassword, String firstName, String lastName, String email);
	

}
