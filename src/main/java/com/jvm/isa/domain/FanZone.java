package com.jvm.isa.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class FanZone {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id", unique=true, nullable=false)
	private Long id;
	
	@OneToMany(fetch = FetchType.LAZY)
	private List<Requisite> requisites;
	
	@OneToMany(fetch = FetchType.LAZY)
	private List<Ad> ads;

	public FanZone() {
		this.requisites = new ArrayList<Requisite>();
		this.ads = new ArrayList<Ad>();
	}
	
	public FanZone(List<Requisite> requisites, List<Ad> ads) {
		this.requisites = requisites;
		this.ads = ads;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Requisite> getRequisites() {
		return requisites;
	}

	public void setRequisites(List<Requisite> requisites) {
		this.requisites = requisites;
	}

	public List<Ad> getAds() {
		return ads;
	}

	public void setAds(List<Ad> ads) {
		this.ads = ads;
	}
	
}
