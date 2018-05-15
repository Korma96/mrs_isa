package com.jvm.isa.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

import com.jvm.isa.service.EmailService;
import com.jvm.isa.domain.Administrator;
import com.jvm.isa.domain.CulturalInstitution;
import com.jvm.isa.domain.RegisteredUser;
import com.jvm.isa.domain.Requisite;
import com.jvm.isa.domain.RequisiteDTO;
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
	@RequestMapping(value = "/get_cultural_institutions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArrayList<String>> getCulturalInstitutions() {
		ArrayList<String> culturalInstitutions = adminService.getCulturalInstitutions();
		
		return new ResponseEntity<ArrayList<String>>(culturalInstitutions, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/get_showings_of_cultural_institution/{culturalInstitutionName}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE,
																									produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArrayList<String>> getShowingsOfCulturalInstitution(@PathVariable("culturalInstitutionName") String culturalInstitutionName) {
		ArrayList<String> showings = adminService.getShowings(culturalInstitutionName);
		
		return new ResponseEntity<ArrayList<String>>(showings, HttpStatus.OK);
		
	}
	
	
	@RequestMapping(value = "/add_requisite", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
																			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> AddRequisite(@RequestBody HashMap<String, String> hm) {
		String name = hm.get("name");
		String description = hm.get("description");
		String priceStr = hm.get("price");
		String culturalInstitutionName = hm.get("culturalInstitution");
		String showing = hm.get("showing");
		
		boolean successAddRequisite;
		int correct = adminService.correctRequisite(name, description, priceStr, culturalInstitutionName, showing);
		if(correct == 6) {
			CulturalInstitution cInstitution = adminService.getCulturalInstitution(culturalInstitutionName);
			double price = Double.parseDouble(priceStr);
			successAddRequisite = adminService.addRequisite(new Requisite(name, description, price, cInstitution.getShowing(showing), cInstitution));
		}
		else successAddRequisite = false;
		
		return new ResponseEntity<Boolean>(successAddRequisite, HttpStatus.OK);
	
	}
	
	@RequestMapping(value = "/get_requisites", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArrayList<RequisiteDTO>> getRequisites() {
		List<Requisite> requisites = adminService.getRequisites();
		
		ArrayList<RequisiteDTO> requisiteDTOs = new ArrayList<RequisiteDTO>();
		
		for (Requisite requisite : requisites) {
			requisiteDTOs.add(new RequisiteDTO(requisite));
		}
		
		return new ResponseEntity<ArrayList<RequisiteDTO>>(requisiteDTOs, HttpStatus.OK);
	
	}
	
		
}


