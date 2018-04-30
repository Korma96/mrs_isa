package com.jvm.isa.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;


@Entity
public class Activation {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id", unique=true, nullable=false)
	private Long id;
	
	@Column(name="id_for_activation", unique=false, nullable=false)
	private String idForActivation;
	
	@OneToOne(optional = false)
	private User user;

	public Activation() {
		
	}
	
	public Activation(String idForActivation, User user) {
		this.idForActivation = idForActivation;
		this.user = user;
	}

	public String getIdForActivation() {
		return idForActivation;
	}

	public void setIdForActivation(String idForActivation) {
		this.idForActivation = idForActivation;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
	
}
