package com.jvm.isa.repository;

import org.springframework.data.repository.Repository;

import com.jvm.isa.domain.Activation;

public interface ActivationRepository extends Repository<Activation, Long> {
	
	Activation findByIdForActivation(String idForActivation);
	
	void delete(Activation activation);
	
	Activation save(Activation activation);
}

