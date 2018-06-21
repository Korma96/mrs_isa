package com.jvm.isa.domain;


public class RequisiteDTO {
	private String name;
	private String description;
	private double price;
	private String showingName;
	private String culturalInstitutionName;

	public RequisiteDTO() {
		
	}
	
	public RequisiteDTO(Requisite requisite) {
		this.name = requisite.getName();
		this.description = requisite.getDescription();
		this.price = requisite.getPrice();
		this.showingName = requisite.getShowing().getName();
		this.culturalInstitutionName = requisite.getCulturalInstitution().getName();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getShowingName() {
		return showingName;
	}

	public void setShowingName(String showingName) {
		this.showingName = showingName;
	}

	public String getCulturalInstitutionName() {
		return culturalInstitutionName;
	}

	public void setCulturalInstitutionName(String culturalInstitutionName) {
		this.culturalInstitutionName = culturalInstitutionName;
	}
	
}
