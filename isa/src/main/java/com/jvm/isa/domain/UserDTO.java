package com.jvm.isa.domain;


public class UserDTO {
	
	private String username;
	private UserType userType;
	private UserStatus userStatus;
	
	public UserDTO() {
		
	}
	
	public UserDTO(User user) {
		this.username = user.getUsername();
		this.userType = user.getUserType();
		this.userStatus = user.getUserStatus();
	}
	
	
	
	public UserDTO(String username, UserType userType, UserStatus userStatus) {
		this.username = username;
		this.userType = userType;
		this.userStatus = userStatus;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	public UserType getUserType() {
		return userType;
	}
	public void setUserType(UserType userType) {
		this.userType = userType;
	}
	
}
