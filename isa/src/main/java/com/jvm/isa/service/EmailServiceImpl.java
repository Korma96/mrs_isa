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

import com.jvm.isa.domain.Activation;
import com.jvm.isa.domain.RegisteredUser;
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
	private final int LENGTH_OF_ID_FOR_ACTIVATION = 20;
	
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
	@Async
	@Override
	public void sendEmailAsync(RegisteredUser user) throws MessagingException {
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
				htmlMsg += "&nbsp; Email: &nbsp; <b> "+ user.getEmail() +" <b> <br/>";
				htmlMsg += "&nbsp; Username: &nbsp; <b> " + user.getUsername() + " <b> <br/>";
				htmlMsg += "&nbsp; Password: &nbsp; <b> " + user.getPassword() + " <b> <br/><br/>";
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

	@Override
	public void sendEmailSync(RegisteredUser user) throws MessagingException {

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
				htmlMsg += "&nbsp; Email: &nbsp; <b> "+ user.getEmail() +" <b> <br/>";
				htmlMsg += "&nbsp; Username: &nbsp; <b> " + user.getUsername() + " <b> <br/>";
				htmlMsg += "&nbsp; Password: &nbsp; <b> " + user.getPassword() + " <b> <br/><br/>";
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
	
	@Override
	public boolean activateAccount(String idForActivation) {
		Activation activation = activationRepository.findByIdForActivation(idForActivation);
		if (activation != null) { 
			activation.getUser().setActivated(true);
			activationRepository.delete(activation);
			return true;
		}
		
		return false;
	}

}
