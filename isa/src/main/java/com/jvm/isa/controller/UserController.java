package com.jvm.isa.controller;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jvm.isa.domain.RegisteredUser;
import com.jvm.isa.domain.User;
import com.jvm.isa.domain.UserType;
import com.jvm.isa.service.EmailService;
import com.jvm.isa.service.UserService;


@RestController
@RequestMapping(value = "/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private HttpSession httpSession;
	
	@RequestMapping(value = "/logged_user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> getLoggedUser() {
		User user = (User) httpSession.getAttribute("loggedUser");
		if(user == null) user = new User("-1", "-1", UserType.REGISTERED_USER, false); // iz nekog razloga na frontendu ne mogu da primim null

		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/registrate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, 
																		produces = MediaType.APPLICATION_JSON_VALUE)
	// NIJE MI RADILO BEZ ANOTACIJE @RequestBody ZA PARAMETAR METODE
	public ResponseEntity<Boolean> registrate(@RequestBody HashMap<String, String> hm) { 
		String username = hm.get("username");
		String password = hm.get("password");
		String repeatPassword = hm.get("repeatPassword");
		String firstName = hm.get("firstName");
		String lastName = hm.get("lastName");
		String email = hm.get("email");
		String city = hm.get("city");
		String phoneNumber = hm.get("phoneNumber");

		//System.out.println("|" + username + " - " + password + " - " + repeatPassword + " - " + firstName + " - " + lastName + " - " + email + " - " + city + " - " + phoneNumber + "|");
		
		int correct = userService.correctUser(null, username, null, password, repeatPassword, firstName, lastName, email, city, phoneNumber);
		boolean successRegistrate;
		
		if(correct == 6)	{
			RegisteredUser user = new RegisteredUser(username, password, firstName, lastName, email, city, phoneNumber);
			successRegistrate = userService.registrate(user);
			
			try {
				emailService.sendEmailAsync(user);
			}
			catch(Exception e)	{
				System.out.println("Greska prilikom slanja emaila! - " + e.getMessage());
			}
			
		}
		else successRegistrate = false;
		
		return new ResponseEntity<Boolean>(successRegistrate, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, 
																			produces = MediaType.APPLICATION_JSON_VALUE)
	// NIJE MI RADILO BEZ ANOTACIJE @RequestBody ZA PARAMETAR METODE
	public ResponseEntity<User> login(@RequestBody HashMap<String, String> hm) {
		
		System.out.println("|" + hm.get("username") + " - " + hm.get("password") + "|");
		User user = userService.getUser(hm.get("username"), hm.get("password"));
		System.out.println("user: " + user);
		if(user == null) user = new User("-1", "-1", UserType.REGISTERED_USER, false); // iz nekog razloga na frontendu ne mogu da primim null
		else {
			
			if(!user.isActivated()) {
				// posto user nije aktiviran
				user = new User("-1", "-1", UserType.REGISTERED_USER, false); // iz nekog razloga na frontendu ne mogu da primim null
			}
			else {
				httpSession.setAttribute("loggedUser", user); // cuvamo ulogovanog korisnika na sesiji
			}
			
		}
		
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/activate/{id_for_activation}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> activateAccount(@PathVariable("id_for_activation") String idForActivation) {
		boolean successActivate = emailService.activateAccount(idForActivation);
			
		return new ResponseEntity<Boolean>(successActivate, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/save_changes_on_profile", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, 
																					produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> saveChangesOnProfile(@RequestBody HashMap<String, String> hm) {
		User user = (User) httpSession.getAttribute("loggedUser");
		
		if(user != null) {
			RegisteredUser loggedUser = (RegisteredUser) user;
			
			String username = hm.get("username");
			String oldPassword = hm.get("oldPassword");
			String newPassword = hm.get("newPassword");
			String repeatNewPassword = hm.get("repeatNewPassword");
			String firstName = hm.get("firstName");
			String lastName = hm.get("lastName");
			String email = hm.get("email");
			String city = hm.get("city");
			String phoneNumber = hm.get("phoneNumber");
			
			int correct = userService.correctUser(loggedUser, username, oldPassword, newPassword, repeatNewPassword, firstName, lastName, email, city, phoneNumber);
			if(correct == 6) {
				loggedUser.setUsername(username);
				loggedUser.setPassword(newPassword);
				loggedUser.setFirstName(firstName);
				loggedUser.setLastName(lastName);
				loggedUser.setEmail(email);
				loggedUser.setCity(city);
				loggedUser.setPhoneNumber(phoneNumber);
				
				userService.registrate(loggedUser); // cuvanje napravljenih izmena
			}
			
			return new ResponseEntity<Integer>(correct, HttpStatus.OK);
		}
		
		return new ResponseEntity<Integer>(-1, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> logout(String username) {
		httpSession.invalidate(); // brisemo sve sa sesije
		System.out.println("Logout");
		boolean successLogout = true;
		return new ResponseEntity<Boolean>(successLogout, HttpStatus.OK);
	}
}
