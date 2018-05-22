package com.jvm.isa.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jvm.isa.domain.CulturalInstitution;
import com.jvm.isa.domain.CulturalInstitutionType;
import com.jvm.isa.domain.Showing;
import com.jvm.isa.repository.CulturalInstitutionRepository;

@Service
public class CulturalInstitutionServiceImpl implements CulturalInstitutionService
{
	@Autowired
	private CulturalInstitutionRepository  culturalInstitutionRepository;
	
	@Override
	public boolean exists(String name)
	{
		return culturalInstitutionRepository.findByName(name) != null;
	}
	
	@Override
	public boolean save(CulturalInstitution ci)
	{
		try 
		{
			culturalInstitutionRepository.save(ci);
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	}
	
	@Override
	public List<CulturalInstitution> getCulturalInstitutionsByType(CulturalInstitutionType type) 
	{
		return this.culturalInstitutionRepository.findByType(type);
	} 	
	

	public CulturalInstitution getCulturalInstitution(String name) {
		return culturalInstitutionRepository.findByName(name);
	}
	
	@Override
	public ArrayList<String> getCulturalInstitutions() {
		ArrayList<String> culturalInstitutions = new ArrayList<String>();
		
		/*ArrayList<Showing> showings =  new ArrayList<Showing>();
		showings.add(new Showing("Rambo 1", "aaaa", "qwqwqwqw", "xcxcx", 150, "wqwqw", 5, "ssdsds"));
		showings.add(new Showing("Rambo 2", "aaaa", "qwqwqwqw", "xcxcx", 150, "wqwqw", 5, "ssdsds"));
		showings.add(new Showing("Rambo 3", "aaaa", "qwqwqwqw", "xcxcx", 150, "wqwqw", 5, "ssdsds"));
		CulturalInstitution c = new CulturalInstitution("SNP", "AAA", "BBB", CulturalInstitutionType.THEATER, showings, new ArrayList<Auditorium>(), new ArrayList<Repertoire>());
		culturalInstitutionRepository.save(c);
		showings =  new ArrayList<Showing>();
		showings.add(new Showing("Roki 1", "aaaa", "qwqwqwqw", "xcxcx", 150, "wqwqw", 5, "ssdsds"));
		showings.add(new Showing("Roki 2", "aaaa", "qwqwqwqw", "xcxcx", 150, "wqwqw", 5, "ssdsds"));
		showings.add(new Showing("Roki 3", "aaaa", "qwqwqwqw", "xcxcx", 150, "wqwqw", 5, "ssdsds"));
		c = new CulturalInstitution("Jugoslovensko dramsko", "AAA", "BBB", CulturalInstitutionType.THEATER, showings, new ArrayList<Auditorium>(), new ArrayList<Repertoire>());
		culturalInstitutionRepository.save(c);
		showings =  new ArrayList<Showing>();
		showings.add(new Showing("Robocop 1", "aaaa", "qwqwqwqw", "xcxcx", 150, "wqwqw", 5, "ssdsds"));
		showings.add(new Showing("Robocop 2", "aaaa", "qwqwqwqw", "xcxcx", 150, "wqwqw", 5, "ssdsds"));
		showings.add(new Showing("Robocop 3", "aaaa", "qwqwqwqw", "xcxcx", 150, "wqwqw", 5, "ssdsds"));
		c = new CulturalInstitution("Atelje 212", "AAA", "BBB", CulturalInstitutionType.THEATER, showings, new ArrayList<Auditorium>(), new ArrayList<Repertoire>());
		culturalInstitutionRepository.save(c);*/
		
		List<CulturalInstitution> cs = culturalInstitutionRepository.findAll();
		
		for (CulturalInstitution culturalInstitution : cs) {
			culturalInstitutions.add(culturalInstitution.getName());
		}
		
		return culturalInstitutions;
	}
	
	@Override
	public ArrayList<String> getShowings(String culturalInstitutionName) {
		ArrayList<String> showings = new ArrayList<String>();
		
		CulturalInstitution culturalInstitution = culturalInstitutionRepository.findByName(culturalInstitutionName);
		
		if(culturalInstitution != null) {
			for (Showing showing : culturalInstitution.getShowings()) {
				showings.add(showing.getName());
			}
		}
		
		
		return showings;
	}
	
	@Override
	public boolean delete(CulturalInstitution ci)
	{
		try
		{
			culturalInstitutionRepository.delete(ci);	
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
		
	}


}
