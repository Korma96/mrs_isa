package com.jvm.isa.service;

import java.time.LocalDate;

import com.jvm.isa.domain.Term;
import com.jvm.isa.domain.Ticket;

public interface TicketService {

	Ticket getTicket(Term term, int seat);
	
	Ticket getTicket(String dateStr, String timeStr, String culturalInstitutionName, String showingName, int seat);
	
	Ticket save(Ticket ticket);

	int getNumberOfTicketsByDateAndCulturalInstitution(LocalDate date, String ci);

	int getIncome(String ci, LocalDate dateLocal1, LocalDate dateLocal2);
}

