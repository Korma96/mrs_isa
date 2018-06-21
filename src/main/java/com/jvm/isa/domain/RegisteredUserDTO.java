package com.jvm.isa.domain;



public class RegisteredUserDTO extends UserDTO {
	private String firstName;
	private String lastName;
	private String email;
	private String city;
	private String phoneNumber;

	
	public RegisteredUserDTO() {
		
	}
	
	public RegisteredUserDTO(RegisteredUser ru) {
		super(ru.getUsername(), ru.getUserType(), ru.getUserStatus());
		
		this.firstName = ru.getFirstName();
		this.lastName = ru.getLastName();
		this.email = ru.getEmail();
		this.city = ru.getCity();
		this.phoneNumber = ru.getPhoneNumber();
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
}
