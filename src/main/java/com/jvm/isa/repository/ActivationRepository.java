package com.jvm.isa.repository;

import org.springframework.data.repository.Repository;

import com.jvm.isa.domain.Activation;
import com.jvm.isa.domain.RegisteredUser;

public interface ActivationRepository extends Repository<Activation, Long> {
	
	Activation findByIdForActivation(String idForActivation);
	
	Activation findByUser(RegisteredUser registeredUser);
	
	void delete(Activation activation);
	
	Activation save(Activation activation);
}

