package com.jvm.isa.service;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jvm.isa.domain.CulturalInstitution;
import com.jvm.isa.domain.CulturalInstitutionDTO;
import com.jvm.isa.domain.RegisteredUser;
import com.jvm.isa.domain.Term;
import com.jvm.isa.domain.Ticket;
import com.jvm.isa.repository.TicketRepository;

@Service
public class TicketServiceImpl implements TicketService {

	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
	private TermService termService;
	
	@Autowired
	private CulturalInstitutionService culturalInstitutionService;

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	@Override
	public Ticket getTicket(Term term, int seat) {
		try {
			return ticketRepository.findByTermAndSeat(term, seat);
		}
		catch (Exception e) {
			return null;
		}
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	@Override
	public Ticket getTicket(String dateStr, String timeStr, String culturalInstitutionName, String showingName, String auditoriumName, int seat) {
		Term term = termService.getTerm(dateStr, timeStr, culturalInstitutionName, showingName, auditoriumName);
		if(term == null) return null;
		
		return ticketRepository.findByTermAndSeat(term, seat);
	}


	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	@Override
	public Ticket save(Ticket ticket) {
		Ticket savedTicket;
		try {
			savedTicket = ticketRepository.save(ticket);
			return savedTicket;
			
		} catch (Exception e) {
			return null;
		}
		
		
	}

	@Override
	public int getNumberOfTicketsByDateAndCulturalInstitution(LocalDate date, String ci) {
		try {
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
		catch (Exception e) {
				return 0;
		}
	}

	@Override
	public int getIncome(String ci, LocalDate dateLocal1, LocalDate dateLocal2) {
		try {
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
		 catch (Exception e) {
				return 0;
			}
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	@Override
	public HashMap<String, Object> getVisitedAndUnvisitedCulturalInstitutions(RegisteredUser registeredUser) {
	
		ArrayList<CulturalInstitutionDTO> visitedCulturalInstitutions = new ArrayList<CulturalInstitutionDTO>();
		ArrayList<CulturalInstitutionDTO> unvisitedCulturalInstitutions = new ArrayList<CulturalInstitutionDTO>();
		
		try {
			List<Ticket> tickets = ticketRepository.findByOwner(registeredUser);
			ArrayList<CulturalInstitution> culturalInstitutions = culturalInstitutionService.getAllCulturalInstitutions();
			
			Term term;
			boolean visited;
			
			for (CulturalInstitution culturalInstitution : culturalInstitutions) {
				visited = false;
				
				for (Ticket ticket : tickets) {
					term = ticket.getTerm();
					if(culturalInstitution.equals(term.getCulturalInstitution())) {
						if(term.getTime().compareTo(LocalTime.now()) < 0) {
							visited = true;
							break;
						}
					}
					
				}
				
				if(visited) {
					visitedCulturalInstitutions.add(new CulturalInstitutionDTO(culturalInstitution));
				}
				else {
					unvisitedCulturalInstitutions.add(new CulturalInstitutionDTO(culturalInstitution));
				}
			}

			HashMap<String, Object> hm = new HashMap<>();
			hm.put("visited", visitedCulturalInstitutions);
			hm.put("unvisited", unvisitedCulturalInstitutions);
			
			return hm;
		}
		catch (Exception e) {
				return null;
		}
			
		
	}

	
}
