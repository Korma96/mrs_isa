package com.jvm.isa.repository;

import org.springframework.data.repository.Repository;

import com.jvm.isa.domain.User;

public interface UserRepository extends Repository<User, Long> {
	
	User findByUsername(String username);
	
	User findByUsernameAndPassword(String username, String password);
	
	User save(User user);
	
	void deleteAll();
}
