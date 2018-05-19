package com.jvm.isa.domain;


public class SysAdministratorDTO extends UserDTO {
	
	private String email;
	
	public SysAdministratorDTO() {
		
	}
	
	public SysAdministratorDTO(String username, String email, UserType type, UserStatus status) {
		super(username, type, status);
		
		this.email = email;
	}

	public SysAdministratorDTO(SysAdministrator sysAdministrator) {
		super(sysAdministrator.getUsername(), sysAdministrator.getUserType(), sysAdministrator.getUserStatus());
		
		this.email = sysAdministrator.getEmail();
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
