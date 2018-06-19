package com.jvm.isa.service;

import java.util.HashMap;

import com.jvm.isa.domain.RegisteredUser;
import com.jvm.isa.domain.Term;
import com.jvm.isa.domain.Ticket;

public interface TicketService {

	Ticket getTicket(Term term, int seat);
	
	Ticket getTicket(String dateStr, String timeStr, String culturalInstitutionName, String showingName, String auditoriumName, int seat);
	
	Ticket save(Ticket ticket);
	
	HashMap<String, Object> getVisitedAndUnvisitedCulturalInstitutions(RegisteredUser registeredUser);
}
