package com.jvm.isa.controller;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jvm.isa.service.EmailService;
import com.jvm.isa.domain.Administrator;
import com.jvm.isa.domain.RegisteredUser;
import com.jvm.isa.domain.User;
import com.jvm.isa.domain.UserStatus;
import com.jvm.isa.domain.UserType;
import com.jvm.isa.service.AdminService;

@RestController
@RequestMapping(value = "/admins")

public class AdminController {

	@Autowired
	private AdminService adminService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private HttpSession httpSession;

	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	// NIJE MI RADILO BEZ ANOTACIJE @RequestBody ZA PARAMETAR METODE
	public ResponseEntity<Boolean> registerSysAdmin(@RequestBody HashMap<String, String> hm) {
		String username = hm.get("username");
		String password = hm.get("newPassword");
		String repeatPassword = hm.get("repeatNewPassword");
		String firstName = hm.get("firstName");
		String lastName = hm.get("lastName");
		String email = hm.get("email");
		String role = hm.get("role");
		System.out.println("|" + username + " - " + password + " - " + repeatPassword + " - " + firstName + " - "
				+ lastName + " - " + email + " - " + "|");

		int correct = adminService.validAdmin(null, username, null, password, repeatPassword, firstName, lastName,
				email);
		boolean successregister;

		if (correct == 5) {
			Administrator admin = new Administrator(username, password, firstName, lastName, email, UserType.SYS_ADMINISTRATOR,UserStatus.ACTIVATED);
			if (role.equals("CIA")) admin.setUserType(UserType.INSTITUTION_ADMINISTRATOR);
			else if (role.equals("FZA")) admin.setUserType(UserType.FUNZONE_ADMINISTRATOR);
			successregister = adminService.register(admin);

		} else
			successregister = false;

		return new ResponseEntity<Boolean>(successregister, HttpStatus.OK);

	}

	@RequestMapping(value = "/save_changes_on_profile", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> saveChangesOnProfile(@RequestBody HashMap<String, String> hm) {
		User user = (User) httpSession.getAttribute("loggedUser");

		if (user != null) {
			Administrator loggedUser = (Administrator) user;
			String username = hm.get("username");
			String oldPassword = hm.get("oldPassword");
			String newPassword = hm.get("newPassword");
			String repeatNewPassword = hm.get("repeatNewPassword");
			String firstName = hm.get("firstName");
			String lastName = hm.get("lastName");
			String email = hm.get("email");

			int correct = adminService.validAdmin(loggedUser, username, oldPassword, newPassword, repeatNewPassword,
					firstName, lastName, email);
			if (correct == 5) {
				loggedUser.setUsername(username);
				loggedUser.setPassword(newPassword);
				loggedUser.setFirstName(firstName);
				loggedUser.setLastName(lastName);
				loggedUser.setEmail(email);

				adminService.register(loggedUser); // cuvanje napravljenih
													// izmena
			}

			return new ResponseEntity<Integer>(correct, HttpStatus.OK);
		}

		return new ResponseEntity<Integer>(-1, HttpStatus.OK);
	}
	
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/logout", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity logout(String username) {
		httpSession.invalidate();
		System.out.println("Logout");
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}

