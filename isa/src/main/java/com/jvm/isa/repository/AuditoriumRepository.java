package com.jvm.isa.repository;

import org.springframework.data.repository.Repository;

import com.jvm.isa.domain.Auditorium;

public interface AuditoriumRepository extends Repository<Auditorium, Long> {
	
	Auditorium save(Auditorium auditorium);
	
	Auditorium findByName(String name);
	
	void delete(Auditorium a);

	Auditorium findById(Long id);

}
