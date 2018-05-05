package com.jvm.isa.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
//ovom anotacijom se navodi vrednost diskriminatorske kolone koja vazi za 
//objekte ove klase
@DiscriminatorValue("AD")
public class Administrator extends User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id", unique=true, nullable=false)
	private Long id;
	
	@Column(name="first_name", unique=false, nullable=false)
	private String firstName;
	
	@Column(name="last_name", unique=false, nullable=false)
	private String lastName;
	
	@Column(name="email", unique=false, nullable=false)
	private String email;
	
	public Administrator(String username, String password, String firstName, String lastName, String email, UserType type, UserStatus status) {
		super(username, password, type, status);
		
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
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
	
	
}
