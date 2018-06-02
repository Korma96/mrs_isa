package com.jvm.isa.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jvm.isa.domain.Auditorium;
import com.jvm.isa.domain.CulturalInstitution;
import com.jvm.isa.domain.RegisteredUser;
import com.jvm.isa.domain.Showing;
import com.jvm.isa.domain.Term;
import com.jvm.isa.repository.AuditoriumRepository;
import com.jvm.isa.repository.ShowingRepository;
import com.jvm.isa.repository.TermRepository;

@Service
public class TermServiceImpl implements TermService {
	
	@Autowired
	private TermRepository termRepository;
	
	@Autowired
	private CulturalInstitutionService culturalInstitutionService;
	
	@Autowired
	private ShowingRepository showingRepository;
	
	@Autowired
	private AuditoriumRepository auditoriumRepository;
	
	@Override
	public Boolean bookSelectedSeats(String dateStr, String timeStr, String culturalInstitutionName, String showingName, String selectedSeats, RegisteredUser owner) {
		String[] tokens = selectedSeats.split(",");
		
		LocalDate date = null;
		LocalTime time = null;
		
		try {
			date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
			time = LocalTime.parse(timeStr, DateTimeFormatter.ISO_TIME);
		} catch (Exception e) {
			return false;
		}
		
		CulturalInstitution culturalInstitution = culturalInstitutionService.getCulturalInstitution(culturalInstitutionName);
		if(culturalInstitution == null) {
			return false;
		}
		
		Showing showing = culturalInstitution.getShowing(showingName);
		if(showing == null) {
			return false;
		}
		
		//Auditorium auditorium = auditoriumRepository.save(new Auditorium("Sala na Klisi", 10, 10));
		//termRepository.save(new Term(LocalDateTime.now(), auditorium, showing));
		
		Term term = termRepository.findByDateAndTimeAndCulturalInstitutionAndShowing(date, time, culturalInstitution, showing);
		
		int[] nums = new int[tokens.length];
		int num;
		
		for (int k = 0; k < tokens.length; k++) {
			try {
				num =Integer.parseInt(tokens[k]);
				nums[k] = num;
			} catch (NumberFormatException e) {
				return false;
			}
			
			if(!term.getSeats()[num-1]) { // na frontendu indeksi krecu od 1
				term.getSeats()[num-1] = true;
			}
			else {
				return false;
			}
		}
		
		/*List<Ticket> tickets = culturalInstitution.getTickets();
		
		for (int i = 0; i < nums.length; i++) {
			tickets.add(new Ticket(term, owner, nums[i]));
		}*/
		
		save(term);
		//culturalInstitutionService.save(culturalInstitution);
		
		return true;
	}
	
	@Override
	public Term getTerm(String dateStr, String timeStr, String culturalInstitutionName, String showingName) {
		LocalDate date = null;
		LocalTime time = null;
		
		try {
			date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
			time = LocalTime.parse(timeStr, DateTimeFormatter.ISO_TIME);
		} catch (Exception e) {
		
		}
		
		CulturalInstitution culturalInstitution = culturalInstitutionService.getCulturalInstitution(culturalInstitutionName);
		Showing showing = culturalInstitution.getShowing(showingName);
		Term term = termRepository.findByDateAndTimeAndCulturalInstitutionAndShowing(date, time, culturalInstitution, showing);
		
		return term;
	}
	
	@Override
	public ArrayList<Integer> getIndexOfBusySeatsAndRowsCols(Term term) {
		
		
		Boolean[] seats = term.getSeats();
		ArrayList<Integer> indexes = getIndexesOfBusySeats(seats);
		
		Auditorium auditorium = term.getAuditorium();
		indexes.add(auditorium.getNumOfRows());
		indexes.add(auditorium.getNumOfCols());
		
		return indexes;
	}
	
	private ArrayList<Integer> getIndexesOfBusySeats(Boolean[] seats) {
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		
		for (int i = 0; i < seats.length; i++) {
			if(seats[i]) {
				indexes.add(i+1); // na frontendu indeksi krecu od 1
			}
		}
		
		return indexes;
	}

	
	/*
	 * return value:
	 * 0 - all right
	 * 1 - You did not put yourself
	 * 2 - there are repetitions
	*/
	@Override
	public int thereAreRepetitionsOrNotPutYourself(Collection<String> values, String loggedUsername) {
		Object[] valuesArray = values.toArray();
		
		boolean notPutYourself = true;
		
		for (int i = 0; i < valuesArray.length; i++) {
			if(((String)valuesArray[i]).equals(loggedUsername)) notPutYourself = false;
			
			for (int j = i+1; j < valuesArray.length; j++) {
				if(((String)valuesArray[i]).equals(((String)valuesArray[j])) && !((String)valuesArray[i]).equals(loggedUsername)) return 2;
			}
		}
		
		if (notPutYourself) return 1;
		
		return 0;
	}

	@Override
	public boolean save(Term term) {
		try {
			termRepository.save(term);
		} catch (Exception e) {
			return false;
		}
		
		return true;
		
	}

	@Override
	public ArrayList<String> getCulturalInstitutions() {
		List<Term> terms = termRepository.findAll();
		ArrayList<String> culturalInstitutions = new ArrayList<String>();
		
		for (Term term : terms) {
			if(!culturalInstitutions.contains(term.getCulturalInstitution().getName())) {
				culturalInstitutions.add(term.getCulturalInstitution().getName());
			}
			
		}
		
		return culturalInstitutions;
	}

	@Override
	public ArrayList<String> getShowings(String culturalInstitutionName) {
		CulturalInstitution culturalInstitution = culturalInstitutionService.getCulturalInstitution(culturalInstitutionName);
		List<Term> terms = termRepository.findByCulturalInstitution(culturalInstitution);
		ArrayList<String> showings = new ArrayList<String>();
		
		for (Term term : terms) {
			if(!showings.contains(term.getShowing().getName())) {
				showings.add(term.getShowing().getName());
			}
			
		}
		
		return showings;
	}

	@Override
	public ArrayList<String> getDates(String culturalInstitutionName, String showingName) {
		CulturalInstitution culturalInstitution = culturalInstitutionService.getCulturalInstitution(culturalInstitutionName);
		Showing showing = culturalInstitution.getShowing(showingName);
		List<Term> terms = termRepository.findByCulturalInstitutionAndShowing(culturalInstitution, showing);
		ArrayList<String> dates = new ArrayList<String>();
		
		for (Term term : terms) {
			if(!dates.contains(term.getDate().toString())) {
				dates.add(term.getDate().toString());
			}
			
		}
		
		return dates;
	}

	@Override
	public ArrayList<String> getAuditoriumAndTime(String culturalInstitutionName, String showingName, String dateStr) {
		CulturalInstitution culturalInstitution = culturalInstitutionService.getCulturalInstitution(culturalInstitutionName);
		Showing showing = culturalInstitution.getShowing(showingName);
		
		LocalDate date = null;
		try {
			date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
		} catch (Exception e) {}
		
		List<Term> terms = termRepository.findByDateAndCulturalInstitutionAndShowing(date, culturalInstitution, showing);
		ArrayList<String> auditoriumsAndTimes = new ArrayList<String>();
		
		String at;
		for (Term term : terms) {
			at = term.getAuditorium().getName() + ", " + term.getTime();
			if(!auditoriumsAndTimes.contains(at)) {
				auditoriumsAndTimes.add(at);
			}
			
		}
		
		return auditoriumsAndTimes;
	}

	@Override
	public List<String> getTermsByDateAndAuditoriumAndShowing(String date, String auditorium,
			String showing) {
		
		Showing showingDB = showingRepository.findByName(showing);
		
		Auditorium auditoriumDB = auditoriumRepository.findByName(auditorium);
		
		LocalDate dateLocal = null;
		try {
			dateLocal = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
		} catch (Exception e) {}
		
		List<Term> terms = termRepository.findByDateAndAuditoriumAndShowing(dateLocal, auditoriumDB, showingDB);
		
		int duration = showingDB.getDuration();
		List<String> termsReturn = new ArrayList<String>();
		for(Term t : terms)
		{			
			String term = t.getId().toString() + "*" + t.getTime().toString() + " - " + (t.getTime().plusMinutes(duration)).toString();
			termsReturn.add(term);
		}
		
		return termsReturn;
	}

	@Override
	public boolean addTerm(String culturalInstitutionName, String date, String auditoriumName, String showingName,
			String time) {
		CulturalInstitution culturalInstitution = culturalInstitutionService.getCulturalInstitution(culturalInstitutionName);
		
		Showing showing = showingRepository.findByName(showingName);
		
		Auditorium auditorium = auditoriumRepository.findByName(auditoriumName);
		
		LocalDate dateLocal = null;
		try {
			dateLocal = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
		} catch (Exception e) {}
		
		List<Term> terms = termRepository.findByDateAndAuditoriumAndShowing(dateLocal, auditorium, showing);
		
		int duration = showing.getDuration();
		LocalTime insertedTimeStart = LocalTime.parse(time, DateTimeFormatter.ISO_LOCAL_TIME);
		LocalTime insertedTimeEnd = insertedTimeStart.plusMinutes(duration);
		for(Term t : terms)
		{			
			LocalTime startTime = t.getTime();
			LocalTime endTime = t.getTime().plusMinutes(duration);
			if(insertedTimeStart.compareTo(startTime) <= 0)
			{
				if(insertedTimeEnd.compareTo(startTime) >= 0)
				{
					return false;
				}
			}
			else
			{
				if(insertedTimeStart.compareTo(endTime) < 0)
				{
					return false;
				}
			}
		}
		
		Term term = new Term(dateLocal, insertedTimeStart, culturalInstitution, auditorium, showing);
		termRepository.save(term);
		return true;
	}

	@Override
	public boolean deleteTerm(String id) {
		try
		{
			Term t = termRepository.findById(Long.parseLong(id));
			termRepository.delete(t);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
}
