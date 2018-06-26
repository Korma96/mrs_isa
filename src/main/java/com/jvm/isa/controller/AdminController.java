package com.jvm.isa.controller;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jvm.isa.domain.Administrator;
import com.jvm.isa.domain.Auditorium;
import com.jvm.isa.domain.CulturalInstitution;
import com.jvm.isa.domain.CulturalInstitutionDTO;
import com.jvm.isa.domain.CulturalInstitutionType;
import com.jvm.isa.domain.ImageModel;
import com.jvm.isa.domain.Requisite;
import com.jvm.isa.domain.RequisiteDTO;
import com.jvm.isa.domain.Showing;
import com.jvm.isa.domain.ShowingDTO;
import com.jvm.isa.domain.ShowingType;
import com.jvm.isa.domain.SysAdministrator;
import com.jvm.isa.domain.User;
import com.jvm.isa.domain.UserStatus;
import com.jvm.isa.domain.UserType;
import com.jvm.isa.service.AdminService;
import com.jvm.isa.service.CulturalInstitutionService;
import com.jvm.isa.service.EmailService;
import com.jvm.isa.service.ImageModelService;
import com.jvm.isa.service.TermService;
import com.jvm.isa.service.TicketService;


@RestController
@RequestMapping(value = "/administrators")

public class AdminController {

	private final String ADMIN_CONST = "admin";
	private final String ADMIN_PASSWORD = "admin_#password$";
	private final String[] ADMIN_TYPES = { "fun", "cul", "sys" };

	private Random random = new Random();

	@Autowired
	private AdminService adminService;

	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private CulturalInstitutionService culturalInstitutionService;

	@Autowired
	private UserController userController;
	
	@Autowired
	private TermService termService;
	
	@Autowired
	private ImageModelService imageModelService;
	

	@RequestMapping(value = "/sys_admin/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	// NIJE MI RADILO BEZ ANOTACIJE @RequestBody ZA PARAMETAR METODE
	public ResponseEntity<Boolean> registerSysAdmin(@RequestBody HashMap<String, String> hm) {
		String adminRole = hm.get("adminRole");
		String email = hm.get("email");

		String username = ADMIN_CONST + "_";
		String password = ADMIN_PASSWORD + "_" + random.nextInt(10000);
		String firstName = null;
		String lastName = null;
		
		CulturalInstitution ci = null;

		if (adminRole.equals("SA")) {
			username += ADMIN_TYPES[2];

		} else {
			String culturalInstitutionName = hm.get("culturalInstitutionName");
			ci = culturalInstitutionService.getCulturalInstitution(culturalInstitutionName);
			
			if (adminRole.equals("CIA")) {
				username += ADMIN_TYPES[1];
			} else if (adminRole.equals("FZA")) {
				username += ADMIN_TYPES[0];
			}

			firstName = hm.get("firstName");
			lastName = hm.get("lastName");
		}

		String potentionalUsername;
		do {
			potentionalUsername = username + "_" + random.nextInt(10000);
		} while (adminService.exists(potentionalUsername));

		boolean successregister = false;
		String typeOfAdmin = null;

		if (firstName != null && lastName != null) {
			if (!firstName.equals("") && !lastName.equals("")) {
				UserType userType = null;
				if (adminRole.equals("CIA")) {
					typeOfAdmin = "cultural institution administrator";
					userType = UserType.INSTITUTION_ADMINISTRATOR;
				} else if (adminRole.equals("FZA")) {
					typeOfAdmin = "fun zone administrator";
					userType = UserType.FUNZONE_ADMINISTRATOR;
				}

				Administrator admin = new Administrator(potentionalUsername, password, firstName, lastName, email,
						userType, ci);
				successregister = adminService.register(admin);

				if (successregister) {
					try {
						//emailService.sendNewAdminEmailAsyncSparkPost(potentionalUsername, password, email, typeOfAdmin);
						emailService.sendNewAdminEmailAsync(potentionalUsername, password, email, typeOfAdmin);
					} catch (MessagingException e) {
						System.out.println("Greska prilikom slanja emaila! - " + e.getMessage());
					}
				}
			}
		} else {
			typeOfAdmin = "system administrator";
			SysAdministrator sysAdministrator = new SysAdministrator(potentionalUsername, password, email);
			successregister = adminService.register(sysAdministrator);

			if (successregister) {
				try {
					//emailService.sendNewAdminEmailAsyncSparkPost(potentionalUsername, password, email, typeOfAdmin);
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

			int correct = adminService.validSystemAdmin(user, username, oldPassword, newPassword, repeatNewPassword,
					email);
			if (correct == 0) {
				SysAdministrator sysAdministrator = (SysAdministrator) user;
				sysAdministrator.setUsername(username);
				userController.setLogged(username);
				if (!newPassword.equals(""))
					sysAdministrator.setPassword(newPassword);

				if (!email.equals(sysAdministrator.getEmail())) {
					sysAdministrator.setEmail(email);
					try {
						//emailService.sendUserChangedEmailAsyncSparkPost(sysAdministrator.getUsername(), sysAdministrator.getPassword(), sysAdministrator.getEmail());
						emailService.sendUserChangedEmailAsync(sysAdministrator.getUsername(), sysAdministrator.getPassword(), sysAdministrator.getEmail());
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
	
	
	@RequestMapping(value = "/admin_cultural_institution/get_showings", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArrayList<Showing>> getShowings() 
	{
		ArrayList<Showing> returnList = culturalInstitutionService.getShowings();
		return new ResponseEntity<ArrayList<Showing>>(returnList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/admin_cultural_institution/get_showings_for_ci", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HashMap<String, Object>> getShowingsForCI() 
	{
		HashMap<String, Object> hm = new HashMap<String, Object>();
		
		User loggedUser = userController.getLoggedUserLocalMethod();
		if(loggedUser != null) {
			if(loggedUser.getUserType() == UserType.INSTITUTION_ADMINISTRATOR) {
				CulturalInstitution ci = ((Administrator) loggedUser).getCulturalInstitution();
				if(ci != null) {
					List<Showing> showings = ci.getShowings();
					if(showings != null) {
						ArrayList<ShowingDTO> showingsDTO = new ArrayList<ShowingDTO>(); 
						for (Showing showing : showings) {
							showingsDTO.add(new ShowingDTO(showing));
						}
						hm.put("has", true);
						hm.put("showings", showingsDTO);
						return new ResponseEntity<HashMap<String, Object>>(hm, HttpStatus.OK);
					}
					
				}
				
			}
		}

		hm.put("has", false);
		
		return new ResponseEntity<HashMap<String, Object>>(hm, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/admin_cultural_institution/get_auditoriums_for_ci", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>> getAuditoriumsForCI(@RequestBody HashMap<String, String> hm) 
	{
		String ciName = hm.get("ciName");
		List<Auditorium> auditoriums = culturalInstitutionService.getAuditoriums(ciName);
		List<String> returnList = new ArrayList<String>();
		
		if(auditoriums != null) {
			for(Auditorium a : auditoriums)
			{
				returnList.add(a.getName());
			}
			
		}
		
		return new ResponseEntity<List<String>>(returnList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/admin_cultural_institution/get_auditoriums_for_cultural_institution", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HashMap<String, Object>> getAuditoriumsForCulturalInstitution() 
	{	
		HashMap<String, Object> hm = new HashMap<String, Object>();
		
		User loggedUser = userController.getLoggedUserLocalMethod();
		if(loggedUser != null) {
			if(loggedUser.getUserType() == UserType.INSTITUTION_ADMINISTRATOR) {
				CulturalInstitution ci = ((Administrator) loggedUser).getCulturalInstitution();
				if(ci != null) {
					List<Auditorium> auditoriums = ci.getAuditoriums();
					if(auditoriums != null) {
						hm.put("has", true);
						hm.put("auditoriums", auditoriums);
						return new ResponseEntity<HashMap<String, Object>>(hm, HttpStatus.OK);
					}
					
				}
				
			}
		}

		hm.put("has", false);
		
		return new ResponseEntity<HashMap<String, Object>>(hm, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/admin_cultural_institution/get_terms", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>> getTerms(@RequestBody HashMap<String, String> hm) 
	{
		String auditoriumName = hm.get("auditorium");
		String date = hm.get("date");
		
		List<String> returnList = termService.getTermsByDateAndAuditorium(date, auditoriumName);

		return new ResponseEntity<List<String>>(returnList, HttpStatus.OK);
	}
		
	@RequestMapping(value = "/admin_cultural_institution/add_term", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> addTerm(@RequestBody HashMap<String, String> hm) 
	{
		User loggedUser = userController.getLoggedUserLocalMethod();
		if(loggedUser != null) {
			if(loggedUser.getUserType() == UserType.INSTITUTION_ADMINISTRATOR) {
				CulturalInstitution ci = ((Administrator) loggedUser).getCulturalInstitution();
				if(ci != null) {
					String showingName = hm.get("showing");
					String auditoriumName = hm.get("auditorium");
					String date = hm.get("date");
					String time = hm.get("time");
					String price = hm.get("price");
					boolean success = termService.addTerm(ci, date, auditoriumName, showingName, time, new Double(price));
					return new ResponseEntity<Boolean>(success, HttpStatus.OK);
				}
			}
		}
		
		
		return new ResponseEntity<Boolean>(false, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/admin_cultural_institution/delete_term", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> deleteTerm(@RequestBody HashMap<String, String> hm) 
	{
		String id = hm.get("id");
		boolean success = termService.deleteTerm(id);
		if(success)
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		return new ResponseEntity<Boolean>(false, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/admin_cultural_institution/update_profile", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> saveChangesOnProfileAdminCulturalInstitution(
			@RequestBody HashMap<String, String> hm) {
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
				if (!repeatNewPassword.equals(""))
					loggedUser.setPassword(repeatNewPassword);
				loggedUser.setFirstName(firstName);
				loggedUser.setLastName(lastName);
				if (!email.equals(loggedUser.getEmail())) {
					loggedUser.setEmail(email);
					try {
						//emailService.sendUserChangedEmailAsyncSparkPost(loggedUser.getUsername(), loggedUser.getPassword(), loggedUser.getEmail());
						emailService.sendUserChangedEmailAsync(loggedUser.getUsername(), loggedUser.getPassword(), loggedUser.getEmail());
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

	@RequestMapping(value = "/admin_funzone/save_changed_password", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> saveAdminFunZoneChangedPassword(@RequestBody HashMap<String, String> hm) {
		return userController.saveChangedPassword(hm);
	}

	@RequestMapping(value = "/admin_cultural_institution/save_changed_password", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> saveAdminCulturalInstitutionChangedPassword(
			@RequestBody HashMap<String, String> hm) {
		return userController.saveChangedPassword(hm);
	}

	@RequestMapping(value = "/sys_admin/add_new_cultural_institution", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> saveCulturalInstitution(@RequestParam("name") String name, @RequestParam("address") String address, @RequestParam("description") String description, @RequestParam("type") String type, @RequestParam("image") MultipartFile image) {
		/*String name = hm.get("name");
		String address = hm.get("address");
		String description = hm.get("description");
		String type = hm.get("type");*/
		
		if(name.equals("") || address.equals("") || description.equals("") || type.equals("")) {
			return new ResponseEntity<Boolean>(false, HttpStatus.OK);
		}
		
		CulturalInstitutionType ciType = CulturalInstitutionType.valueOf(type);
		CulturalInstitution ci = new CulturalInstitution(name, address, description, ciType);
		if (!culturalInstitutionService.exists(ci.getName())) {
			boolean success =culturalInstitutionService.save(ci);
			
			if(success) {
				if(!image.isEmpty()) {
					String fileName = "cultural_institution_" + name;  
					imageModelService.saveImageinDatabase(fileName, image);
				}
	
			}
			
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}
		
		return new ResponseEntity<Boolean>(false, HttpStatus.OK);
	}

	@RequestMapping(value = "/sys_admin/update_cultural_institution", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> updateCulturalInstitution(@RequestParam("old_name") String oldName, @RequestParam("name") String name, @RequestParam("address") String address, @RequestParam("description") String description, @RequestParam("image") MultipartFile image) {
		/*String oldName = hm.get("old_name");
		String name = hm.get("name");
		String address = hm.get("address");
		String description = hm.get("description");*/
		
		if(oldName.equals("") || name.equals("") || address.equals("") || description.equals("")) {
			return new ResponseEntity<Boolean>(false, HttpStatus.OK);
		}
		
		CulturalInstitution ci = culturalInstitutionService.getCulturalInstitution(oldName);
		if (ci != null) 
		{
			if(!name.equals(oldName) && culturalInstitutionService.exists(name))
			{
				return new ResponseEntity<Boolean>(false, HttpStatus.OK);
			}
			ci.setName(name);
			ci.setAddress(address);
			ci.setDescription(description);
			boolean success = culturalInstitutionService.save(ci);
			
			if(success) {
				ImageModel oldImageModel = imageModelService.getImageModel("cultural_institution_" + oldName);
				
				if(!image.isEmpty()) {
					if(oldImageModel != null) {
						if(!oldName.equals(name)) {
							oldImageModel.setName("cultural_institution_" + name);
						}
						try {
							oldImageModel.setPic(image.getBytes());
							imageModelService.save(oldImageModel);
						} catch (IOException e) {
							System.out.println("Image model error");
						}
						
					}
					else {
						imageModelService.saveImageinDatabase("cultural_institution_" + name, image);
					}
					
				}
				else {
					if(!oldName.equals(name) && oldImageModel != null) {
						imageModelService.removeImageFromDatabase(oldImageModel);
					}
				}
				
			}
			
			return new ResponseEntity<Boolean>(success, HttpStatus.OK);
		}
		return new ResponseEntity<Boolean>(false, HttpStatus.OK);
	}

	@RequestMapping(value = "/sys_admin/delete_cultural_institution", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> deleteCulturalInstitution(@RequestBody HashMap<String, String> hm) {
		String name = hm.get("name");
		CulturalInstitution ci = culturalInstitutionService.getCulturalInstitution(name);
		boolean success = culturalInstitutionService.delete(ci);
		if (success) {
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}
		return new ResponseEntity<Boolean>(false, HttpStatus.OK);
	}

	@RequestMapping(value = "/sys_admin/save_changed_password", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> saveSysAdminChangedPassword(@RequestBody HashMap<String, String> hm) {
		return userController.saveChangedPassword(hm);
	}

	@RequestMapping(value = "/admin_cultural_institution/get_cultural_institutions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArrayList<String>> getCulturalInstitutions() {
		ArrayList<String> culturalInstitutions = adminService.getCulturalInstitutions();

		return new ResponseEntity<ArrayList<String>>(culturalInstitutions, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/admin_cultural_institution/get_cultural_institution_for_admin", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HashMap<String, Object>> getCulturalInstitutionForAdmin() {
		HashMap<String, Object> hm = new HashMap<String, Object>();
		
		User loggedUser = userController.getLoggedUserLocalMethod();
		if(loggedUser != null) {
			if(loggedUser.getUserType() == UserType.INSTITUTION_ADMINISTRATOR) {
				CulturalInstitution ci = ((Administrator) loggedUser).getCulturalInstitution();
				if(ci != null) {
					hm.put("has", true);
					hm.put("institution", new CulturalInstitutionDTO(ci));
					return new ResponseEntity<HashMap<String, Object>>(hm, HttpStatus.OK);
				}
				
			}
		}

		hm.put("has", false);
		
		return new ResponseEntity<HashMap<String, Object>>(hm, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/admin_cultural_institution/add_new_showing", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> addNewShowing(@RequestParam("name") String name, @RequestParam("type") String type, @RequestParam("genre") String genre, @RequestParam("duration") int duration, @RequestParam("actors") String actors, @RequestParam("director") String director, @RequestParam("description") String description, @RequestParam("image") MultipartFile image)
	{
		User loggedUser = userController.getLoggedUserLocalMethod();
		
		if(loggedUser != null) {
			if(loggedUser.getUserType() == UserType.INSTITUTION_ADMINISTRATOR) {
				Administrator loggedAdministrator = (Administrator) loggedUser; 
			
				if(name.equals("") || type.equals("") || genre.equals("") || duration == 0 || actors.equals("") || director.equals("") || description.equals("")) {
					return new ResponseEntity<Boolean>(false, HttpStatus.OK);
				}
				
				//String name = hm.get("name");
				if (loggedAdministrator.getCulturalInstitution().getShowing(name) == null) 
				{
					/*String type = hm.get("type");
					String genre = hm.get("genre");
					String duration = hm.get("duration");
					String actors = hm.get("actors");
					String director = hm.get("director");
					String description = hm.get("description");*/
					ShowingType shType = ShowingType.valueOf(type);
					try
					{
						Showing showing = new Showing(name, genre, actors, director, new Integer(duration), description, shType);
						boolean success = culturalInstitutionService.save(showing, loggedAdministrator.getCulturalInstitution());
						
						if(success) {
							if(!image.isEmpty()) {
								String fileName = loggedAdministrator.getCulturalInstitution().getName() + "_" + name;
								imageModelService.saveImageinDatabase(fileName, image);
							}
							
						}
						
						return new ResponseEntity<Boolean>(success, HttpStatus.OK);
					}
					catch(Exception e)
					{
						return new ResponseEntity<Boolean>(false, HttpStatus.OK);
					}
				}
			}
			
		}
		
		return new ResponseEntity<Boolean>(false, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/admin_cultural_institution/update_showing", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> updateShowing(@RequestParam("old_name") String oldName, @RequestParam("name") String name, @RequestParam("type") String type, @RequestParam("genre") String genre, @RequestParam("duration") int duration, @RequestParam("actors") String actors, @RequestParam("director") String director, @RequestParam("description") String description, @RequestParam("image") MultipartFile image)
	{
		User loggedUser = userController.getLoggedUserLocalMethod();
		
		if(loggedUser != null) {
			if(loggedUser.getUserType() == UserType.INSTITUTION_ADMINISTRATOR) {
				Administrator loggedAdministrator = (Administrator) loggedUser; 
			
				if(oldName.equals("") || name.equals("") || type.equals("") || genre.equals("") || duration == 0 || actors.equals("") || director.equals("") || description.equals("")) {
					return new ResponseEntity<Boolean>(false, HttpStatus.OK);
				}
				
				/*String oldName = hm.get("old_name");
				String name = hm.get("name");
				String type = hm.get("type");
				String genre = hm.get("genre");
				String duration = hm.get("duration");
				//String rating = hm.get("rating");
				String actors = hm.get("actors");
				String director = hm.get("director");
				String description = hm.get("description");*/
				ShowingType shType = ShowingType.valueOf(type);
				CulturalInstitution ci = loggedAdministrator.getCulturalInstitution();
				if(ci != null) {
					Showing s = ci.getShowing(oldName);
					if (s != null) 
					{
						if(!oldName.equals(name) && loggedAdministrator.getCulturalInstitution().getShowing(name) != null)
						{
							return new ResponseEntity<Boolean>(false, HttpStatus.OK);
						}
						s.setName(name);
						try
						{
						//s.setAverageRating(new Double(rating));
						s.setDuration(new Integer(duration));
						}
						catch(Exception e)
						{
							return new ResponseEntity<Boolean>(false, HttpStatus.OK);
						}
						s.setGenre(genre);
						s.setListOfActors(actors);
						s.setNameOfDirector(director);
						s.setShortDescription(description);
						s.setType(shType);
						boolean success = culturalInstitutionService.save(s);
						
						if(success) {
							ImageModel oldImageModel = imageModelService.getImageModel(ci.getName() +"_" + oldName);
							
							if(!image.isEmpty()) {
								if(oldImageModel != null) {
									if(!oldName.equals(name)) {
										oldImageModel.setName(ci.getName() +"_" + name);
									}
									try {
										oldImageModel.setPic(image.getBytes());
										imageModelService.save(oldImageModel);
									} catch (IOException e) {
										System.out.println("Image model error");
									}
									
								}
								else {
									imageModelService.saveImageinDatabase(ci.getName() +"_" + name, image);
								}
								
							}
							else {
								if(!oldName.equals(name) && oldImageModel != null) {
									imageModelService.removeImageFromDatabase(oldImageModel);
								}
							}
							
						}
						
						return new ResponseEntity<Boolean>(success, HttpStatus.OK);
					}
				}
				
			}
		}
		return new ResponseEntity<Boolean>(false, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/admin_cultural_institution/delete_showing", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> deleteShowing(@RequestBody HashMap<String, String> hm)
	{
		User loggedUser = userController.getLoggedUserLocalMethod();
		
		if(loggedUser != null) {
			if(loggedUser.getUserType() == UserType.INSTITUTION_ADMINISTRATOR) {
				CulturalInstitution ci = ((Administrator) loggedUser).getCulturalInstitution();
				
				String idStr = hm.get("id");
				
				int idShowing;
				try {
					idShowing = Integer.parseInt(idStr);
				}
				catch(Exception e) { return new ResponseEntity<Boolean>(false, HttpStatus.OK); }
				Showing s = ci.getShowing(idShowing);
				if(s != null) {
					ci.getShowings().remove(s);
					boolean success = culturalInstitutionService.save(ci);
					if(!success) {
						return new ResponseEntity<Boolean>(false, HttpStatus.OK);
					}
					success = culturalInstitutionService.deleteShowing(s);
					return new ResponseEntity<Boolean>(success, HttpStatus.OK);
				}
				
			}
		}
		
		return new ResponseEntity<Boolean>(false, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/admin_cultural_institution/add_new_auditorium", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> addNewAuditorium(@RequestBody HashMap<String, String> hm)
	{
		User loggedUser = userController.getLoggedUserLocalMethod();
		if(loggedUser != null) {
			if(loggedUser.getUserType() == UserType.INSTITUTION_ADMINISTRATOR) {
				CulturalInstitution ci = ((Administrator) loggedUser).getCulturalInstitution();
				if(ci != null) {
					String name = hm.get("name");
					if (ci.getAuditorium(name) == null) 
					{
						String numOfRows = hm.get("numOfRows");
						String numOfCols = hm.get("numOfCols");
						try
						{
							Auditorium a = new Auditorium(name, new Integer(numOfRows), new Integer(numOfCols));		
							ci.getAuditoriums().add(a);
							boolean success = culturalInstitutionService.save(ci);
							return new ResponseEntity<Boolean>(success, HttpStatus.OK);
						}
						catch(Exception e)
						{
							return new ResponseEntity<Boolean>(false, HttpStatus.OK);
						}
					}
				}
			}
		}
					
		
		return new ResponseEntity<Boolean>(false, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/admin_cultural_institution/update_auditorium", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> updateAuditorium(@RequestBody HashMap<String, String> hm)
	{
		String oldName = hm.get("old_name");
		String name = hm.get("name");
		String numOfRows = hm.get("numOfRows");
		String numOfCols = hm.get("numOfCols");
		
		User loggedUser = userController.getLoggedUserLocalMethod();
		if(loggedUser != null) {
			if(loggedUser.getUserType() == UserType.INSTITUTION_ADMINISTRATOR) {
				CulturalInstitution ci = ((Administrator) loggedUser).getCulturalInstitution();
				if(ci != null) {
					Auditorium a = ci.getAuditorium(oldName);
					
					if (a != null) 
					{
						if(!oldName.equals(name) && ci.getAuditorium(name) != null)
						{
							return new ResponseEntity<Boolean>(false, HttpStatus.OK);
						}
						a.setName(name);
						
						try
						{
							a.setNumOfRows(new Integer(numOfRows));
							a.setNumOfCols(new Integer(numOfCols));
						}
						catch(Exception e)
						{
							return new ResponseEntity<Boolean>(false, HttpStatus.OK);
						}

						boolean success = culturalInstitutionService.saveAuditorium(a);
						return new ResponseEntity<Boolean>(success, HttpStatus.OK);
					}
				}
			}
		}
		
		return new ResponseEntity<Boolean>(false, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/admin_cultural_institution/delete_auditorium", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> deleteAuditorium(@RequestBody HashMap<String, String> hm)
	{
		User loggedUser = userController.getLoggedUserLocalMethod();
		if(loggedUser != null) {
			if(loggedUser.getUserType() == UserType.INSTITUTION_ADMINISTRATOR) {
				CulturalInstitution ci = ((Administrator) loggedUser).getCulturalInstitution();
				if(ci != null) {
					String id = hm.get("id");
					Auditorium a = ci.getAuditoriumById(id);
					ci.getAuditoriums().remove(a);
					
					boolean success = culturalInstitutionService.save(ci);
					return new ResponseEntity<Boolean>(success, HttpStatus.OK);
				}
			}
		}
		
		return new ResponseEntity<Boolean>(false, HttpStatus.OK);

	}

	@RequestMapping(value = "/admin_cultural_institution/get_data_for_line_chart", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<int[][]> getDataForLineChart(@RequestBody HashMap<String, String> hm)
	{
		int[][] returnList = null;
		String ci = hm.get("ci");
		String date = hm.get("date");
		String timePeriodType = hm.get("timePeriodType");
		LocalDate dateLocal = null;
		try {
			dateLocal = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
		} catch (Exception e) {}
		
		switch(timePeriodType)
		{
			case "DAY":
			{
				int numberOfTickets = ticketService.getNumberOfTicketsByDateAndCulturalInstitution(dateLocal, ci);
				int dayOfMonth = dateLocal.getDayOfMonth();
				int[] firstList = new int[] {numberOfTickets};
				int[] secondList = new int[] {dayOfMonth};
				returnList = new int[][] {firstList, secondList};
				break;
			}
			case "WEEK":
			{
				DayOfWeek dayOfWeek = dateLocal.getDayOfWeek();
				// set date on the beggining of the week
				while(dayOfWeek.compareTo(DayOfWeek.MONDAY) != 0)
				{
					dateLocal = dateLocal.minusDays(1);
					dayOfWeek = dateLocal.getDayOfWeek();
				}
				// now current date is monday
				int[] firstList = new int[7];
				int[] secondList = new int[7];
				for(int i=0; i<7; i++)
				{
					firstList[i] = ticketService.getNumberOfTicketsByDateAndCulturalInstitution(dateLocal, ci);
					secondList[i] = dateLocal.getDayOfMonth();
					dateLocal = dateLocal.plusDays(1);
				}
				returnList = new int[][] {firstList, secondList};
				break;
			}
			case "MONTH":
			{
				int dayOfMonth = dateLocal.getDayOfMonth();
				dateLocal = dateLocal.minusDays(dayOfMonth - 1);
				ArrayList<Integer> helpList = new ArrayList<Integer>();
				do
				{
					helpList.add(ticketService.getNumberOfTicketsByDateAndCulturalInstitution(dateLocal, ci));
					dateLocal = dateLocal.plusDays(1);
				}
				while(dateLocal.getDayOfMonth() != 1);
				int[] firstList = new int[helpList.size()];
				int[] secondList = new int[helpList.size()];
				for(int i=0; i<helpList.size(); i++)
				{
					firstList[i] = helpList.get(i);
					secondList[i] = i+1;
				}
				returnList = new int[][] {firstList, secondList};
				break;
			}
		}
		
		return new ResponseEntity<int[][]>(returnList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/admin_cultural_institution/get_income", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getIncome(@RequestBody HashMap<String, String> hm)
	{
		String ci = hm.get("ci");
		String date1 = hm.get("date1");
		String date2 = hm.get("date2");
		LocalDate dateLocal1 = null;
		try {
			dateLocal1 = LocalDate.parse(date1, DateTimeFormatter.ISO_DATE);
		} catch (Exception e) {}
		
		LocalDate dateLocal2 = null;
		try {
			dateLocal2 = LocalDate.parse(date2, DateTimeFormatter.ISO_DATE);
		} catch (Exception e) {}
		
		int income = ticketService.getIncome(ci, dateLocal1, dateLocal2);
		String Income = (new Integer(income)).toString();
		return new ResponseEntity<String>(Income, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/admin_funzone/get_showings_of_cultural_institution/{culturalInstitutionName}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArrayList<String>> getShowingsOfCulturalInstitution(
			@PathVariable("culturalInstitutionName") String culturalInstitutionName) {
		ArrayList<String> showings = adminService.getShowings(culturalInstitutionName);

		return new ResponseEntity<ArrayList<String>>(showings, HttpStatus.OK);

	}

	@RequestMapping(value = "/admin_funzone/add_requisite", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> AddRequisite(@RequestBody HashMap<String, String> hm) {
		String name = hm.get("name");
		String description = hm.get("description");
		String priceStr = hm.get("price");
		String culturalInstitutionName = hm.get("culturalInstitution");
		String showing = hm.get("showing");

		boolean successAddRequisite;
		int correct = adminService.correctRequisite(name, description, priceStr, culturalInstitutionName, showing);
		if (correct == 6) {
			CulturalInstitution cInstitution = adminService.getCulturalInstitution(culturalInstitutionName);
			double price = Double.parseDouble(priceStr);
			successAddRequisite = adminService.addRequisite(
					new Requisite(name, description, price, cInstitution.getShowing(showing), cInstitution));
		} else
			successAddRequisite = false;

		return new ResponseEntity<Boolean>(successAddRequisite, HttpStatus.OK);

	}

	@RequestMapping(value = "/admin_funzone/load_requisite/{requisiteName}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RequisiteDTO> loadRequisite(@PathVariable("requisiteName") String requisiteName) {
		Requisite requisite = adminService.getRequisite(requisiteName);
		return new ResponseEntity<RequisiteDTO>(new RequisiteDTO(requisite), HttpStatus.OK);
	}

	@RequestMapping(value = "/admin_funzone/remove_requisite", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> removeRequisite(@RequestBody HashMap<String, String> hm) {
		String name = hm.get("name");
		List<Requisite> requisites = adminService.getRequisites();
		for (Requisite req : requisites) {
			if (req.getName().equals(name)) {
				requisites.remove(req);
				adminService.deleteRequisite(req);

				return new ResponseEntity<>(true, HttpStatus.OK);
			}
		}

		return new ResponseEntity<>(false, HttpStatus.OK);
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

	@RequestMapping(value = "/admin_funzone/save_changes_requisite", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> saveChangesRequisite(@RequestBody HashMap<String, String> hm) {
		String oldName = hm.get("oldName");
		String name = hm.get("name");
		String description = hm.get("description");
		String priceStr = hm.get("price");
		String culturalInstitutionName = hm.get("culturalInstitution");
		String showing = hm.get("showing");
		
		
		int correct = adminService.correctRequisite(name, description, priceStr, culturalInstitutionName, showing);
		System.out.println(correct);
		if (correct == 6) {
			Requisite req = adminService.getRequisite(oldName);
			CulturalInstitution cInstitution = adminService.getCulturalInstitution(culturalInstitutionName);
			double price = Double.parseDouble(priceStr);
			req.setName(name);
			req.setDescription(description);
			req.setPrice(price);
			req.setCulturalInstitution(cInstitution);
			req.setShowing(cInstitution.getShowing(showing));
			adminService.saveRequisite(req);
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);

		} else {

			return new ResponseEntity<Boolean>(false, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/admin_funzone/changes_default_username_password", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> saveAdminFunZoneChangedUsernameAndPassword(@RequestBody HashMap<String, String> hm) {
		return saveChangedUsernameAndPassword(hm);
	}

	@RequestMapping(value = "/admin_cultural_institution/changes_default_username_password", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> saveCulturalInstitutionAdminChangedUsernameAndPassword(
			@RequestBody HashMap<String, String> hm) {
		return saveChangedUsernameAndPassword(hm);
	}

	@RequestMapping(value = "/sys_admin/changes_default_username_password", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> saveSysAdminChangedUsernameAndPassword(@RequestBody HashMap<String, String> hm) {
		return saveChangedUsernameAndPassword(hm);
	}

	public ResponseEntity<Boolean> saveChangedUsernameAndPassword(HashMap<String, String> hm) {
		User user = userController.getLoggedUserLocalMethod();

		if (user != null) {
			String username = hm.get("username");
			String password = hm.get("password");
			String repeatPassword = hm.get("repeatPassword");

			int correct = adminService.validChangedUsernameAndPassword(user, username, user.getPassword(), password,
					repeatPassword);
			if (correct == 0) {
				if (!repeatPassword.equals("")) {
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
