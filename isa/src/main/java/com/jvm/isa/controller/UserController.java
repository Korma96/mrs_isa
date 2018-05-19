package com.jvm.isa.controller;

import java.util.ArrayList;
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

import com.jvm.isa.domain.Administrator;
import com.jvm.isa.domain.AdministratorDTO;
import com.jvm.isa.domain.RegisteredUser;
import com.jvm.isa.domain.RegisteredUserDTO;
import com.jvm.isa.domain.SysAdministrator;
import com.jvm.isa.domain.SysAdministratorDTO;
import com.jvm.isa.domain.User;
import com.jvm.isa.domain.UserDTO;
import com.jvm.isa.domain.UserStatus;
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
	
	public User getLoggedUserLocalMethod() {
		String username = (String) httpSession.getAttribute("loggedUsername");
		User user = null;
		
		if(username != null) {
			//user = userService.getUserWithoutProxy(username);
			user = userService.getUser(username);
		}
		
		return user;
	}
	
	public void setLogged(String username) {
		String oldUsername = (String) httpSession.getAttribute("loggedUsername");
		if(oldUsername != null) {
			if(oldUsername.equals(username)) return;
		}
		
		httpSession.setAttribute("loggedUsername", username); // cuvamo username ulogovanog korisnika na sesiji
	}
	
	@RequestMapping(value = "/is_logged", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> isLogged() {
		String username = (String) httpSession.getAttribute("loggedUsername");
		return new ResponseEntity<Boolean>(username != null, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/logged_user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDTO> getLoggedUser() {
		UserDTO userDTO= null;
		
		User user = getLoggedUserLocalMethod();
		
		if(user != null) {
			switch (user.getUserType()) {
			case REGISTERED_USER:
				RegisteredUserDTO registeredUserDTO = new RegisteredUserDTO((RegisteredUser) user);
				return new ResponseEntity<UserDTO>(registeredUserDTO, HttpStatus.OK);
			case FUNZONE_ADMINISTRATOR:
			case INSTITUTION_ADMINISTRATOR:
				AdministratorDTO administratorDTO = new AdministratorDTO((Administrator) user);
				return new ResponseEntity<UserDTO>(administratorDTO, HttpStatus.OK);
			case SYS_ADMINISTRATOR:
				SysAdministratorDTO sysAdministratorDTO = new SysAdministratorDTO((SysAdministrator) user);
				return new ResponseEntity<UserDTO>(sysAdministratorDTO, HttpStatus.OK);

			default:
				userDTO = new UserDTO(user);
				return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
			}
		}
	
		return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
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
			user.setUserType(UserType.REGISTERED_USER);
			user.setUserStatus(UserStatus.PENDING);
			successRegistrate = userService.registrate(user);
			
			try {
				emailService.sendActivationEmailAsync(user);
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
	public ResponseEntity<Boolean> login(@RequestBody HashMap<String, String> hm) {
		System.out.println("|" + hm.get("username") + " - " + hm.get("password") + "|");
		User user = userService.getUser(hm.get("username"), hm.get("password"));
		System.out.println("user: " + user);
		if(user != null) {
			if(user.getUserType() == UserType.REGISTERED_USER)
			{
				if(user.getUserStatus() == UserStatus.ACTIVATED) {
					setLogged(user.getUsername()); // cuvamo username ulogovanog korisnika na sesiji
					
					return new ResponseEntity<Boolean>(true, HttpStatus.OK);
				}
			}
			else
			{
				// kada je admin PENDING to znaci da jos nije promenuo default lozinku
				if(user.getUserStatus() != UserStatus.DEACTIVATED)
				{
					httpSession.setAttribute("loggedUsername", user.getUsername()); // cuvamo username ulogovanog korisnika na sesiji
					
					return new ResponseEntity<Boolean>(true, HttpStatus.OK);
				}
			}
			
		}
		
		return new ResponseEntity<Boolean>(false, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/activate/{id_for_activation}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> activateAccount(@PathVariable("id_for_activation") String idForActivation) {
		boolean successActivate = emailService.activateAccount(idForActivation);
			
		return new ResponseEntity<Boolean>(successActivate, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/save_changes_on_profile", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, 
																					produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> saveChangesOnProfile(@RequestBody HashMap<String, String> hm) {
		User user = getLoggedUserLocalMethod();
		
		if(user != null) {
			if(user.getUserType() == UserType.REGISTERED_USER) {
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
					setLogged(username);
					
					if(!repeatNewPassword.equals("")) loggedUser.setPassword(repeatNewPassword);
					loggedUser.setFirstName(firstName);
					loggedUser.setLastName(lastName);
					loggedUser.setEmail(email);
					loggedUser.setCity(city);
					loggedUser.setPhoneNumber(phoneNumber);
					
					userService.registrate(loggedUser); // cuvanje napravljenih izmena
				}
				
				return new ResponseEntity<Integer>(correct, HttpStatus.OK);
			}
			
		}
		
		return new ResponseEntity<Integer>(-1, HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/logout", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity logout(String username) {
		httpSession.invalidate(); // brisemo sve sa sesije
		System.out.println("Logout");
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/get_people", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArrayList<String>> getPeople() {
		User user = getLoggedUserLocalMethod();
		
		if(user != null) {
			if (user.getUserType() == UserType.REGISTERED_USER) {
				RegisteredUser ru = (RegisteredUser) user;
				
				ArrayList<String> people = userService.getPeople(ru);
				
				return new ResponseEntity<ArrayList<String>>(people, HttpStatus.OK);
			}
		}
		
		// vracamo praznu arrayList-u, ako nijedan korisnik nije ulogovan
		return new ResponseEntity<ArrayList<String>>(new ArrayList<String>(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/get_friends", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArrayList<String>> getFriends() {
		User user = getLoggedUserLocalMethod();
		ArrayList<String> friends = new ArrayList<String>();
		
		if(user != null) {
			if (user.getUserType() == UserType.REGISTERED_USER) {
				RegisteredUser ru = (RegisteredUser) user;
				for (RegisteredUser registeredUser : ru.getFriends()) {
					friends.add(registeredUser.toString());
				}
				
			}
		}
		
		// vracamo praznu arrayList-u, ako nijedan korisnik nije ulogovan
		return new ResponseEntity<ArrayList<String>>(friends, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/get_requests", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArrayList<String>> getRequests() {
		User user = getLoggedUserLocalMethod();
		ArrayList<String> requests = new ArrayList<String>();
		
		if(user != null) {
			if (user.getUserType() == UserType.REGISTERED_USER) {
				RegisteredUser ru = (RegisteredUser) user;
				for (RegisteredUser registeredUser : ru.getRequests()) {
					requests.add(registeredUser.toString());
				}
				
			}
		}
		
		// vracamo praznu arrayList-u, ako nijedan korisnik nije ulogovan
		return new ResponseEntity<ArrayList<String>>(requests, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/send_request_friend", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> sendRequestFriend(@RequestBody HashMap<String, String> hm) {
		String usernameOfPotentialFriend = hm.get("username");
		User user = getLoggedUserLocalMethod();
		
		if(user != null) {
			if (user.getUserType() == UserType.REGISTERED_USER) {
				User user2 = userService.getUser(usernameOfPotentialFriend);
				if(user2 != null) {
					if(user2.getUserType() == UserType.REGISTERED_USER) {
						RegisteredUser ru = (RegisteredUser) user;
						RegisteredUser potentialFriend = (RegisteredUser) user2;
						
						if(!ru.equals(potentialFriend) && !potentialFriend.getRequests().contains(ru) && !ru.getFriends().contains(potentialFriend)) {
							potentialFriend.getRequests().add(ru);
							userService.registrate(potentialFriend); // cuvanje izmena (dodavanje novo request-a)
							
							// zahtev uspesno poslat
							return new ResponseEntity<>(true, HttpStatus.OK);
						}
					}
				}
				
			}
			
			
		}
		
		// zahtev nije poslat 
		return new ResponseEntity<>(false, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/accept_request_friend", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> acceptRequestFriend(@RequestBody HashMap<String, String> hm) {
		String usernameOfNewFriend = hm.get("username");
		User user = getLoggedUserLocalMethod();
		
		if(user != null) {
			if (user.getUserType() == UserType.REGISTERED_USER) {
				User user2 = userService.getUser(usernameOfNewFriend);
				if(user2 != null) {
					if(user2.getUserType() == UserType.REGISTERED_USER) {
						RegisteredUser ru = (RegisteredUser) user;
						RegisteredUser ru2 = (RegisteredUser) user2;
						
						if(!ru.equals(ru2) && ru.getRequests().contains(ru2) && !ru.getFriends().contains(ru2)) {
							ru.getRequests().remove(ru2);
							ru.getFriends().add(ru2);
							ru2.getFriends().add(ru);
							userService.registrate(ru); // cuvanje izmena (dodavanje novo friend-a)
							userService.registrate(ru2); // cuvanje izmena (dodavanje novo friend-a)
							
							return new ResponseEntity<>(true, HttpStatus.OK);
						}
					}
				}
				
			}
			
			
		}
		
		return new ResponseEntity<>(false, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/decline_request_friend", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> declineRequestFriend(@RequestBody HashMap<String, String> hm) {
		String usernameOfDeclineFriend = hm.get("username");
		User user = getLoggedUserLocalMethod();
		
		if(user != null) {
			if (user.getUserType() == UserType.REGISTERED_USER) {
				User user2 = userService.getUser(usernameOfDeclineFriend);
				if(user2 != null) {
					if(user2.getUserType() == UserType.REGISTERED_USER) {
						RegisteredUser ru = (RegisteredUser) user;
						RegisteredUser ru2 = (RegisteredUser) user2;
						
						if(!ru.equals(ru2) && ru.getRequests().contains(ru2) && !ru.getFriends().contains(ru2)) {
							ru.getRequests().remove(ru2);
							
							userService.registrate(ru); // cuvanje izmena (brisanje request-a)
							
							return new ResponseEntity<>(true, HttpStatus.OK);
						}
					}
				}
				
			}
			
			
		}
		
		return new ResponseEntity<>(false, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/remove_friend", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> removeFriend(@RequestBody HashMap<String, String> hm) {
		String removeFriend = hm.get("username");
		User user = getLoggedUserLocalMethod();
		
		if(user != null) {
			if (user.getUserType() == UserType.REGISTERED_USER) {
				User user2 = userService.getUser(removeFriend);
				if(user2 != null) {
					if(user2.getUserType() == UserType.REGISTERED_USER) {
						RegisteredUser ru = (RegisteredUser) user;
						RegisteredUser ru2 = (RegisteredUser) user2;
						
						if(!ru.equals(ru2) && ru.getFriends().contains(ru2) && ru2.getFriends().contains(ru)) {
							ru.getFriends().remove(ru2);
							ru2.getFriends().remove(ru);
							userService.registrate(ru); // cuvanje izmena (brisanje friend-a)
							userService.registrate(ru2); // cuvanje izmena (brisanje friend-a)
							
							return new ResponseEntity<>(true, HttpStatus.OK);
						}
					}
				}
				
			}
			
			
		}
		
		return new ResponseEntity<>(false, HttpStatus.OK);
	}
	
}
