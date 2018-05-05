package com.jvm.isa.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
//ovom anotacijom se navodi vrednost diskriminatorske kolone koja vazi za 
//objekte ove klase
@DiscriminatorValue("A")
public class CulturalInstitution{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id", unique=true, nullable=false)
	private Long id;
	
	@Column(name="name", unique=false, nullable=false)
	private String name;
	
	@Column(name="address", unique=false, nullable=false)
	private String address;
	
	@Column(name="description", unique=false, nullable=false)
	private String description;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "culturalInstitution")
	private List<Auditorium> auditoriums;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "culturalInstitution")
	private List<Repertoire> repertoires;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "culturalInstitution")
	private List<Ticket> tickets;
	
}
