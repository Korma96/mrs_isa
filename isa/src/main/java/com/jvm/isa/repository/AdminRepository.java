package com.jvm.isa.repository;

import org.springframework.data.repository.Repository;

import com.jvm.isa.domain.Administrator;

public interface AdminRepository extends Repository<Administrator, Long>{
	
	Administrator findByUsername(String username);
	
	Administrator findByUsernameAndPassword(String username, String password);
	
	Administrator save(Administrator admin);
	
	void deleteAll();

}
