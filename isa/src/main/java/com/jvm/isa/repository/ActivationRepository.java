package com.jvm.isa.repository;

import org.springframework.data.repository.Repository;

import com.jvm.isa.domain.Activation;
import com.jvm.isa.domain.User;

public interface ActivationRepository extends Repository<Activation, Long> {
	
	Activation findByIdForActivation(String idForActivation);
	
	Activation findByUser(User user);
	
	void delete(Activation activation);
	
	Activation save(Activation activation);
}

