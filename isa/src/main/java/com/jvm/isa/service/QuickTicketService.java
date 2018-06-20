package com.jvm.isa.service;

import java.util.ArrayList;

import com.jvm.isa.domain.QuickTicket;

public interface QuickTicketService 
{

	ArrayList<QuickTicket> findByCulturalInstitutionAndShowing(String ci, String sh);

	ArrayList<String> getReservedQuickTickets(String termId);
	

}

