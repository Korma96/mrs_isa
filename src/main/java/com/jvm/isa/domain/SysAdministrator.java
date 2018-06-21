package com.jvm.isa.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.ColumnDefault;

@Entity
// ovom anotacijom se navodi vrednost diskriminatorske kolone koja vazi za
// objekte ove klase
@DiscriminatorValue("SA")
public class SysAdministrator extends User {

	@ColumnDefault("''")
	@Column(name = "email", unique = false, nullable = false)
	private String email;

	public SysAdministrator() {
		super();
	}
	
	public SysAdministrator(String username, String password, String email) {
		super(username, password, UserType.SYS_ADMINISTRATOR, UserStatus.PENDING);

		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
