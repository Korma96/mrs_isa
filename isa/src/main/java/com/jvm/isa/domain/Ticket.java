package com.jvm.isa.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Ticket {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id", unique=true, nullable=false)
	private Long id;
	
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private Term term;
	
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private CulturalInstitution culturalInstitution;

	public Ticket() {
		
	}
	
	public Ticket(Term term, CulturalInstitution culturalInstitution) {
		this.term = term;
		this.culturalInstitution = culturalInstitution;
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

	public CulturalInstitution getCulturalInstitution() {
		return culturalInstitution;
	}

	public void setCulturalInstitution(CulturalInstitution culturalInstitution) {
		this.culturalInstitution = culturalInstitution;
	}
	
}
