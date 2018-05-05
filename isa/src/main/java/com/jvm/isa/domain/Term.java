package com.jvm.isa.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Term {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id", unique=true, nullable=false)
	private Long id;
	
	@OneToOne(optional = false)
	private Auditorium auditorium;
	
	@OneToOne(optional = false)
	private Show show;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "term")
	private List<Ticket> tickets;
}
