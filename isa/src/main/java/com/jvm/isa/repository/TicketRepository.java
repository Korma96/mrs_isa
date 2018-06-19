package com.jvm.isa.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import com.jvm.isa.domain.RegisteredUser;
import com.jvm.isa.domain.Term;
import com.jvm.isa.domain.Ticket;

public interface TicketRepository extends Repository<Ticket, Long> {
	
	Ticket findByTermAndSeat(Term term, int seat);
	
	List<Ticket> findByOwner(RegisteredUser owner);

	Ticket save(Ticket ticket);
	
}
