package com.jvm.isa.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jvm.isa.domain.Auditorium;
import com.jvm.isa.domain.CulturalInstitution;
import com.jvm.isa.domain.RegisteredUser;
import com.jvm.isa.domain.Showing;
import com.jvm.isa.domain.Term;
import com.jvm.isa.domain.User;
import com.jvm.isa.domain.UserStatus;
import com.jvm.isa.domain.UserType;
import com.jvm.isa.repository.AuditoriumRepository;
import com.jvm.isa.repository.TermRepository;
import com.jvm.isa.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
    private UserRepository userRepository;
	
	@Autowired
	private TermRepository termRepository;

	@Autowired
	private AuditoriumRepository auditoriumRepository;
	
	@Autowired
	private CulturalInstitutionService culturalInstitutionService;
	
	
	@Override
	public boolean registrate(User user) {
		try {
			userRepository.save(user);
		}
		catch(Exception e) {
			return false;
		}
		
		return true;
	}

	@Override
	public boolean exists(String username) {
		return userRepository.findByUsername(username) != null;
	}

	@Override
	public User getUser(String username) {
		User user = userRepository.findByUsername(username);
		return user;
	}
	
	
	@Override
	public User getUser(String username, String password) {
		User user = userRepository.findByUsernameAndPassword(username, password);
		return user;
	}
	
	
	/*
	 * return value:
	 *  0 - All fields are not filled
	 *  1 - This username already exists
	 *  2 - You did not enter the correct old password
	 *  3 - You are not the first to enter the same new password the second time
	 *  4 - Incorrect email
	 *  5 - Incorrect phone number
	 *  6 - Everything is right
	 * */
	@Override
	public int correctUser(User oldUser, String username, String oldPassword, String newPassword, String repeatNewPassword, String firstName, String lastName, String email, String city, String phoneNumber) {
		if(newPassword.equals("") ^ repeatNewPassword.equals("")) return 0; // xor, tj. jedno od polja je ostalo prazno
		
		if(username.equals("") || firstName.equals("") || lastName.equals("") || email.equals("") || city.equals("") || phoneNumber.equals("")) return 0;
		
		if(oldPassword != null) {
			if(oldPassword.equals("")) return 0;
			if(!oldUser.getPassword().equals(oldPassword)) return 2;
		}
		
		if(oldUser != null) {
			if(exists(username) && !oldUser.getUsername().equals(username)) return 1;
		}
		else {
			if(exists(username)) return 1;
		}
		
		if(!newPassword.equals(repeatNewPassword)) return 3;
		
		if(!EmailValidator.getInstance().isValid(email)) return 4;
		
		try { Integer.parseInt(phoneNumber); }
		catch(Exception e) { return 5; }
		
		return 6;
		
	}
	
	@Override
	public int correctChangepassword(User oldUser, String oldPassword, String newPassword, String repeatNewPassword) {
		if(oldPassword.equals("") || newPassword.equals("") || repeatNewPassword.equals("")) {
			return 0;
		}
		
		if(!oldUser.getPassword().equals(oldPassword)) return 1;
		
		if(!newPassword.equals(repeatNewPassword)) return 2;
		
		return 3;
	}
	
	@Override
	public ArrayList<String> getPeople(RegisteredUser ru) {
		ArrayList<String> people = new ArrayList<String>();
	
		List<User> registeredUsers = userRepository.findByUserTypeAndUserStatus(UserType.REGISTERED_USER, UserStatus.ACTIVATED);
		
		for (User user : registeredUsers) {
			if (user.equals(ru)) continue;
			
			people.add(((RegisteredUser) user).toString());
		}
		
		return people;
	}

	@Override
	public Boolean[] bookSelectedSeats(String dateStr, String timeStr, String culturalInstitutionName, String showingName, String selectedSeats) {
		String[] tokens = selectedSeats.split(",");
		
		LocalDate date = null;
		LocalTime time = null;
		
		try {
			date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
			time = LocalTime.parse(timeStr, DateTimeFormatter.ISO_TIME);
		} catch (Exception e) {
			// nijedno sediste nije uspesno rezervisano
			return unsuccessfullyBookSelectedSeats(tokens.length);
		}
		
		CulturalInstitution culturalInstitution = culturalInstitutionService.getCulturalInstitution(culturalInstitutionName);
		if(culturalInstitution == null) {
			// nijedno sediste nije uspesno rezervisano
			return unsuccessfullyBookSelectedSeats(tokens.length);
		}
		
		Showing showing = culturalInstitution.getShowing(showingName);
		if(showing == null) {
			// nijedno sediste nije uspesno rezervisano
			return unsuccessfullyBookSelectedSeats(tokens.length);
		}
		
		//Auditorium auditorium = auditoriumRepository.save(new Auditorium("Sala na Klisi", 10, 10));
		//termRepository.save(new Term(LocalDateTime.now(), auditorium, showing));
		
		Term term = termRepository.findByDateAndCulturalInstitutionAndShowing(date, culturalInstitution, showing);
		
		Boolean[] returnValue = new Boolean[tokens.length];
		
		int num;
		for (int k = 0; k < tokens.length; k++) {
			try {
				num =Integer.parseInt(tokens[k]);
			} catch (NumberFormatException e) {
				returnValue[k] = false;
				continue;
			}
			
			if(!term.getSeats()[num-1]) { // na frontendu indeksi krecu od 1
				term.getSeats()[num-1] = true;
				returnValue[k] = true;
			}
			else {
				returnValue[k] = false;
			}
		}
		
		termRepository.save(term);
		
		return returnValue;
	}
	
	public Term getTerm(String dateStr, String timeStr, String culturalInstitutionName, String showingName) {
		LocalDate date = null;
		LocalTime time = null;
		
		try {
			date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
			time = LocalTime.parse(timeStr, DateTimeFormatter.ISO_TIME);
		} catch (Exception e) {
		
		}
		
		CulturalInstitution culturalInstitution = culturalInstitutionService.getCulturalInstitution(culturalInstitutionName);
		Showing showing = culturalInstitution.getShowing(showingName);
		
		//Auditorium auditorium = auditoriumRepository.findByName("Sala 1");
		//termRepository.save(new Term(date, time, culturalInstitution, auditorium, showing));
		
		Term term = termRepository.findByDateAndCulturalInstitutionAndShowing(date, culturalInstitution, showing);
		
		return term;
	}
	
	@Override
	public ArrayList<Integer> getIndexOfBusySeatsAndRowsCols(Term term) {
		
		
		Boolean[] seats = term.getSeats();
		ArrayList<Integer> indexes = getIndexesOfBusySeats(seats);
		
		Auditorium auditorium = term.getAuditorium();
		indexes.add(auditorium.getNumOfRows());
		indexes.add(auditorium.getNumOfCols());
		
		return indexes;
	}
	
	private ArrayList<Integer> getIndexesOfBusySeats(Boolean[] seats) {
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		
		for (int i = 0; i < seats.length; i++) {
			if(seats[i]) {
				indexes.add(i+1); // na frontendu indeksi krecu od 1
			}
		}
		
		return indexes;
	}

	private Boolean[] unsuccessfullyBookSelectedSeats(int length) {
		Boolean[] returnValue = new Boolean[length];
		
		// nijedno sediste nije uspesno rezervisano
		for (int i = 0; i < returnValue.length; i++) {
			returnValue[i] = false;
		}
		
		return returnValue;
	}
	
}
