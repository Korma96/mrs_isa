package com.jvm.isa.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
//ovom anotacijom se naglasava tip mapiranja "jedna tabela po hijerarhiji"
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
//ovom anotacijom se navodi diskriminatorska kolona
@DiscriminatorColumn(name="type", discriminatorType=DiscriminatorType.STRING)
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id", unique=true, nullable=false)
	protected Long id;
	
	@Column(name="username", unique=true, nullable=false)
	protected String username;
	
	@Column(name="password", unique=false, nullable=false)
	protected String password;
	
	@Column(name="user_type", unique=false, nullable=false)
	@Enumerated(EnumType.ORDINAL)
	protected UserType userType;
	
	@Column(name="user_status", unique=false, nullable=false)
	@Enumerated(EnumType.ORDINAL)
	protected UserStatus userStatus;
	
	public User() {
		
	}
	
	public User(String username, String password, UserType type, UserStatus status) {
		this.username = username;
		this.password = password;
		this.userType = type;
		this.userStatus = status;
	}
	
	public User(String username, String password, UserType type) {
		this.username = username;
		this.password = password;
		this.userType = type;
		this.userStatus = UserStatus.ACTIVATED;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}


	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof User) {
			User user = (User) obj;
			return user.username.equals(username) && user.password.equals(password);
		}
		
		return false;
	}
}
