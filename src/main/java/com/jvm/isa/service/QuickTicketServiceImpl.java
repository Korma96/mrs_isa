package com.jvm.isa.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jvm.isa.domain.CulturalInstitution;
import com.jvm.isa.domain.QuickTicket;
import com.jvm.isa.domain.Showing;
import com.jvm.isa.domain.Term;
import com.jvm.isa.repository.QuickTicketRepository;
import com.jvm.isa.repository.TermRepository;

@Service
public class QuickTicketServiceImpl implements QuickTicketService
{
	@Autowired
	TermRepository termRepository;
	
	@Autowired
	CulturalInstitutionService culturalInstitutionService;

	@Autowired
	QuickTicketRepository quickTicketRepository;
	
	@Override
	public ArrayList<QuickTicket> findByCulturalInstitutionAndShowing(String ci, String sh) {
		CulturalInstitution culturalInstitution = culturalInstitutionService.getCulturalInstitution(ci);
		Showing showing = culturalInstitution.getShowing(sh);
		ArrayList<Term> terms = (ArrayList<Term>) termRepository.findByCulturalInstitutionAndShowing(culturalInstitution, showing);
		ArrayList<QuickTicket> allQuickTickets = new ArrayList<QuickTicket>();
		for(Term t : terms)
		{
			ArrayList<QuickTicket> quickTickets = (ArrayList<QuickTicket>) quickTicketRepository.findByTerm(t);
			if(quickTickets.size() != 0)
				allQuickTickets.addAll(quickTickets);
		}
		return allQuickTickets;
	}

	@Override
	public ArrayList<String> getReservedQuickTickets(String termId) {
		// TODO Auto-generated method stub
		return null;
	}

}
