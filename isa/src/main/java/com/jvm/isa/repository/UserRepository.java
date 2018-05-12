package com.jvm.isa.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import com.jvm.isa.domain.User;
import com.jvm.isa.domain.UserStatus;
import com.jvm.isa.domain.UserType;

public interface UserRepository extends Repository<User, Long> {
	
	User findByUsername(String username);
	
	User findByUsernameAndPassword(String username, String password);
	
	List<User> findByUserTypeAndUserStatus(UserType userType, UserStatus userStatus);
	
	User findByUsernameAndUserTypeAndUserStatus(String username, UserType userType, UserStatus userStatus);
	
	User save(User user);
	
	void deleteAll();
}
