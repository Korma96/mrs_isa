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
public class QuickTicket {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id", unique=true, nullable=false)
	private Long id;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Term term;
	
	@Column(name="seat", unique=false, nullable=false)
	private int seat;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private RegisteredUser reservedBy;
	
	public QuickTicket() {
		
	}
	
	public QuickTicket(Term term, RegisteredUser owner, RegisteredUser reservedBy, int seat) {
		this.term = term;
		this.seat = seat;
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

	public int getSeat() {
		return seat;
	}

	public void setSeat(int seat) {
		this.seat = seat;
	}

	
}
