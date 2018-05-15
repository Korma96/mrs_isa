package com.jvm.isa.domain;

public class AdministratorDTO extends UserDTO {
	
	private String firstName;
	private String lastName;
	private String email;
	
	public AdministratorDTO() {
		
	}
	
	public AdministratorDTO(String username, String firstName, String lastName, String email, UserType type, UserStatus status) {
		super(username, type, status);
		
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public AdministratorDTO(Administrator administrator) {
		super(administrator.getUsername(), administrator.getUserType(), administrator.getUserStatus());
		
		this.firstName = administrator.getFirstName();
		this.lastName = administrator.getLastName();
		this.email = administrator.getEmail();
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
