package com.jvm.isa.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
//ovom anotacijom se navodi vrednost diskriminatorske kolone koja vazi za 
//objekte ove klase
public class Auditorium {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id", unique=true, nullable=false)
	private Long id;
	
	@Column(name="name", unique=false, nullable=false)
	private String name;
	
	@Column(name="num_Of_seats", unique=false, nullable=false)
	private int numOfSeats;
	
	@ManyToOne(optional = false)
	private CulturalInstitution culturalInstitution;
	

}
