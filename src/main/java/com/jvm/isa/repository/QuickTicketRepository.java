package com.jvm.isa.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import com.jvm.isa.domain.QuickTicket;
import com.jvm.isa.domain.Term;
import com.jvm.isa.domain.Ticket;

public interface QuickTicketRepository extends Repository<QuickTicket, Long> {
	
	List<QuickTicket> findByTerm(Term term);
	
	List<QuickTicket> findById(Long id);

	QuickTicket save(QuickTicket ticket);
	
	List<QuickTicket> findAll();
	
}