package com.jvm.isa.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Requisite {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id", unique=true, nullable=false)
	private Long id;
	
	@Column(name="name", unique=false, nullable=false)
	private String name;
	
	@Column(name="description", unique=false, nullable=false)
	private String description;
	
	@Column(name="price", unique=false, nullable=false)
	private double price;
	
	@OneToOne(fetch = FetchType.LAZY)
	private Showing showing;
	
	@OneToOne(fetch = FetchType.LAZY)
	private CulturalInstitution culturalInstitution;

	public Requisite() {
		
	}
	
	public Requisite(String name, String description, double price, Showing showing, CulturalInstitution culturalInstitution) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.showing = showing;
		this.culturalInstitution = culturalInstitution;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Showing getShowing() {
		return showing;
	}

	public void setShowing(Showing showing) {
		this.showing = showing;
	}
	
	public CulturalInstitution getCulturalInstitution() {
		return culturalInstitution;
	}

	public void setCulturalInstitution(CulturalInstitution culturalInstitution) {
		this.culturalInstitution = culturalInstitution;
	}
	
}
