package com.jvm.isa.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL/*, mappedBy = "culturalInstitution"*/)
	private List<Auditorium> auditoriums;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL/*, mappedBy = "repertoire"*/)
	private List<Showing> showings;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Ticket> tickets;
	
	//@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL/*, mappedBy = "culturalInstitution"*/)
	//private List<Repertoire> repertoires;


	
	public CulturalInstitution() {
		
	}
	
	public CulturalInstitution(String name, String address, String description, CulturalInstitutionType type, List<Showing> showings, List<Auditorium> auditoriums/*, List<Repertoire> repertoires*/) {
		this.name = name;
		this.address = address;
		this.description = description;
		this.type = type;
		this.auditoriums = auditoriums;
		this.showings = showings;
		this.tickets = new ArrayList<Ticket>();
		//this.repertoires = repertoires;
	}
	
	public CulturalInstitution(String name, String address, String description, CulturalInstitutionType type)
	{
		this.name = name;
		this.address = address;
		this.description = description;
		this.type = type;
		this.auditoriums = new ArrayList<Auditorium>();
		this.showings = new ArrayList<Showing>();
		this.tickets = new ArrayList<Ticket>();
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

	public CulturalInstitutionType getType() {
		return type;
	}

	public void setType(CulturalInstitutionType type) {
		this.type = type;
	}

	public List<Showing> getShowings() {
		return showings;
	}

	public void setShowings(List<Showing> showings) {
		this.showings = showings;
	}
	
	

	/*public List<Repertoire> getRepertoires() {
		return repertoires;
	}

	public void setRepertoires(List<Repertoire> repertoires) {
		this.repertoires = repertoires;
	}*/

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	public boolean containsShowing(String showingName) {
		for (Showing showing : showings) {
			if (showing.getName().equals(showingName)) {
				return true;
			}
		}
		
		return false;
	}
	
	public Ticket getTicket(Term term, int seat) {
		for (Ticket ticket : tickets) {
			if (ticket.getTerm().equals(term) && ticket.getSeat() == seat) {
				return ticket;
			}
		}
		
		return null;
	}
	
	public Showing getShowing(String showingName) {
		for (Showing showing : showings) {
			if (showing.getName().equals(showingName)) {
				return showing;
			}
		}
		
		return null;
	}
	
	public Auditorium getAuditorium(String auditoriumName) {
		for (Auditorium auditorium : auditoriums) {
			if (auditorium.getName().equals(auditoriumName)) {
				return auditorium;
			}
		}
		
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		
		if(obj instanceof CulturalInstitution) {
			CulturalInstitution ci = (CulturalInstitution) obj;
			return ci.name.equals(name);
		}
		
		return false;
	}
	
}
