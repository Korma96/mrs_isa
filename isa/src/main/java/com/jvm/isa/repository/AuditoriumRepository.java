package com.jvm.isa.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import com.jvm.isa.domain.Auditorium;

public interface AuditoriumRepository extends Repository<Auditorium, Long> {
	
	Auditorium save(Auditorium auditorium);
	
	Auditorium findByName(String name);

}
