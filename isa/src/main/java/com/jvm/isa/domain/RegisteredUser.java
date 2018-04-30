package com.jvm.isa.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
//ovom anotacijom se navodi vrednost diskriminatorske kolone koja vazi za 
//objekte ove klase
@DiscriminatorValue("RU")
public class RegisteredUser extends User {
	@Column(name="first_name", unique=false, nullable=false)
	private String firstName;
	
	@Column(name="last_name", unique=false, nullable=false)
	private String lastName;
	
	@Column(name="email", unique=false, nullable=false)
	private String email;
	
	@Column(name="city", unique=false, nullable=false)
	private String city;
	
	@Column(name="phone_number", unique=false, nullable=false)
	private String phoneNumber;

	public RegisteredUser() {
		
	}
	
	public RegisteredUser(String username, String password, String firstName, String lastName, String email, String city, String phoneNumber) {
		super(username, password, UserType.REGISTERED_USER, false);
		
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.city = city;
		this.phoneNumber = phoneNumber;
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
