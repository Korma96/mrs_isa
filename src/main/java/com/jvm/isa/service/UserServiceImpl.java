package com.jvm.isa.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jvm.isa.domain.ImageModel;
import com.jvm.isa.domain.RegisteredUser;
import com.jvm.isa.domain.User;
import com.jvm.isa.domain.UserStatus;
import com.jvm.isa.domain.UserType;
import com.jvm.isa.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
    private UserRepository userRepository;
	
	@Autowired
	private ImageModelService imageModelService;
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	@Override
	public boolean registrate(User user) {
		try {
			user = userRepository.save(user);
			return user.getId() != null;
		}
		catch(Exception e) {
			return false;
		}
		
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	@Override
	public boolean exists(String username) {
		try {
			return userRepository.findByUsername(username) != null;
		}
		catch(Exception e) {
			return false;
		}
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	@Override
	public User getUser(String username) {
		try {
			User user = userRepository.findByUsername(username);
			return user;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	@Override
	public User getUser(String username, String password) {
		try {
			User user = userRepository.findByUsernameAndPassword(username, password);
			return user;
		}
		catch(Exception e) {
			return null;
		}
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
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
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
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	@Override
	public ArrayList<String> getPeople(RegisteredUser ru) {
		ArrayList<String> people = new ArrayList<String>();
	
		try {
			List<User> registeredUsers = userRepository.findByUserTypeAndUserStatus(UserType.REGISTERED_USER, UserStatus.ACTIVATED);
			
			for (User user : registeredUsers) {
				if (user.equals(ru)) continue;
				
				people.add(((RegisteredUser) user).toString());
			}
			
			return people;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	//@Async
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	@Override
	public void saveImageinDatabase(String fileName, InputStream is) {
		/*Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {*/

		try {
			ImageModel newImageModel = new ImageModel(fileName, IOUtils.toByteArray(is));
			imageModelService.save(newImageModel);
			System.out.println("File successfully uploaded to : " + fileName); 
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error upload image!");
		}
		
		
				
		//	}
		//});
		//thread.start();
		
	}
	
	public long computeSubtractTwoDateTime(LocalDate ld1, LocalDate ld2, LocalTime lt1, LocalTime lt2) {
		long sub = ChronoUnit.MINUTES.between(LocalDateTime.of(ld1, lt1), LocalDateTime.of(ld2, lt2));
		return sub;
		//Duration.between(ticket.getTerm().getTime(), LocalTime.now()).toMinutes()
	}
}

