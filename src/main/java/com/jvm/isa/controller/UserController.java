package com.jvm.isa.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jvm.isa.domain.Administrator;
import com.jvm.isa.domain.AdministratorDTO;
import com.jvm.isa.domain.CulturalInstitution;
import com.jvm.isa.domain.ImageModel;
import com.jvm.isa.domain.RegisteredUser;
import com.jvm.isa.domain.RegisteredUserDTO;
import com.jvm.isa.domain.Showing;
import com.jvm.isa.domain.ShowingDTO;
import com.jvm.isa.domain.SysAdministrator;
import com.jvm.isa.domain.SysAdministratorDTO;
import com.jvm.isa.domain.Term;
import com.jvm.isa.domain.Ticket;
import com.jvm.isa.domain.TicketDTO;
import com.jvm.isa.domain.User;
import com.jvm.isa.domain.UserDTO;
import com.jvm.isa.domain.UserStatus;
import com.jvm.isa.domain.UserType;
import com.jvm.isa.service.CulturalInstitutionService;
import com.jvm.isa.service.EmailService;
import com.jvm.isa.service.ImageModelService;
import com.jvm.isa.service.TermService;
import com.jvm.isa.service.TicketService;
import com.jvm.isa.service.UserService;


@RestController
@RequestMapping(value = "/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TermService termService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private TicketService ticketService;
	
	//@Autowired
	//private AuditoriumRepository auditoriumRepository;
	
	@Autowired
	private CulturalInstitutionService culturalInstitutionService;
	
	//@Autowired
	//private TermRepository termRepository;
	
	@Autowired
	private ImageModelService imageModelService;
	
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
	
	@RequestMapping(value = "/registrate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> registrate(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("repeat_password") String repeatPassword, @RequestParam("first_name") String firstName, @RequestParam("last_name") String lastName, @RequestParam("email") String email, @RequestParam("city") String city, @RequestParam("phone_number") String phoneNumber, @RequestParam("image") MultipartFile image) { 
		/*String username = hm.get("username");
		String password = hm.get("password");
		String repeatPassword = hm.get("repeatPassword");
		String firstName = hm.get("firstName");
		String lastName = hm.get("lastName");
		String email = hm.get("email");
		String city = hm.get("city");
		String phoneNumber = hm.get("phoneNumber");*/

		//System.out.println("|" + username + " - " + password + " - " + repeatPassword + " - " + firstName + " - " + lastName + " - " + email + " - " + city + " - " + phoneNumber + "|");
		
		int correct = userService.correctUser(null, username, null, password, repeatPassword, firstName, lastName, email, city, phoneNumber);
		boolean successRegistrate;
		
		if(correct == 6)	{
			RegisteredUser user = new RegisteredUser(username, password, firstName, lastName, email, city, phoneNumber);
			user.setUserType(UserType.REGISTERED_USER);
			user.setUserStatus(UserStatus.PENDING);
			successRegistrate = userService.registrate(user);
			
			try {
				if(successRegistrate) emailService.sendActivationEmailAsync(user);
			}
			catch(Exception e)	{
				System.out.println("Greska prilikom slanja emaila! - " + e.getMessage());
			}
			
			if(successRegistrate) {
				if(!image.isEmpty()) {
					String fileName = "user_" + username;
					imageModelService.saveImageinDatabase(fileName, image);
				}
			}
			
		}
		else successRegistrate = false;
		
		return new ResponseEntity<Boolean>(successRegistrate, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, 
																			produces = MediaType.APPLICATION_JSON_VALUE)
	// NIJE MI RADILO BEZ ANOTACIJE @RequestBody ZA PARAMETAR METODE
	public ResponseEntity<Boolean> login(@RequestBody HashMap<String, String> hm) {
		System.out.println("|" + hm.get("username").trim() + " - " + hm.get("password").trim() + "|");
		User user = userService.getUser(hm.get("username").trim(), hm.get("password").trim());
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
	
		@RequestMapping(value = "/save_changes_on_profile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> saveChangesOnProfile(@RequestParam("username") String username, @RequestParam("old_password") String oldPassword, @RequestParam("new_password") String newPassword, @RequestParam("repeat_new_password") String repeatNewPassword, @RequestParam("first_name") String firstName, @RequestParam("last_name") String lastName, @RequestParam("email") String email, @RequestParam("city") String city, @RequestParam("phone_number") String phoneNumber, @RequestParam("image") MultipartFile image) {
		User user = getLoggedUserLocalMethod();
		
		if(user != null) {
			if(user.getUserType() == UserType.REGISTERED_USER) {
				RegisteredUser loggedUser = (RegisteredUser) user;
				
				/*String username = hm.get("username");
				String oldPassword = hm.get("oldPassword");
				String newPassword = hm.get("newPassword");
				String repeatNewPassword = hm.get("repeatNewPassword");
				String firstName = hm.get("firstName");
				String lastName = hm.get("lastName");
				String email = hm.get("email");
				String city = hm.get("city");
				String phoneNumber = hm.get("phoneNumber");*/
				
				int correct = userService.correctUser(loggedUser, username, oldPassword, newPassword, repeatNewPassword, firstName, lastName, email, city, phoneNumber);
				if(correct == 6) {
					String oldUsername = loggedUser.getUsername();
					loggedUser.setUsername(username);
					setLogged(username);
					
					if(!repeatNewPassword.equals("")) loggedUser.setPassword(repeatNewPassword);
					loggedUser.setFirstName(firstName);
					loggedUser.setLastName(lastName);
					
					boolean emailChanged = false;
					if (!email.equals(loggedUser.getEmail())) {
						loggedUser.setEmail(email);
						emailChanged = true;
						
					}
					loggedUser.setCity(city);
					loggedUser.setPhoneNumber(phoneNumber);
					
					boolean success = userService.registrate(loggedUser); // cuvanje napravljenih izmena
					
					if(success) {
						try {
							if(emailChanged) emailService.sendUserChangedEmail(loggedUser.getUsername(), loggedUser.getPassword(), loggedUser.getEmail());
						} catch (MessagingException e) {
							System.out.println("Greska prilikom slanja emaila! - " + e.getMessage());
						}
						
						ImageModel oldImageModel = imageModelService.getImageModel("user_" + oldUsername);
						
						if(!image.isEmpty()) {
							if(oldImageModel != null) {
								if(!oldUsername.equals(username)) {
									oldImageModel.setName("user_" + username);
								}
								try {
									oldImageModel.setPic(image.getBytes());
									imageModelService.save(oldImageModel);
								} catch (IOException e) {
									System.out.println("Image model error");
								}
								
							}
							else {
								imageModelService.saveImageinDatabase("user_" + username, image);
							}
							
						}
						else {
							if(!oldUsername.equals(username) && oldImageModel != null) {
								imageModelService.removeImageFromDatabase(oldImageModel);
							}
						}
						
					}
					else correct = 7;
				}
				
				return new ResponseEntity<Integer>(correct, HttpStatus.OK);
			}
			
		}
		
		return new ResponseEntity<Integer>(-1, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/save_changed_password", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, 
																					produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> saveRegisteredUserChangedPassword(@RequestBody HashMap<String, String> hm) {
		return saveChangedPassword(hm);
	}
	
	public ResponseEntity<Integer> saveChangedPassword(HashMap<String, String> hm) {
		User user = getLoggedUserLocalMethod();
		
		if(user != null) {
			String oldPassword = hm.get("oldPassword");
			String newPassword = hm.get("newPassword");
			String repeatNewPassword = hm.get("repeatNewPassword");
			
			int correct = userService.correctChangepassword(user, oldPassword, newPassword, repeatNewPassword);
			if(correct == 3) {
				user.setPassword(repeatNewPassword);
			
				userService.registrate(user); // cuvanje napravljenih izmena
			}
		
			return new ResponseEntity<Integer>(correct, HttpStatus.OK);
			
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
	
	@RequestMapping(value = "/book_selected_seats", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> bookSelectedSeats(@RequestBody HashMap<String, String> hm) {
		User loggedUser = getLoggedUserLocalMethod();
		
		if(loggedUser != null) {
			if(loggedUser.getUserType() == UserType.REGISTERED_USER) {
				String dateStr = hm.get("date").trim();
				String timeStr = hm.get("time").trim();
				String culturalInstitutionName = hm.get("culturalInstitution").trim();
				String showingName = hm.get("showing").trim();
				String auditoriumName = hm.get("auditorium");
				String selectedSeats = hm.get("selectedSeats").trim();
				
				Boolean success;
				
				if(!selectedSeats.equals("")) {
					success = termService.bookSelectedSeats(dateStr, timeStr, culturalInstitutionName, showingName, auditoriumName, selectedSeats, (RegisteredUser) loggedUser);
				
				}
				else {
					success = false;
				}
				
				return new ResponseEntity<Boolean>(success, HttpStatus.OK);
			}
		}
			
		return new ResponseEntity<Boolean>(false, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/get_indexes_of_busy_seats_and_rows_cols", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HashMap<String, Object>> getIndexesOfBusySeatsAndRowsCols(@RequestBody HashMap<String, String> hm) {
		String dateStr = hm.get("date").trim();
		String timeStr = hm.get("time").trim();
		String culturalInstitutionName = hm.get("culturalInstitution").trim();
		String showingName = hm.get("showing").trim();
		String auditoriumName = hm.get("auditorium").trim();
		
		HashMap<String, Object> hmReturn = new HashMap<String, Object>();
		
		
		Term term = termService.getTerm(dateStr, timeStr, culturalInstitutionName, showingName, auditoriumName);
		if(term != null) {
			ArrayList<Integer> indexOfBusySeatsAndRowsCols = termService.getIndexOfBusySeatsAndRowsCols(term);
			hmReturn.put("existsTerm", true);
			hmReturn.put("indexOfBusySeatsAndRowsCols", indexOfBusySeatsAndRowsCols);
		}
		else {
			hmReturn.put("existsTerm", false);
		}
		
		return new ResponseEntity<HashMap<String, Object>>(hmReturn, HttpStatus.OK);
	}
	
	/*
	 * return value:
	 * 0 - all right
	 * 1 - You did not put yourself
	 * 2 - there are repetitions
	*/
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/send_seats_and_friends", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
																				produces = MediaType.APPLICATION_JSON_VALUE)
	// NIJE MI RADILO BEZ ANOTACIJE @RequestBody ZA PARAMETAR METODE
	public ResponseEntity<Integer> sendSeatsAndFriends(@RequestBody HashMap<String, Object> hm) { 
		User loggedUser = getLoggedUserLocalMethod();
		
		if(loggedUser != null) {
			if(loggedUser.getUserType() == UserType.REGISTERED_USER) {
				RegisteredUser loggedRegisteredUser = (RegisteredUser) loggedUser;

				
				String dateStr = ((String) hm.get("date")).trim();
				String timeStr = ((String) hm.get("time")).trim();
				String culturalInstitutionName = ((String) hm.get("culturalInstitution")).trim();
				String showingName = ((String) hm.get("showing")).trim();
				String auditoriumName = ((String) hm.get("auditorium")).trim();
				HashMap<String, String> seatsAndFriends = (HashMap<String, String>) hm.get("seatsAndFriends");
				
				int retValue = termService.thereAreRepetitionsOrNotPutYourself(seatsAndFriends.values(), loggedUser.getUsername());
				if(retValue != 0) {
					return new ResponseEntity<Integer>(retValue, HttpStatus.OK);
				}
				
				RegisteredUser friend;
				User user;
				Ticket ticket;
				Term term = termService.getTerm(dateStr, timeStr, culturalInstitutionName, showingName, auditoriumName);
				if(term == null) return new ResponseEntity<Integer>(-1, HttpStatus.OK);
				
				ArrayList<Ticket> loggedRegisteredUserTickets = new ArrayList<Ticket>();
				ArrayList<Ticket> savedTickets = new ArrayList<Ticket>();
				
				int seat;
				Ticket savedTicket;
				
				for (String seatStr : seatsAndFriends.keySet()) {
					//ticket = ticketService.getTicket(term, Integer.parseInt(seat));
					seat = Integer.parseInt(seatStr) - 1;
					ticket = new Ticket(term, loggedRegisteredUser, loggedRegisteredUser, seat);
					
					savedTicket = ticketService.save(ticket);
					savedTickets.add(savedTicket);
					//ticket.setOwner(loggedRegisteredUser);
					
					if(seatsAndFriends.get(seatStr).equals(loggedRegisteredUser.getUsername())) {
						loggedRegisteredUserTickets.add(savedTicket);
					}
					
					else {
						user = userService.getUser(seatsAndFriends.get(seatStr));
						if(user.getUserType() == UserType.REGISTERED_USER) {
							friend = (RegisteredUser) user;
							try {
								emailService.sendInviteForShowing(culturalInstitutionName, showingName, dateStr, timeStr, term.getAuditorium().getName(), seatStr, term.getPrice(), term.getShowing().getDuration(), loggedRegisteredUser, friend);
								
								friend.getInvitations().add(savedTicket);
								userService.registrate(friend);
							}
							catch (Exception e) {
								System.out.println("Greska prilikom slanja emaila! - " + e.getMessage());
							}
							
						}
						
					}
				}
				
				if(loggedRegisteredUserTickets.size() > 0) saveTicketsForLoggedUser(loggedRegisteredUser, loggedRegisteredUserTickets);
				
				saveTicketsForCulturalInstitution(term.getCulturalInstitution(), savedTickets);
				
				return new ResponseEntity<Integer>(0, HttpStatus.OK);
			}
		}
		
		return new ResponseEntity<Integer>(-1, HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/send_seats_for_release", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
																					produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> sendSeatsForRelease(@RequestBody HashMap<String, Object> hm) { 
		User loggedUser = getLoggedUserLocalMethod();

		if(loggedUser != null) {
			if(loggedUser.getUserType() == UserType.REGISTERED_USER) {
				String dateStr = ((String) hm.get("date")).trim();
				String timeStr = ((String) hm.get("time")).trim();
				String culturalInstitutionName = ((String) hm.get("culturalInstitution")).trim();
				String showingName = ((String) hm.get("showing")).trim();
				String auditoriumName = ((String) hm.get("auditorium")).trim();
				ArrayList<String> seats = (ArrayList<String>) hm.get("seats");
				
				Term term = termService.getTerm(dateStr, timeStr, culturalInstitutionName, showingName, auditoriumName);
				if(term != null) {
					boolean success = true;
					
					try {
						for (String seatStr : seats) {
							term.getSeats()[Integer.parseInt(seatStr) - 1] = false;
						}
					}
					catch(Exception e) {
						success = false;
					}
					
					if(success) {
						success = termService.save(term);
					}
					
					return new ResponseEntity<Boolean>(success, HttpStatus.OK);
				}
			}
		}
	
		return new ResponseEntity<Boolean>(false, HttpStatus.OK);
	}
	
	private void saveTicketsForLoggedUser(RegisteredUser loggedRegisteredUser, ArrayList<Ticket> loggedRegisteredUserTickets) {
		loggedRegisteredUser.getTickets().addAll(loggedRegisteredUserTickets);
		userService.registrate(loggedRegisteredUser);
	}
	
	private void saveTicketsForCulturalInstitution(CulturalInstitution culturalInstitution, ArrayList<Ticket> savedTickets) {
		culturalInstitution.getTickets().addAll(savedTickets);
		culturalInstitutionService.save(culturalInstitution);
	}
	
	@RequestMapping(value = "/get_visited_and_unvisited_cultural_institutions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HashMap<String, Object>> getVisitedAndUnvisitedCulturalInstitutions() {
		User loggedUser = getLoggedUserLocalMethod();
		
		if(loggedUser != null) {
			HashMap<String, Object> visitedAndUnvisitedCulturalInstitutions = ticketService.getVisitedAndUnvisitedCulturalInstitutions((RegisteredUser) loggedUser);
			return new ResponseEntity<HashMap<String, Object>>(visitedAndUnvisitedCulturalInstitutions, HttpStatus.OK);
		}
		
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("has", false);
		return new ResponseEntity<HashMap<String, Object>>(hm, HttpStatus.OK);
	
	}
	
	@RequestMapping(value = "/get_cultural_institutions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArrayList<String>> getCulturalInstitutions() {
		ArrayList<String> culturalInstitutions = termService.getCulturalInstitutions();
		
		
		/*
		CulturalInstitution culturalInstitution = culturalInstitutionService.getCulturalInstitution("Pozoriste mladih");
		CulturalInstitution culturalInstitution2 = culturalInstitutionService.getCulturalInstitution("CineStar");
		Showing showing1 = culturalInstitution.getShowing("Repo Man");
		Showing showing2 = culturalInstitution2.getShowing("Repo Man");
		
		Auditorium auditorium1 = auditoriumRepository.findByName("Sala 8");
		termRepository.save(new Term(LocalDate.now(), LocalTime.parse("19:00", DateTimeFormatter.ISO_TIME), culturalInstitution, auditorium1, showing1));
		Auditorium auditorium2 = auditoriumRepository.findByName("Sala 4");
		termRepository.save(new Term(LocalDate.now(), LocalTime.parse("16:00", DateTimeFormatter.ISO_TIME), culturalInstitution, auditorium2, showing1));
		Auditorium auditorium3= auditoriumRepository.findByName("Sala 3");
		termRepository.save(new Term(LocalDate.now(), LocalTime.parse("21:00", DateTimeFormatter.ISO_TIME), culturalInstitution2, auditorium2, showing2));
		*/
		
		return new ResponseEntity<ArrayList<String>>(culturalInstitutions, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/get_showings_of_cultural_institution/{culturalInstitutionName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArrayList<String>> getSowings(@PathVariable("culturalInstitutionName") String culturalInstitutionName) {
		ArrayList<String> showings = termService.getShowings(culturalInstitutionName);
		
		return new ResponseEntity<ArrayList<String>>(showings, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/get_dates", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArrayList<String>> getDates(@RequestBody HashMap<String, String> hm) {
		String culturalInstitutionName = hm.get("culturalInstitution").trim();
		String showingName = hm.get("showing").trim();
	
	
		ArrayList<String> dates = termService.getDates(culturalInstitutionName, showingName);
	
		return new ResponseEntity<ArrayList<String>>(dates, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/get_auditoriums", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
																					produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArrayList<String>> getAuditoriums(@RequestBody HashMap<String, String> hm) {
		String dateStr = hm.get("date").trim();
		String culturalInstitutionName = hm.get("culturalInstitution").trim();
		String showingName = hm.get("showing").trim();
		

		ArrayList<String> auditoriumsAndTimes = termService.getAuditoriums(culturalInstitutionName, showingName, dateStr);
		
		return new ResponseEntity<ArrayList<String>>(auditoriumsAndTimes, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/get_times", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArrayList<String>> getTimes(@RequestBody HashMap<String, String> hm) {
		String dateStr = hm.get("date").trim();
		String culturalInstitutionName = hm.get("culturalInstitution").trim();
		String showingName = hm.get("showing").trim();
		String auditoriumStr = hm.get("auditorium").trim();
		

		ArrayList<String> times = termService.getTimes(culturalInstitutionName, showingName, dateStr, auditoriumStr);
		
		return new ResponseEntity<ArrayList<String>>(times, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/get_price", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HashMap<String, Object>> getPrice(@RequestBody HashMap<String, String> hm) {
		String dateStr = hm.get("date").trim();
		String culturalInstitutionName = hm.get("culturalInstitution").trim();
		String showingName = hm.get("showing").trim();
		String auditoriumStr = hm.get("auditorium").trim();
		String timeStr = hm.get("time").trim();
		
		HashMap<String, Object> retHm = new HashMap<String, Object>();
		
		Term term = termService.getTerm(dateStr, timeStr, culturalInstitutionName, showingName, auditoriumStr);
		boolean hasPrice = (term != null);
		retHm.put("hasPrice", hasPrice);
		if(hasPrice) {
			retHm.put("price", term.getPrice());
		}
		
		return new ResponseEntity<HashMap<String, Object>>(retHm, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/get_tickets", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArrayList<TicketDTO>> getTickets() {
		User user = getLoggedUserLocalMethod();
		ArrayList<TicketDTO> tickets = new ArrayList<TicketDTO>();
		
		if(user != null) {
			if (user.getUserType() == UserType.REGISTERED_USER) {
				RegisteredUser ru = (RegisteredUser) user;
				for (Ticket ticket : ru.getTickets()) {
					try {
						if(ticket.getTerm() != null) tickets.add(new TicketDTO(ticket));
					}
					catch(Exception e) {}
					
				}
				
			}
		}
		
		// vracamo praznu arrayList-u, ako nijedan korisnik nije ulogovan
		return new ResponseEntity<ArrayList<TicketDTO>>(tickets, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/get_invitations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArrayList<TicketDTO>> getInvitations() {
		User user = getLoggedUserLocalMethod();
		ArrayList<TicketDTO> invitations = new ArrayList<TicketDTO>();
		
		if(user != null) {
			if (user.getUserType() == UserType.REGISTERED_USER) {
				RegisteredUser ru = (RegisteredUser) user;
				for (Ticket ticket : ru.getInvitations()) {
					invitations.add(new TicketDTO(ticket));
				}
				
			}
		}
		
		// vracamo praznu arrayList-u, ako nijedan korisnik nije ulogovan
		return new ResponseEntity<ArrayList<TicketDTO>>(invitations, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/accept_invitation", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> acceptInvitation(@RequestBody TicketDTO ticketDTO) {
		User user = getLoggedUserLocalMethod();
		
		if(user != null) {
			if (user.getUserType() == UserType.REGISTERED_USER) {
				RegisteredUser ru = (RegisteredUser) user;
				Ticket ticket = ticketService.getTicket(ticketDTO.getDate(), ticketDTO.getTime(), ticketDTO.getCulturalInstitution(), ticketDTO.getShowing(), ticketDTO.getAuditorium(), ticketDTO.getSeat());
				
				if(ru.getInvitations().contains(ticket) && !ru.getTickets().contains(ticket)) {
					ticket.setOwner(ru);
					ru.getInvitations().remove(ticket);
					ru.getTickets().add(ticket);
					userService.registrate(ru); // cuvanje izmena (ticket friend-a)
					
					return new ResponseEntity<>(true, HttpStatus.OK);
				}
			
				
			}
			
			
		}
		
		return new ResponseEntity<>(false, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/decline_invitation", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> declineInvitation(@RequestBody TicketDTO ticketDTO) {
		User user = getLoggedUserLocalMethod();
		
		if(user != null) {
			if (user.getUserType() == UserType.REGISTERED_USER) {
				RegisteredUser ru = (RegisteredUser) user;
				Ticket ticket = ticketService.getTicket(ticketDTO.getDate(), ticketDTO.getTime(), ticketDTO.getCulturalInstitution(), ticketDTO.getShowing(), ticketDTO.getAuditorium(), ticketDTO.getSeat());
				if(ticket == null) return new ResponseEntity<Boolean>(false, HttpStatus.OK);
				
				if(ru.getInvitations().contains(ticket)) {
					ru.getInvitations().remove(ticket);
					Term term = ticket.getTerm();
					term.getSeats()[ticket.getSeat()] = false;
					
					userService.registrate(ru); // cuvanje izmena (brisanje ticket-a)
					
					termService.save(term);
					
					CulturalInstitution culturalInstitution = term.getCulturalInstitution();
					if(culturalInstitution.getTickets().contains(ticket)) {
						culturalInstitution.getTickets().remove(ticket);
						culturalInstitutionService.save(culturalInstitution);
					}
					
					return new ResponseEntity<Boolean>(true, HttpStatus.OK);
				}
			
				
			}
			
			
		}
		
		return new ResponseEntity<Boolean>(false, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/remove_ticket", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> removeTicket(@RequestBody TicketDTO ticketDTO) {
		User user = getLoggedUserLocalMethod();
		
		if(user != null) {
			if (user.getUserType() == UserType.REGISTERED_USER) {
				RegisteredUser ru = (RegisteredUser) user;
				Ticket ticket = ticketService.getTicket(ticketDTO.getDate(), ticketDTO.getTime(), ticketDTO.getCulturalInstitution(), ticketDTO.getShowing(), ticketDTO.getAuditorium(), ticketDTO.getSeat());
				if(ticket == null) return new ResponseEntity<Boolean>(false, HttpStatus.OK);
				
				if(userService.computeSubtractTwoDateTime(LocalDate.now(), ticket.getTerm().getDate(), LocalTime.now(), ticket.getTerm().getTime()) < 30) {
					return new ResponseEntity<Boolean>(false, HttpStatus.OK); // za 30 min pocinje predstava/projekcija (nije moguce otkazati)
				}
				
				if(ru.getTickets().contains(ticket)) {
					ru.getTickets().remove(ticket);
					Term term = ticket.getTerm();
					term.getSeats()[ticket.getSeat() - 1] = false;
					
					userService.registrate(ru); // cuvanje izmena (brisanje ticket-a)
					termService.save(term);
					
					CulturalInstitution culturalInstitution = term.getCulturalInstitution();
					if(culturalInstitution.getTickets().contains(ticket)) {
						culturalInstitution.getTickets().remove(ticket);
						culturalInstitutionService.save(culturalInstitution);
					}
					
					
					return new ResponseEntity<Boolean>(true, HttpStatus.OK);
				}
			
				
			}
			
			
		}
		
		return new ResponseEntity<>(false, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/get_image/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HashMap<String, Object>> getImage(@PathVariable("name") String name) {
		ImageModel imageModel = imageModelService.getImageModel(name);
		HashMap<String, Object> hm = new HashMap<String, Object>();
		
		boolean hasImage = (imageModel != null);
		hm.put("hasImage", hasImage);
		if(hasImage) hm.put("imageModel", imageModel);
		
		return new ResponseEntity<HashMap<String, Object>>(hm, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/get_showing_for_show/{name}/{culturalInstitutionName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HashMap<String, Object>> getShowingForShow(@PathVariable("name") String name, @PathVariable("culturalInstitutionName") String culturalInstitutionName) {
		CulturalInstitution culturalInstitution = culturalInstitutionService.getCulturalInstitution(culturalInstitutionName);
		HashMap<String, Object> hm = new HashMap<String, Object>();
		
		boolean hasShowing = (culturalInstitution != null);
		if(!hasShowing) {
			hm.put("hasShowing", hasShowing);
		}
		else {
			Showing showing = culturalInstitution.getShowing(name);
			hasShowing = (showing != null);
			hm.put("hasShowing", hasShowing);
			
			if(hasShowing) {
				hm.put("showing", new ShowingDTO(showing));
			}
			
		}
		
		return new ResponseEntity<HashMap<String, Object>>(hm, HttpStatus.OK);
	}
	
}
