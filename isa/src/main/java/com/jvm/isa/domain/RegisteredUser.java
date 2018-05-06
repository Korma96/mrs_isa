package com.jvm.isa.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
//ovom anotacijom se navodi vrednost diskriminatorske kolone koja vazi za 
//objekte ove klase
@DiscriminatorValue("RU")
public class RegisteredUser extends User {
	@Column(name="first_name", unique=false, nullable=false)
	private String firstName;
	
	@Column(name="last_name", unique=false, nullable=false)
	private String lastName;
	
	@Column(name="email", unique=false, nullable=false)
	private String email;
	
	@Column(name="city", unique=false, nullable=false)
	private String city;
	
	@Column(name="phone_number", unique=false, nullable=false)
	private String phoneNumber;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<RegisteredUser> requests;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<RegisteredUser> friends;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<Ad> ads;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<Offer> offerss;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<Ticket> tickets;

	public RegisteredUser() {
		
	}

	public RegisteredUser(String username, String password, String firstName, String lastName, String email, String city, String phoneNumber) {
		super(username, password, UserType.REGISTERED_USER, UserStatus.PENDING);
		
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.city = city;
		this.phoneNumber = phoneNumber;
		this.requests = new ArrayList<RegisteredUser>();
		this.friends = new ArrayList<RegisteredUser>();
		this.ads = new ArrayList<Ad>();
		this.offerss = new ArrayList<Offer>();
		this.tickets = new ArrayList<Ticket>();
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public List<RegisteredUser> getRequests() {
		return requests;
	}

	public void setRequests(List<RegisteredUser> requests) {
		this.requests = requests;
	}

	public List<RegisteredUser> getFriends() {
		return friends;
	}

	public void setFriends(List<RegisteredUser> friends) {
		this.friends = friends;
	}

	public List<Ad> getAds() {
		return ads;
	}

	public void setAds(List<Ad> ads) {
		this.ads = ads;
	}

	public List<Offer> getOfferss() {
		return offerss;
	}

	public void setOfferss(List<Offer> offerss) {
		this.offerss = offerss;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

}
