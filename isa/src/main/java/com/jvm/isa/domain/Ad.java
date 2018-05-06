package com.jvm.isa.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Ad {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id", unique=true, nullable=false)
	private Long id;
	
	//@Temporal(TemporalType.DATE)
	@Column(name="date_of_activity", unique=false, nullable=false)
	private LocalDateTime dateOfActivity;
	
	@OneToOne(fetch = FetchType.LAZY)
	private Requisite requisite;
	
	@OneToOne(fetch = FetchType.LAZY)
	private RegisteredUser publisher;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<Offer> offers;
	
	public Ad() {
		
	}
	
	public Ad(Requisite requisite, RegisteredUser publisher) {
		this.dateOfActivity = LocalDateTime.now();
		this.requisite = requisite;
		this.publisher = publisher;
		this.offers = new ArrayList<Offer>();
	}
	
	public Ad(String name, String description, LocalDateTime dateOfActivity, Requisite requisite, RegisteredUser publisher,
			List<Offer> offers) {
		
		this.dateOfActivity = dateOfActivity;
		this.requisite = requisite;
		this.publisher = publisher;
		this.offers = offers;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getDateOfActivity() {
		return dateOfActivity;
	}

	public void setDateOfActivity(LocalDateTime dateOfActivity) {
		this.dateOfActivity = dateOfActivity;
	}

	public Requisite getRequisite() {
		return requisite;
	}

	public void setRequisite(Requisite requisite) {
		this.requisite = requisite;
	}

	public RegisteredUser getPublisher() {
		return publisher;
	}

	public void setPublisher(RegisteredUser publisher) {
		this.publisher = publisher;
	}

	public List<Offer> getOffers() {
		return offers;
	}

	public void setOffers(List<Offer> offers) {
		this.offers = offers;
	}
	
	
	
}
