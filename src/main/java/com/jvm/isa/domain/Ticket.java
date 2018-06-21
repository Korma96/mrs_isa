package com.jvm.isa.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Ticket {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id", unique=true, nullable=false)
	private Long id;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Term term;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private RegisteredUser owner;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private RegisteredUser reservedBy;
	
	@Column(name="reserved_date_and_time", unique=false, nullable=false)
	private LocalDateTime reservedDateTime;
	
	@Column(name="seat", unique=false, nullable=false)
	private int seat;
	
	public Ticket() {
		
	}
	
	public Ticket(Term term, RegisteredUser owner, RegisteredUser reservedBy, int seat) {
		this.term = term;
		this.owner = owner;
		this.reservedBy = reservedBy;
		this.seat = seat;
		this.reservedDateTime = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Term getTerm() {
		return term;
	}

	public void setTerm(Term term) {
		this.term = term;
	}

	public RegisteredUser getOwner() {
		return owner;
	}

	public void setOwner(RegisteredUser owner) {
		this.owner = owner;
	}
	
	public RegisteredUser getReservedBy() {
		return reservedBy;
	}

	public void setReservedBy(RegisteredUser reservedBy) {
		this.reservedBy = reservedBy;
	}

	public LocalDateTime getReservedDateAndTime() {
		return reservedDateTime;
	}

	public void setReservedDateAndTime(LocalDateTime reservedDateAndTime) {
		this.reservedDateTime = reservedDateAndTime;
	}

	public int getSeat() {
		return seat;
	}

	public void setSeat(int seat) {
		this.seat = seat;
	}

	
}
