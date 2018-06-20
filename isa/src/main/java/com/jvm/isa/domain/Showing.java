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
import javax.persistence.OneToMany;

@Entity
public class Showing {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id", unique=true, nullable=false)
	private Long id;
	
	@Column(name="name", unique=false, nullable=false)
	private String name;
	
	@Column(name="genre", unique=false, nullable=false)
	private String genre;
	
	@Column(name="list_of_actors", unique=false, nullable=false)
	private String listOfActors;
	
	@Column(name="name_of_director", unique=false, nullable=false)
	private String nameOfDirector;
	
	@Column(name="duration", unique=false, nullable=false)
	private int duration;
	
	//@Column(name="poster", unique=false, nullable=false)
	//private String poster;
	
	@Column(name="average_rating", unique=false, nullable=false)
	private double averageRating;
	
	@Column(name="short_description", unique=false, nullable=false)
	private String shortDescription;
	
	@Column(name="type", unique=false, nullable=false)
	private ShowingType type;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<Term> terms;

	
	public Showing() {
		
	}


	public Showing(String name, String genre, String listOfActors, String nameOfDirector, int duration,
			/*String poster,*/ double averageRating, String shortDescription, ShowingType type) {
	
		this.name = name;
		this.genre = genre;
		this.listOfActors = listOfActors;
		this.nameOfDirector = nameOfDirector;
		this.duration = duration;
		//this.poster = poster;
		this.averageRating = averageRating;
		this.shortDescription = shortDescription;
		this.type = type;
		this.terms = new ArrayList<Term>();
	}

	public Showing(String name, String genre, String listOfActors, String nameOfDirector, int duration,
			/*String poster,*/ String shortDescription, ShowingType type) {
	
		this.name = name;
		this.genre = genre;
		this.listOfActors = listOfActors;
		this.nameOfDirector = nameOfDirector;
		this.duration = duration;
		//this.poster = poster;
		this.shortDescription = shortDescription;
		this.type = type;
		this.terms = new ArrayList<Term>();
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


	public String getGenre() {
		return genre;
	}


	public void setGenre(String genre) {
		this.genre = genre;
	}


	public String getListOfActors() {
		return listOfActors;
	}


	public void setListOfActors(String listOfActors) {
		this.listOfActors = listOfActors;
	}


	public String getNameOfDirector() {
		return nameOfDirector;
	}


	public void setNameOfDirector(String nameOfDirector) {
		this.nameOfDirector = nameOfDirector;
	}


	public int getDuration() {
		return duration;
	}


	public void setDuration(int duration) {
		this.duration = duration;
	}


	/*public String getPoster() {
		return poster;
	}


	public void setPoster(String poster) {
		this.poster = poster;
	}*/


	public double getAverageRating() {
		return averageRating;
	}


	public void setAverageRating(double averageRating) {
		this.averageRating = averageRating;
	}


	public String getShortDescription() {
		return shortDescription;
	}


	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}


	public List<Term> getTerms() {
		return terms;
	}


	public void setTerms(List<Term> terms) {
		this.terms = terms;
	}


	public ShowingType getType() {
		return type;
	}


	public void setType(ShowingType type) {
		this.type = type;
	}
	
	
	
}
