package com.jvm.isa.service;

import javax.mail.MessagingException;

import com.jvm.isa.domain.RegisteredUser;

public interface EmailService {
	final int LENGTH_OF_ID_FOR_ACTIVATION = 20;
	
	String generateIdForActivation();
	
	void sendEmailAsync(RegisteredUser user) throws MessagingException;
	
	boolean activateAccount(String idForActivation);
}
