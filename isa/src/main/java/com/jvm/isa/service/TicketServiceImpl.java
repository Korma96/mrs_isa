package com.jvm.isa.service;

import java.awt.List;
import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jvm.isa.domain.Term;
import com.jvm.isa.domain.Ticket;
import com.jvm.isa.repository.TicketRepository;

@Service
public class TicketServiceImpl implements TicketService {

	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
	private TermService termService;

	@Override
	public Ticket getTicket(Term term, int seat) {
		return ticketRepository.findByTermAndSeat(term, seat);
	}

	@Override
	public Ticket getTicket(String dateStr, String timeStr, String culturalInstitutionName, String showingName, int seat) {
		Term term = termService.getTerm(dateStr, timeStr, culturalInstitutionName, showingName);
		return ticketRepository.findByTermAndSeat(term, seat);
	}

	
	@Override
	public Ticket save(Ticket ticket) {
		Ticket savedTicket;
		try {
			savedTicket = ticketRepository.save(ticket);
		} catch (Exception e) {
			return null;
		}
		
		return savedTicket;
	}

	@Override
	public int getNumberOfTicketsByDateAndCulturalInstitution(LocalDate date, String ci) {
		ArrayList<Ticket> tickets = (ArrayList<Ticket>) ticketRepository.findAll();
		int counter = 0;
		for(Ticket t : tickets)
		{
			Term term = t.getTerm();
			if((term.getDate().compareTo(date) == 0) && term.getCulturalInstitution().getName().equals(ci))
			{
				counter++;
			}
		}
		return counter;
	}

	@Override
	public int getIncome(String ci, LocalDate dateLocal1, LocalDate dateLocal2) {
		ArrayList<Ticket> tickets = (ArrayList<Ticket>) ticketRepository.findAll();
		int income = 0;
		for(Ticket t : tickets)
		{
			Term term = t.getTerm();
			if((term.getDate().compareTo(dateLocal1) >= 0) && (term.getDate().compareTo(dateLocal2) <= 0) && term.getCulturalInstitution().getName().equals(ci))
			{
				income += term.getPrice();
			}
		}
		return income;
	}
	
}
