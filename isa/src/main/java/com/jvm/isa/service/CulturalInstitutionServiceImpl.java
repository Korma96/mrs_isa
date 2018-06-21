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
		try {
			return culturalInstitutionRepository.findByName(name) != null;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	@Override
	public ArrayList<Showing> getShowings()
	{
		try {
			return (ArrayList<Showing>) showingRepository.findAll();
		}
		catch(Exception e) {
			return null;
		}
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
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
		try {
			CulturalInstitution ci = culturalInstitutionRepository.findByName(culturalInstitutionName);
			if(ci == null) return null;
			
			List<Auditorium> auditoriums = ci.getAuditoriums();
			return auditoriums;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	@Override
	public List<CulturalInstitution> getCulturalInstitutionsByType(CulturalInstitutionType type) 
	{
		try {
			return this.culturalInstitutionRepository.findByType(type);
		}
		catch(Exception e) {
			return null;
		}
	} 	
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public CulturalInstitution getCulturalInstitution(String name) {
		try {
			return culturalInstitutionRepository.findByName(name);
		}
		catch(Exception e) {
			return null;
		}
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	@Override
	public ArrayList<CulturalInstitution> getAllCulturalInstitutions()
	{
		try {
			return (ArrayList<CulturalInstitution>) culturalInstitutionRepository.findAll();
		}
		catch(Exception e) {
			return null;
		}
		
	}
	
	@Override
	public ArrayList<String> getCulturalInstitutions() {
		ArrayList<String> culturalInstitutions = new ArrayList<String>();
		
		try {
			List<CulturalInstitution> cs = culturalInstitutionRepository.findAll();
			
			for (CulturalInstitution culturalInstitution : cs) {
				culturalInstitutions.add(culturalInstitution.getName());
			}
			
			return culturalInstitutions;
		}
		catch(Exception e) {
			return null;
		}
		
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	@Override
	public ArrayList<String> getShowings(String culturalInstitutionName) {
		ArrayList<String> showings = new ArrayList<String>();
		
		try {
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
		catch(Exception e) {
			return null;
		}
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
		try {
			return culturalInstitution.getShowing(name) != null;
		}
		catch(Exception e) {
			return false;
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	@Override
	public boolean deleteShowing(Showing s)
	{
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

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	@Override
	public boolean save(Showing showing, CulturalInstitution culturalInstitution)
	{
		try 
		{
			showing = showingRepository.save(showing);			
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
		try {
			return showingRepository.findByName(name);
		}
		catch(Exception e) {
			return null;
		}
	}

	@Override
	public boolean auditoriumExists(String name) 
	{
		try {
			return auditoriumRepository.findByName(name) != null;
		}
		catch(Exception e) {
			return false;
		}
	}

	@Override
	public boolean saveAuditorium(Auditorium a) {
		try 
		{
			auditoriumRepository.save(a);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	
	}

	@Override
	public Auditorium getAuditorium(String oldName) {
		try {
			return auditoriumRepository.findByName(oldName);
		}
		catch(Exception e) {
			return null;
		}
	}
	@Override
	public boolean deleteAuditorium(String name) {
		Auditorium a = auditoriumRepository.findByName(name);
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

}
