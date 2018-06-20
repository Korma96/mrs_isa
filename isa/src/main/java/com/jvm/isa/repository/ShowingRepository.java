package com.jvm.isa.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import com.jvm.isa.domain.Showing;
import com.jvm.isa.domain.ShowingType;

public interface ShowingRepository  extends Repository<Showing, Long>
{	

	List<Showing> findByType(ShowingType type);
	
	List<Showing> findAll();

	Showing findByName(String name);
	
	Showing findById(Long id);
	
	Showing save(Showing s);
	
	void delete(Showing s);
}
