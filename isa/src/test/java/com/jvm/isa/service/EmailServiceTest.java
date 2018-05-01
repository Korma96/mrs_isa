package com.jvm.isa.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jvm.isa.domain.RegisteredUser;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailServiceTest {

	@Autowired
	private EmailService emailService;
	
	@SuppressWarnings("static-access")
	@Test
	public void testGenerateIdForActivation() {
		String IdForActivation = emailService.generateIdForActivation();
		int len = IdForActivation.length();
		
		assertEquals(len, emailService.LENGTH_OF_ID_FOR_ACTIVATION);
	}
	
	//TODO ovo ne radi, pitaj asistenta
	/*@Test(expected = MessagingException.class)
	public void testFailSendEmailAsync() throws MessagingException {

		emailService.sendEmailAsync(new RegisteredUser("", "", "Pera", "Peric", "sunjkajovo@", "", ""));
		
	}*/
	
	@Test
	public void testSuccessSendEmailAsync() throws Exception {
			emailService.sendEmailAsync(new RegisteredUser("", "", "Pera", "Peric", "sunjkajov.com", "", ""));
	}
}
