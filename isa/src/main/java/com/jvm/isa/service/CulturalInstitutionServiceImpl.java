package com.jvm.isa.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jvm.isa.domain.Auditorium;
import com.jvm.isa.domain.CulturalInstitution;
import com.jvm.isa.domain.CulturalInstitutionType;
import com.jvm.isa.domain.Showing;
import com.jvm.isa.repository.AuditoriumRepository;
import com.jvm.isa.repository.CulturalInstitutionRepository;
import com.jvm.isa.repository.ShowingRepository;

@Service
public class CulturalInstitutionServiceImpl implements CulturalInstitutionService
{
	@Autowired
	private CulturalInstitutionRepository  culturalInstitutionRepository;

	@Autowired
	private ShowingRepository showingRepository;
	
	@Autowired
	private AuditoriumRepository auditoriumRepository;

	
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	@Override
	public boolean exists(String name)
	{
		return culturalInstitutionRepository.findByName(name) != null;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	@Override
	public ArrayList<Showing> getShowings()
	{
		return (ArrayList<Showing>) showingRepository.findAll();
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
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
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
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public CulturalInstitution getCulturalInstitution(String name) {
		return culturalInstitutionRepository.findByName(name);
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
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
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	@Override
	public ArrayList<String> getShowings(String culturalInstitutionName) {
		ArrayList<String> showings = new ArrayList<String>();
		
		CulturalInstitution culturalInstitution = culturalInstitutionRepository.findByName(culturalInstitutionName);
		
		if(culturalInstitution != null) {
			for (Showing showing : culturalInstitution.getShowings()) {
				if(!showings.contains(showing.getName()))
				{
					showings.add(showing.getName());
				}
			}
		}
		
		
		return showings;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
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

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	@Override
	public boolean showingExists(CulturalInstitution culturalInstitution, String name)
	{
		return culturalInstitution.getShowing(name) != null;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
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

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	@Override
	public boolean save(Showing showing, CulturalInstitution culturalInstitution)
	{
		try 
		{
			//showing = showingRepository.save(showing);			
			culturalInstitution.getShowings().add(showing);
			culturalInstitutionRepository.save(culturalInstitution);
			
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	@Override
	public boolean save(Showing showing)
	{
		try 
		{
			showingRepository.save(showing);			
			
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

	@Override
	public boolean auditoriumExists(String name) 
	{
		return auditoriumRepository.findByName(name) != null;
	}

	@Override
	public boolean saveAuditorium(Auditorium a) {
		try 
		{
			auditoriumRepository.save(a);
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	}

	@Override
	public Auditorium getAuditorium(Long id) {
		return auditoriumRepository.findById(id);
	}
	
	@Override
	public boolean deleteAuditorium(Long id) {
		Auditorium a = auditoriumRepository.findById(id);
		try
		{
			auditoriumRepository.delete(a);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	@Override
	public boolean deleteShowing(Long id) {
		Showing s = showingRepository.findById(id);
		try
		{
			showingRepository.delete(s);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

}
