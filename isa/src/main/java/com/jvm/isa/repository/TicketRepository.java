package com.jvm.isa.repository;

import org.springframework.data.repository.Repository;

import com.jvm.isa.domain.Term;
import com.jvm.isa.domain.Ticket;

public interface TicketRepository extends Repository<Ticket, Long> {
	
	Ticket findByTermAndSeat(Term term, int seat);

	Ticket save(Ticket ticket);
	
}
