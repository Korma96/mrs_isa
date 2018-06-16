package com.jvm.isa.repository;

import java.util.ArrayList;

import org.springframework.data.repository.Repository;

import com.jvm.isa.domain.Showing;
import com.jvm.isa.domain.ShowingType;

public interface ShowingRepository  extends Repository<Showing, Long>
{	

	ArrayList<Showing> findByShowingType(ShowingType type);
	
	ArrayList<Showing> findAll();

	Showing findByName(String name);
	
	Showing save(Showing s);
	
	void delete(Showing s);
}
