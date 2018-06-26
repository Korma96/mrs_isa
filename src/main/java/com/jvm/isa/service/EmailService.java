package com.jvm.isa.service;

import javax.mail.MessagingException;

import com.jvm.isa.domain.RegisteredUser;

public interface EmailService {
	final int LENGTH_OF_ID_FOR_ACTIVATION = 20;
	
	String generateIdForActivation();
	
	void sendActivationEmailAsync(RegisteredUser user) throws MessagingException;
	//void sendActivationEmailAsyncSparkPost(RegisteredUser user) throws SparkPostException;
	
	void sendNewAdminEmailAsync(String username, String password, String email, String typeOfAdmin) throws MessagingException;
	//void sendNewAdminEmailAsyncSparkPost(String username, String password, String email, String typeOfAdmin) throws SparkPostException;
	
	boolean activateAccount(String idForActivation);

	void sendUserChangedEmailAsync(String username, String password, String email) throws MessagingException;
	//void sendUserChangedEmailAsyncSparkPost(String username, String password, String email) throws SparkPostException;
	
	void sendInviteForShowingAsync(String culturalInstitutionName, String showingName, String dateStr, String timeStr, String auditoriumName,
			String seat, double price, int duration, RegisteredUser loggedRegisteredUser, RegisteredUser friend) throws MessagingException;
	//void sendInviteForShowingAsyncSparkPost(String culturalInstitutionName, String showingName, String dateStr, String timeStr, String auditoriumName,
	//		String seat, double price, int duration, RegisteredUser loggedRegisteredUser, RegisteredUser friend) throws SparkPostException;
}
