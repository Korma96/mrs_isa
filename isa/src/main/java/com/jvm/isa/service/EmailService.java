package com.jvm.isa.service;

import javax.mail.MessagingException;

import com.jvm.isa.domain.RegisteredUser;

public interface EmailService {
	
	String generateIdForActivation();
	
	void sendEmailAsync(RegisteredUser user) throws MessagingException;
	
	void sendEmailSync(RegisteredUser user) throws MessagingException;
	
	boolean activateAccount(String idForActivation);
}
