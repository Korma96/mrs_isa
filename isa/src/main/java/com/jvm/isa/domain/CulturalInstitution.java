package com.jvm.isa.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
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
	
	@Column(name="type", unique=false, nullable=false)
	private CulturalInstitutionType type;
	
	@OneToMany(fetch = FetchType.LAZY/*, mappedBy = "culturalInstitution"*/)
	private List<Auditorium> auditoriums;
	
	@OneToMany(fetch = FetchType.LAZY/*, mappedBy = "culturalInstitution"*/)
	private List<Repertoire> repertoires;

	public CulturalInstitution() {
		
	}
	
	public CulturalInstitution(String name, String address, String description, List<Auditorium> auditoriums,
			List<Repertoire> repertoires) {
		this.name = name;
		this.address = address;
		this.description = description;
		this.auditoriums = auditoriums;
		this.repertoires = repertoires;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Auditorium> getAuditoriums() {
		return auditoriums;
	}

	public void setAuditoriums(List<Auditorium> auditoriums) {
		this.auditoriums = auditoriums;
	}

	public List<Repertoire> getRepertoires() {
		return repertoires;
	}

	public void setRepertoires(List<Repertoire> repertoires) {
		this.repertoires = repertoires;
	}
	
}
