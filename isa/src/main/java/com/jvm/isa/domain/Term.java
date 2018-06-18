package com.jvm.isa.domain;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Term {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id", unique=true, nullable=false)
	private Long id;
	
	//@Temporal(TemporalType.DATE)
	@Column(name="date", unique=false, nullable=false)
	private LocalDate date;
	
	@Column(name="time", unique=false, nullable=false)
	private LocalTime time;
	
	@Lob
	@Column(name="seats", unique=false, nullable=false)
	private Boolean[] seats;
	
	@Column(name="price", unique=false, nullable=false)
	private double price;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private CulturalInstitution culturalInstitution;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Auditorium auditorium;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private Showing showing;

	
	public Term() {
		
	}
	
	public Term(LocalDate date, LocalTime time, CulturalInstitution culturalInstitution,  Auditorium auditorium, Showing showing, double price) {
		this.date = date;
		this.time = time;
		this.seats = new Boolean[auditorium.getNumOfRows() * auditorium.getNumOfCols()];
		for (int i = 0; i < seats.length; i++) {
			seats[i] = false;
		}
		this.culturalInstitution = culturalInstitution;
		this.auditorium = auditorium;
		this.showing = showing;
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public LocalDate getDate() {
		return date;
	}


	public void setDate(LocalDate date) {
		this.date = date;
	}


	public LocalTime getTime() {
		return time;
	}


	public void setTime(LocalTime time) {
		this.time = time;
	}
	
	public Boolean[] getSeats() {
		return seats;
	}

	public void setSeats(Boolean[] seats) {
		this.seats = seats;
	}

	public CulturalInstitution getCulturalInstitution() {
		return culturalInstitution;
	}

	public void setCulturalInstitution(CulturalInstitution culturalInstitution) {
		this.culturalInstitution = culturalInstitution;
	}

	public Auditorium getAuditorium() {
		return auditorium;
	}

	public void setAuditorium(Auditorium auditorium) {
		this.auditorium = auditorium;
	}

	public Showing getShowing() {
		return showing;
	}

	public void setShowing(Showing showing) {
		this.showing = showing;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Term) {
			return id == ((Term) obj).id;
		}
		
		return false;
	}
	

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}
