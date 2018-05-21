package com.jvm.isa.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jvm.isa.domain.Administrator;
import com.jvm.isa.domain.CulturalInstitution;
import com.jvm.isa.domain.CulturalInstitutionType;
import com.jvm.isa.domain.Requisite;
import com.jvm.isa.domain.RequisiteDTO;
import com.jvm.isa.domain.SysAdministrator;
import com.jvm.isa.domain.User;
import com.jvm.isa.domain.UserStatus;
import com.jvm.isa.domain.UserType;
import com.jvm.isa.service.AdminService;
import com.jvm.isa.service.CulturalInstitutionService;
import com.jvm.isa.service.EmailService;

@RestController
@RequestMapping(value = "/administrators")

public class AdminController {

	private final String ADMIN_CONST = "admin";
	private final String ADMIN_PASSWORD = "admin_#password$";
	private final String[] ADMIN_TYPES = {"fun", "cul", "sys"};
	
	private Random random = new Random();
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private CulturalInstitutionService culturalInstitutionService;
	
	@Autowired
	private UserController userController;
	
	@RequestMapping(value = "/sys_admin/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	// NIJE MI RADILO BEZ ANOTACIJE @RequestBody ZA PARAMETAR METODE
	public ResponseEntity<Boolean> registerSysAdmin(@RequestBody HashMap<String, String> hm) {
		String adminRole = hm.get("adminRole");
		String email = hm.get("email");
		
		String username = ADMIN_CONST + "_";
		String password = ADMIN_PASSWORD + "_" + random.nextInt(10000);
		String firstName = null;
		String lastName = null;
		
		if(adminRole.equals("SA")) {
			username += ADMIN_TYPES[2];
			
		}
		else {
			if(adminRole.equals("CIA")) {
				username += ADMIN_TYPES[1];
			}
			else if(adminRole.equals("FZA")) {
				username += ADMIN_TYPES[0];
			}
			
			firstName = hm.get("firstName");
			lastName = hm.get("lastName");
		}
		
		
		String potentionalUsername;
		do {
			potentionalUsername = username + "_" + random.nextInt(10000);
		}
		while(adminService.exists(potentionalUsername));
		
		boolean successregister = false;
		String typeOfAdmin = null;
		
		if(firstName != null && lastName != null) { 
			if(!firstName.equals("") && !lastName.equals("")) {
				UserType userType = null;
				if(adminRole.equals("CIA")) {
					typeOfAdmin = "cultural institution administrator";
					userType = UserType.INSTITUTION_ADMINISTRATOR;
				}
				else if(adminRole.equals("FZA")) {
					typeOfAdmin = "fun zone administrator";
					userType = UserType.FUNZONE_ADMINISTRATOR;
				}
				
				Administrator admin = new Administrator(potentionalUsername, password, firstName, lastName, email, userType);
				successregister = adminService.register(admin);
				
				if(successregister) {
					try {
						emailService.sendNewAdminEmailAsync(potentionalUsername, password, email, typeOfAdmin);
					} catch (MessagingException e) {
						System.out.println("Greska prilikom slanja emaila! - " + e.getMessage());
					}
				}
			}
		}
		else {
			typeOfAdmin = "system administrator";
			SysAdministrator sysAdministrator = new SysAdministrator(potentionalUsername, password, email);
			successregister = adminService.register(sysAdministrator);
			
			if(successregister) {
				try {
					emailService.sendNewAdminEmailAsync(potentionalUsername, password, email, typeOfAdmin);
				} catch (MessagingException e) {
					System.out.println("Greska prilikom slanja emaila! - " + e.getMessage());
				}
			}
		}
		

		return new ResponseEntity<Boolean>(successregister, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/sys_admin/update_profile", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> saveChangesOnProfileSystemAdmin(@RequestBody HashMap<String, String> hm) {
		User user = userController.getLoggedUserLocalMethod();
		
		if (user != null) {
			String username = hm.get("username");
			String oldPassword = hm.get("oldPassword");
			String newPassword = hm.get("newPassword");
			String repeatNewPassword = hm.get("repeatNewPassword");
			String email = hm.get("email");

			int correct = adminService.validSystemAdmin(user, username, oldPassword, newPassword, repeatNewPassword, email);
			if (correct == 0) {
				SysAdministrator sysAdministrator = (SysAdministrator) user;
				sysAdministrator.setUsername(username);
				userController.setLogged(username);
				if(!newPassword.equals("")) sysAdministrator.setPassword(newPassword);
				
				if (!email.equals(sysAdministrator.getEmail())) {
					sysAdministrator.setEmail(email);
					try {
						emailService.sendUserChangedEmail(sysAdministrator.getUsername(), sysAdministrator.getPassword(), sysAdministrator.getEmail());
					} catch (MessagingException e) {
						System.out.println("Greska prilikom slanja emaila! - " + e.getMessage());
					}
				}

				adminService.register(user); // cuvanje napravljenih
													// izmena
			}

			return new ResponseEntity<Integer>(correct, HttpStatus.OK);
		}

		return new ResponseEntity<Integer>(-1, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/admin_funzone/update_profile", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> saveChangesOnProfileAdminFunZone(@RequestBody HashMap<String, String> hm) {
		return saveChangesOnProfileAdmin(hm);
	}
	
	@RequestMapping(value = "/admin_cultural_institution/update_profile", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> saveChangesOnProfileAdminCulturalInstitution(@RequestBody HashMap<String, String> hm) {
		return saveChangesOnProfileAdmin(hm);
	}
	
	public ResponseEntity<Integer> saveChangesOnProfileAdmin(HashMap<String, String> hm) {
		User user = userController.getLoggedUserLocalMethod();
		
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
			if (correct == 0) {
				loggedUser.setUsername(username);
				userController.setLogged(username);
				if(!repeatNewPassword.equals("")) loggedUser.setPassword(repeatNewPassword);
				loggedUser.setFirstName(firstName);
				loggedUser.setLastName(lastName);
				if (!email.equals(loggedUser.getEmail())) {
					loggedUser.setEmail(email);
					try {
						emailService.sendUserChangedEmail(loggedUser.getUsername(), loggedUser.getPassword(), loggedUser.getEmail());
					} catch (MessagingException e) {
						System.out.println("Greska prilikom slanja emaila! - " + e.getMessage());
					}
				}

				adminService.register(loggedUser); // cuvanje napravljenih
													// izmena
			}

			return new ResponseEntity<Integer>(correct, HttpStatus.OK);
		}

		return new ResponseEntity<Integer>(-1, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/admin_funzone/save_changed_password", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, 
																					produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> saveAdminFunZoneChangedPassword(@RequestBody HashMap<String, String> hm) {
		return userController.saveChangedPassword(hm);
	}
	
	@RequestMapping(value = "/admin_cultural_institution/save_changed_password", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> saveAdminCulturalInstitutionChangedPassword(@RequestBody HashMap<String, String> hm) {
		return userController.saveChangedPassword(hm);
	}
	
	@RequestMapping(value = "/admin_cultural_institution/add_new_cultural_institution", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> saveCulturalInstitution(@RequestBody HashMap<String, String> hm)
	{
		String name = hm.get("name");
		String address = hm.get("address");
		String description = hm.get("description");
		String role = hm.get("role");
		CulturalInstitutionType ciType = CulturalInstitutionType.valueOf(role);
		CulturalInstitution ci = new CulturalInstitution(name, address, description, ciType);
		if(!culturalInstitutionService.exists(ci.getName()))
		{
			culturalInstitutionService.save(ci);
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}
		return new ResponseEntity<Boolean>(false, HttpStatus.OK);
	}	
	
	@RequestMapping(value = "/sys_admin/save_changed_password", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> saveSysAdminChangedPassword(@RequestBody HashMap<String, String> hm) {
		return userController.saveChangedPassword(hm);
	}
	
	@RequestMapping(value = "/admin_funzone/get_cultural_institutions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArrayList<String>> getCulturalInstitutions() {
		ArrayList<String> culturalInstitutions = adminService.getCulturalInstitutions();
		
		return new ResponseEntity<ArrayList<String>>(culturalInstitutions, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/admin_funzone/get_showings_of_cultural_institution/{culturalInstitutionName}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE,
																									produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArrayList<String>> getShowingsOfCulturalInstitution(@PathVariable("culturalInstitutionName") String culturalInstitutionName) {
		ArrayList<String> showings = adminService.getShowings(culturalInstitutionName);
		
		return new ResponseEntity<ArrayList<String>>(showings, HttpStatus.OK);
		
	}
	
	
	@RequestMapping(value = "/admin_funzone/add_requisite", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
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
	
	@RequestMapping(value = "/admin_funzone/get_requisites", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArrayList<RequisiteDTO>> getRequisites() {
		List<Requisite> requisites = adminService.getRequisites();
		
		ArrayList<RequisiteDTO> requisiteDTOs = new ArrayList<RequisiteDTO>();
		
		for (Requisite requisite : requisites) {
			requisiteDTOs.add(new RequisiteDTO(requisite));
		}
		
		return new ResponseEntity<ArrayList<RequisiteDTO>>(requisiteDTOs, HttpStatus.OK);
	
	}
	
	@RequestMapping(value = "/admin_funzone/changes_default_username_password", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, 
																						produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> saveAdminFunZoneChangedUsernameAndPassword(@RequestBody HashMap<String, String> hm) {
		return saveChangedUsernameAndPassword(hm);
	}	
	
	@RequestMapping(value = "/admin_cultural_institution/changes_default_username_password", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, 
																											produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> saveCulturalInstitutionAdminChangedUsernameAndPassword(@RequestBody HashMap<String, String> hm) {
		return saveChangedUsernameAndPassword(hm);
	}	
	
	@RequestMapping(value = "/sys_admin/changes_default_username_password", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> saveSysAdminChangedUsernameAndPassword(@RequestBody HashMap<String, String> hm) {
		return saveChangedUsernameAndPassword(hm);
	}
	
	public ResponseEntity<Boolean> saveChangedUsernameAndPassword(HashMap<String, String> hm) {
		User user = userController.getLoggedUserLocalMethod();
		
		if(user != null) {
			String username = hm.get("username");
			String password = hm.get("password");
			String repeatPassword = hm.get("repeatPassword");
			
			int correct = adminService.validChangedUsernameAndPassword(user, username, user.getPassword(), password, repeatPassword);
			if (correct == 0) {
				if(!repeatPassword.equals("")) {
					user.setUsername(username);
					userController.setLogged(username);
					
					user.setPassword(repeatPassword);
					user.setUserStatus(UserStatus.ACTIVATED);
					
					adminService.register(user);
					
					return new ResponseEntity<Boolean>(true, HttpStatus.OK);
				}
			}
			
		}

		return new ResponseEntity<Boolean>(false, HttpStatus.OK);
	}
}


