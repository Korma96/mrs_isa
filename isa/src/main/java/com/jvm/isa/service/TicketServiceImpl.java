package com.jvm.isa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jvm.isa.domain.Term;
import com.jvm.isa.domain.Ticket;
import com.jvm.isa.repository.TicketRepository;

@Service
public class TicketServiceImpl implements TicketService {

	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
	private TermService termService;

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	@Override
	public Ticket getTicket(Term term, int seat) {
		return ticketRepository.findByTermAndSeat(term, seat);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	@Override
	public Ticket getTicket(String dateStr, String timeStr, String culturalInstitutionName, String showingName, int seat) {
		Term term = termService.getTerm(dateStr, timeStr, culturalInstitutionName, showingName);
		return ticketRepository.findByTermAndSeat(term, seat);
	}


	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
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
	
}
