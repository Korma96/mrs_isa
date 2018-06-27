package com.jvm.isa.domain;


public class TicketDTO {
	
	private String showing;
	private String culturalInstitution;
	private String date;
	private String time;
	private String auditorium;
	private int seat;
	private double price;
	private String reservedBy; 
	private String reservedDateAndTime;
	
	public TicketDTO() {
		
	}
	
	public TicketDTO(Ticket ticket) {
		this.showing = ticket.getTerm().getShowing().getName();
		this.culturalInstitution = ticket.getTerm().getCulturalInstitution().getName();
		this.date = ticket.getTerm().getDate().toString();
		this.time = ticket.getTerm().getTime().toString();
		this.auditorium = ticket.getTerm().getAuditorium().getName();
		this.seat = ticket.getSeat() + 1;
		this.price = ticket.getTerm().getPrice();
		this.reservedBy = ticket.getReservedBy().getFirstName() + " " + ticket.getReservedBy().getLastName();
		this.reservedDateAndTime = ticket.getReservedDateAndTime().toString();
	}

	public String getShowing() {
		return showing;
	}

	public void setShowing(String showing) {
		this.showing = showing;
	}

	public String getCulturalInstitution() {
		return culturalInstitution;
	}

	public void setCulturalInstitution(String culturalInstitution) {
		this.culturalInstitution = culturalInstitution;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getAuditorium() {
		return auditorium;
	}

	public void setAuditorium(String auditorium) {
		this.auditorium = auditorium;
	}

	public int getSeat() {
		return seat;
	}

	public void setSeat(int seat) {
		this.seat = seat;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getReservedBy() {
		return reservedBy;
	}

	public void setReservedBy(String reservedBy) {
		this.reservedBy = reservedBy;
	}

	public String getReservedDateAndTime() {
		return reservedDateAndTime;
	}

	public void setReservedDateAndTime(String reservedDateAndTime) {
		this.reservedDateAndTime = reservedDateAndTime;
	}
	
}
