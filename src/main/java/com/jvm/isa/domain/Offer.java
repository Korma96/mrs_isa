package com.jvm.isa.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Offer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id", unique=true, nullable=false)
	private Long id;
	
	@Column(name="value", unique=false, nullable=false)
	private double value;
	
	//@Temporal(TemporalType.DATE)
	@Column(name="date", unique=false, nullable=false)
	private LocalDateTime date;
	
	@OneToOne(fetch = FetchType.LAZY)
	private Ad ad;
	
	@OneToOne(fetch = FetchType.LAZY)
	private RegisteredUser bidder;

	public Offer() {
		
	}
	
	public Offer(double value, Ad ad, RegisteredUser bidder) {
		this.value = value;
		this.date = LocalDateTime.now();
		this.ad = ad;
		this.bidder = bidder;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public Ad getAd() {
		return ad;
	}

	public void setAd(Ad ad) {
		this.ad = ad;
	}

	public RegisteredUser getBidder() {
		return bidder;
	}

	public void setBidder(RegisteredUser bidder) {
		this.bidder = bidder;
	}
	
}
