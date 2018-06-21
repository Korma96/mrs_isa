package com.jvm.isa.service;

import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jvm.isa.domain.Activation;
import com.jvm.isa.domain.RegisteredUser;
import com.jvm.isa.domain.UserStatus;
import com.jvm.isa.repository.ActivationRepository;


@Service
public class EmailServiceImpl implements EmailService {

	private String emailOfSender = "mrs.isa.jvm@gmail.com";
	private String passwordOfSender = "mrs**isa";
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private ActivationRepository activationRepository;

	/*
	 * Koriscenje klase za ocitavanje vrednosti iz application.properties fajla
	 */
	@Autowired
	private Environment env;
	
	private char[] characters = { 'A', 'B', '0', 'C', 'D', '1', 'E', 'F', '2', 'G', 'H', 'I', '3', 'J', '4', 'K', 'L', '5', 'M', 'N', 'O', '6', 'P', 'Q', 'R', 'S', '7', 'T', 'U', '8', 'W', 'X', 'Y', '9', 'Z' };
	
	@Override
	public String generateIdForActivation() {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		int index;
		
		for (int i = 0; i < LENGTH_OF_ID_FOR_ACTIVATION; i++) {
			index = random.nextInt(characters.length);
			sb.append(characters[index]);
		}
		
		return sb.toString(); 
	}
	

	/*
	 * Anotacija za oznacavanje asinhronog zadatka
	 * Vise informacija na: https://docs.spring.io/spring/docs/current/spring-framework-reference/integration.html#scheduling
	 */
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	@Async
	@Override
	public void sendActivationEmailAsync(RegisteredUser user) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		
		String idForActivation;
		Activation activation;
		do {
			idForActivation = generateIdForActivation();
			activation = activationRepository.findByIdForActivation(idForActivation);
		}
		while(activation != null);
		
		String htmlMsg = "Dear " + user.getFirstName() + ", <br/><br/>";
				htmlMsg += "<h3> Welcome to ISA </h3> <br/><br/>";
				htmlMsg += "Below are your login and activation details for your new account with ISA: <br/><br/>";
				htmlMsg += "&nbsp; Email: &nbsp; <b> "+ user.getEmail() +" </b> <br/>";
				htmlMsg += "&nbsp; Username: &nbsp; <b> " + user.getUsername() + " </b> <br/>";
				htmlMsg += "&nbsp; Password: &nbsp; <b> " + user.getPassword() + " </b> <br/><br/>";
				htmlMsg += "To activate your account, please click on the following link (if the link is disabled Copy and Paste the URL into your Browser): <br/>";
				htmlMsg += "<a href='http://localhost:8080/myapp/#/users/activate?id_for_activation=" + idForActivation + "'> http://localhost:8080/myapp/#/users/activate?id_for_activation=" + idForActivation + " </a> <br/><br/>";
				htmlMsg += "Kind Regards, <br/>";
				htmlMsg += "ISA Support";
		mimeMessage.setContent(htmlMsg, "text/html");
		
		
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
		helper.setTo(user.getEmail());
		helper.setSubject("ISA Activation Account Support");
		helper.setFrom(env.getProperty(emailOfSender, passwordOfSender));
		javaMailSender.send(mimeMessage);
		
		activation = new Activation(idForActivation, user);
		activationRepository.save(activation); // cuvanje u bazi

		System.out.println("Email poslat!");
	}
	
	/*
	 * Anotacija za oznacavanje asinhronog zadatka
	 * Vise informacija na: https://docs.spring.io/spring/docs/current/spring-framework-reference/integration.html#scheduling
	 */
	@Async
	@Override
	public void sendNewAdminEmailAsync(String username, String password, String email, String typeOfAdmin) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		
		String htmlMsg = "You are registered as a " + typeOfAdmin + " <br/><br/>";
				htmlMsg += "Below are your login details for your new account with ISA: <br/><br/>";
				htmlMsg += "&nbsp; Email: &nbsp; <b> "+ email +" </b> <br/>";
				htmlMsg += "&nbsp; Username: &nbsp; <b> " + username + " </b> <br/>";
				htmlMsg += "&nbsp; Password: &nbsp; <b> " + password + " </b> <br/><br/>";
				htmlMsg += "After logging in, please change your username and password <br/><br/>";
				htmlMsg += "<a href='http://localhost:8080/myapp/#/users/login'> http://localhost:8080/myapp/#/users/login </a> <br/><br/>";
				htmlMsg += "Kind Regards, <br/>";
				htmlMsg += "ISA Support";
		mimeMessage.setContent(htmlMsg, "text/html");
		
		
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
		helper.setTo(email);
		helper.setSubject("ISA Support");
		helper.setFrom(env.getProperty(emailOfSender, passwordOfSender));
		javaMailSender.send(mimeMessage);

		System.out.println("Email poslat!");
	}
	
	@Override
	public void sendUserChangedEmail(String username, String password, String email) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		
		String htmlMsg = "You have changed your email address to your account. <br/><br/>";
				htmlMsg += "Below are your login details for your account with ISA: <br/><br/>";
				htmlMsg += "&nbsp; Email: &nbsp; <b> "+ email +" </b> <br/>";
				htmlMsg += "&nbsp; Username: &nbsp; <b> " + username + " </b> <br/>";
				htmlMsg += "&nbsp; Password: &nbsp; <b> " + password + " </b> <br/><br/>";
				htmlMsg += "Kind Regards, <br/>";
				htmlMsg += "ISA Support";
		mimeMessage.setContent(htmlMsg, "text/html");
		
		
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
		helper.setTo(email);
		helper.setSubject("ISA Support");
		helper.setFrom(env.getProperty(emailOfSender, passwordOfSender));
		javaMailSender.send(mimeMessage);

		System.out.println("Email poslat!");
		
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	@Override
	public boolean activateAccount(String idForActivation) {
		Activation activation = activationRepository.findByIdForActivation(idForActivation);
		if (activation != null) { 
			activation.getUser().setUserStatus(UserStatus.ACTIVATED);;
			activationRepository.delete(activation);
			return true;
		}
		
		return false;
	}
	
	@Async
	@Override
	public void sendInviteForShowing(String culturalInstitutionName, String showingName, String dateStr, String timeStr, String auditoriumName,
			String seat, double price, int duration, RegisteredUser loggedRegisteredUser, RegisteredUser friend) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		
		String htmlMsg = "Dear " + friend.getFirstName() + ", <br/><br/>";
				htmlMsg += "Your ISA friend " +loggedRegisteredUser.getFirstName() + " " + loggedRegisteredUser.getLastName() + ", invites you to a showing with the following information: <br/><br/>";
				htmlMsg += "&nbsp; Cultural institution: &nbsp; <b> "+ culturalInstitutionName +" </b> <br/>";
				htmlMsg += "&nbsp; Showing: &nbsp; <b> " + showingName + " </b> <br/>";
				htmlMsg += "&nbsp; Date: &nbsp; <b> " + dateStr + " </b> <br/>";
				htmlMsg += "&nbsp; Time: &nbsp; <b> " + timeStr + " </b> <br/>";
				htmlMsg += "&nbsp; Auditorium: &nbsp; <b> " + auditoriumName + " </b> <br/>";
				htmlMsg += "&nbsp; Your seat: &nbsp; <b> " + seat + " </b> <br/>";
				htmlMsg += "&nbsp; Duration: &nbsp; <b> " + duration + " min </b> <br/>";
				htmlMsg += "&nbsp; Price: &nbsp; <b> " + price + " </b> <br/><br/>";
				htmlMsg += "<a href='http://localhost:8080/myapp/#/users/invitations_and_tickets'> Go to invitations and tickets page </a> <br/><br/>";
				htmlMsg += "Kind Regards, <br/>";
				htmlMsg += "ISA Support";
		mimeMessage.setContent(htmlMsg, "text/html");
		
		
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
		helper.setTo(friend.getEmail());
		helper.setSubject("ISA Support");
		helper.setFrom(env.getProperty(emailOfSender, passwordOfSender));
		javaMailSender.send(mimeMessage);
		
		System.out.println("Email poslat!");
	}

}
