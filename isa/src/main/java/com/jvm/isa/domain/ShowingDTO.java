package com.jvm.isa.domain;

import javax.persistence.Column;

public class ShowingDTO {
	private String name;
	private String genre;
	private String listOfActors;
	private String nameOfDirector;
	private int duration;
	private double averageRating;
	private String shortDescription;
	private String type;
	
	public ShowingDTO() {
		
	}
	
	public ShowingDTO(Showing showing) {
		this.name = showing.getName();
		this.genre = showing.getGenre();
		this.listOfActors = showing.getListOfActors();
		this.nameOfDirector = showing.getNameOfDirector();
		this.duration = showing.getDuration();
		this.averageRating = showing.getAverageRating();
		this.shortDescription = showing.getShortDescription();
		this.type = showing.getType().toString();
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
