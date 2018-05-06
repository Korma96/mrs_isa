package com.jvm.isa.domain;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Term {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id", unique=true, nullable=false)
	private Long id;
	
	//@Temporal(TemporalType.DATE)
	@Column(name="date_and_time", unique=false, nullable=false)
	private LocalDateTime dateAndTime;
	
	@Column(name="seats", unique=false, nullable=false)
	private boolean[][] seats;
	
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private Auditorium auditorium;
	
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private Showing showing;

	
	public Term() {
		
	}
	
	
	
	public Term(LocalDateTime dateAndTime, boolean[][] seats, Auditorium auditorium, Showing showing) {
		this.dateAndTime = dateAndTime;
		this.seats = seats;
		this.auditorium = auditorium;
		this.showing = showing;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public LocalDateTime getDateAndTime() {
		return dateAndTime;
	}


	public void setDateAndTime(LocalDateTime dateAndTime) {
		this.dateAndTime = dateAndTime;
	}


	public boolean[][] getSeats() {
		return seats;
	}


	public void setSeats(boolean[][] seats) {
		this.seats = seats;
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
	
	
}
