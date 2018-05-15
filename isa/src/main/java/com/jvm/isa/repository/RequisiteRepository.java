package com.jvm.isa.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import com.jvm.isa.domain.Requisite;

public interface RequisiteRepository extends Repository<Requisite, Long> {
	
	Requisite save(Requisite requisite);
	
	Requisite findByName(String name);
	
	List<Requisite> findAll();
}
