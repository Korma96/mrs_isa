package com.jvm.isa.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jvm.isa.domain.Auditorium;
import com.jvm.isa.domain.CulturalInstitution;
import com.jvm.isa.domain.CulturalInstitutionType;
import com.jvm.isa.domain.Showing;
import com.jvm.isa.repository.CulturalInstitutionRepository;
import com.jvm.isa.repository.ShowingRepository;

@Service
public class CulturalInstitutionServiceImpl implements CulturalInstitutionService
{
	@Autowired
	private CulturalInstitutionRepository  culturalInstitutionRepository;
	
	@Autowired
	private ShowingRepository  showingRepository;
	
	@Override
	public boolean exists(String name)
	{
		return culturalInstitutionRepository.findByName(name) != null;
	}
	
	@Override
	public ArrayList<Showing> getShowings()
	{
		return showingRepository.findAll();
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
	public List<Auditorium> getAuditoriums(String culturalInstitutionName) {
		CulturalInstitution ci = culturalInstitutionRepository.findByName(culturalInstitutionName);
		List<Auditorium> auditoriums = ci.getAuditoriums();
		return auditoriums;
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
	public ArrayList<CulturalInstitution> getAllCulturalInstitutions()
	{
		return (ArrayList<CulturalInstitution>) culturalInstitutionRepository.findAll();
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

	@Override
	public boolean showingExists(String name)
	{
		return showingRepository.findByName(name) != null;
	}

	@Override
	public boolean deleteShowing(String name)
	{
		try
		{
			Showing showing = showingRepository.findByName(name);
			if(showing == null)
			{
				return false;
			}
			showingRepository.delete(showing);	
			return true;
		}
		catch(Exception e)
		{
			return false;
		}		
	}

	@Override
	public boolean save(Showing sh)
	{
		try 
		{
			showingRepository.save(sh);
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	}

	@Override
	public Showing getShowing(String name)
	{
		return showingRepository.findByName(name);
	}


}
