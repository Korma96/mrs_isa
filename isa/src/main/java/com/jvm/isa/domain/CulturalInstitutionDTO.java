package com.jvm.isa.domain;

public class CulturalInstitutionDTO {
	
	private String name;
	private String address;
	private String description;
	private String type;
	
	public CulturalInstitutionDTO(CulturalInstitution ci) {
		this.name = ci.getName();
		this.address = ci.getAddress();
		this.description = ci.getDescription();
		this.type = ci.getType().toString();
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
