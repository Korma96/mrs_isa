package com.jvm.isa.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jvm.isa.domain.Auditorium;
import com.jvm.isa.domain.CulturalInstitution;
import com.jvm.isa.domain.RegisteredUser;
import com.jvm.isa.domain.Showing;
import com.jvm.isa.domain.Term;
import com.jvm.isa.repository.AuditoriumRepository;
import com.jvm.isa.repository.TermRepository;

@Service
public class TermServiceImpl implements TermService {
	
	@Autowired
	private TermRepository termRepository;
	
	@Autowired
	private CulturalInstitutionService culturalInstitutionService;
	
	//@Autowired
	//private ShowingRepository showingRepository;
	
	@Autowired
	private AuditoriumRepository auditoriumRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TicketService ticketService;
	
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	@Override
	public Boolean bookSelectedSeats(String dateStr, String timeStr, String culturalInstitutionName, String showingName, String auditoriumName, String selectedSeats, RegisteredUser owner) {
		String[] tokens = selectedSeats.split(",");
		
		Term term = getTerm(dateStr, timeStr, culturalInstitutionName, showingName, auditoriumName);
		if(term == null) return false;
		
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
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	@Override
	public Term getTerm(String dateStr, String timeStr, String culturalInstitutionName, String showingName, String auditoriumName) {
		LocalDate date = null;
		LocalTime time = null;
		
		try {
			date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
			time = LocalTime.parse(timeStr, DateTimeFormatter.ISO_TIME);
		} catch (Exception e) {
			return null;
		}
		
		CulturalInstitution culturalInstitution = culturalInstitutionService.getCulturalInstitution(culturalInstitutionName);
		if(culturalInstitution == null) return null;
		
		Showing showing = culturalInstitution.getShowing(showingName);
		if(showing == null) return null;
		
		Auditorium auditorium = culturalInstitution.getAuditorium(auditoriumName);
		if(auditorium == null) return null;
		
		try {
			Term term = termRepository.findByCulturalInstitutionAndShowingAndDateAndTimeAndAuditorium(culturalInstitution, showing, date, time, auditorium);	
			return term;
		}
		catch (Exception e) {
			return null;
		}
		
	}
	
	//Mozda treba staviti @Transactional zbog ovog term.getAuditorium() jer je sve podeseno na LAZY
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	@Override
	public ArrayList<Integer> getIndexOfBusySeatsAndRowsCols(Term term) {
		
		
		Boolean[] seats = term.getSeats();
		ArrayList<Integer> indexes = getIndexesOfBusySeats(seats);
		
		Auditorium auditorium = term.getAuditorium();
		if(auditorium == null) return null;
		
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

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	@Override
	public boolean save(Term term) {
		try {
			termRepository.save(term);
			return true;
		} 
		catch (Exception e) {
			return false;
		}
		
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	@Override
	public ArrayList<String> getCulturalInstitutions() {
		try {
			List<Term> terms = termRepository.findAll();
			ArrayList<String> culturalInstitutions = new ArrayList<String>();
		
			for (Term term : terms) {
				if(!culturalInstitutions.contains(term.getCulturalInstitution().getName())) {
					culturalInstitutions.add(term.getCulturalInstitution().getName());
				}
				
			}
			
			return culturalInstitutions;
		}
		catch (Exception e) {
			return null;
		}
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	@Override
	public ArrayList<String> getShowings(String culturalInstitutionName) {
		try {
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
		catch (Exception e) {
			return null;
		}
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	@Override
	public ArrayList<String> getDates(String culturalInstitutionName, String showingName) {
		try {
			CulturalInstitution culturalInstitution = culturalInstitutionService.getCulturalInstitution(culturalInstitutionName);
			if(culturalInstitution == null) return null;
			
			Showing showing = culturalInstitution.getShowing(showingName);
			if(showing == null) return null;
			
			List<Term> terms = termRepository.findByCulturalInstitutionAndShowing(culturalInstitution, showing);
			if(terms == null) return null;
			
			ArrayList<String> dates = new ArrayList<String>();
			
			for (Term term : terms) {
				if(userService.computeSubtractTwoDateTime(LocalDate.now(), term.getDate(), LocalTime.now(), term.getTime()) <= 0) continue;
				
				if(!dates.contains(term.getDate().toString())) {
					dates.add(term.getDate().toString());
				}
				
			}
			
			return dates;
		}
		catch (Exception e) {
			return null;
		}
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	@Override
	public ArrayList<String> getAuditoriums(String culturalInstitutionName, String showingName, String dateStr) {
		ArrayList<String> auditoriums = new ArrayList<String>();
		CulturalInstitution culturalInstitution = culturalInstitutionService.getCulturalInstitution(culturalInstitutionName);
		if(culturalInstitution == null) return auditoriums;
		
		Showing showing = culturalInstitution.getShowing(showingName);
		if(showing == null) return auditoriums;
		
		LocalDate date = null;
		try {
			date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
			if(date.compareTo(LocalDate.now()) < 0) return auditoriums;
		} 
		catch (Exception e) {
			return auditoriums;
		}
		
		try {
			List<Term> terms = termRepository.findByDateAndCulturalInstitutionAndShowing(date, culturalInstitution, showing);
		
			String a;
			for (Term term : terms) {
				a = term.getAuditorium().getName();
				if(!auditoriums.contains(a)) {
					auditoriums.add(a);
				}
				
			}
			
			return auditoriums;
		}
		catch (Exception e) {
			return null;
		}
		
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	@Override
	public ArrayList<String> getTimes(String culturalInstitutionName, String showingName, String dateStr, String auditoriumStr) {
		ArrayList<String> times = new ArrayList<String>();
		CulturalInstitution culturalInstitution = culturalInstitutionService.getCulturalInstitution(culturalInstitutionName);
		if(culturalInstitution == null) return times;
		
		Showing showing = culturalInstitution.getShowing(showingName);
		if(showing == null) return times;
		
		LocalDate date = null;
		try {
			date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
			if(date.compareTo(LocalDate.now()) < 0) return times;
		} 
		catch (Exception e) {
			return times;
		}
		
		Auditorium auditorium = culturalInstitution.getAuditorium(auditoriumStr);
		if(auditorium == null) return times;
		
		try {
			List<Term> terms = termRepository.findByCulturalInstitutionAndShowingAndDateAndAuditorium(culturalInstitution, showing, date, auditorium);
			String t;
			for (Term term : terms) {
				t = term.getTime().format(DateTimeFormatter.ISO_TIME);
				if(!times.contains(t)) {
					times.add(t);
				}
				
			}
			
			return times;
		}
		catch (Exception e) {
			return null;
		}
		
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	@Override
	public List<String> getTermsByDateAndAuditorium(String date, String auditorium) {
		Auditorium auditoriumDB;
		
		try {
			auditoriumDB = auditoriumRepository.findByName(auditorium);
		}
		catch (Exception e) {
			return null;
		}
		
		LocalDate dateLocal = null;
		
		try {
			dateLocal = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
		} catch (Exception e) { return null; }
		
		try {
			List<Term> terms = termRepository.findByDateAndAuditorium(dateLocal, auditoriumDB);
			// sort terms ascending by time
			for(int i=0; i<terms.size(); i++)
			{
				for(int j=i+1; j<terms.size(); j++)
				{
					if(terms.get(i).getTime().compareTo(terms.get(j).getTime()) > 0)
					{
						Term temp = terms.get(i);
						terms.set(i, terms.get(j));
						terms.set(j, temp);
					}
				}
			}
			
			List<String> termsReturn = new ArrayList<String>();
			for(Term t : terms)
			{		
				int duration = t.getShowing().getDuration();
				String term = t.getId().toString() + "*" + t.getShowing().getName() + "*" +t.getTime().toString() + " - " + (t.getTime().plusMinutes(duration)).toString()  + "*" + (new Double(t.getPrice())).toString();
				termsReturn.add(term);
			}
			
			return termsReturn;
		}
		catch (Exception e) {
			return null;
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	@Override
	public boolean addTerm(CulturalInstitution culturalInstitution, String date, String auditoriumName, String showingName,
			String time, double price) {
		
		if(culturalInstitution == null) return false;
		
		Showing showing = culturalInstitution.getShowing(showingName);
		if(showing == null) return false;
		
		Auditorium auditorium = culturalInstitution.getAuditorium(auditoriumName);
		if(auditorium == null) return false;
		
		LocalDate dateLocal = null;
		try {
			dateLocal = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
		} catch (Exception e) {
			return false;
		}
		
		try {
			List<Term> terms = termRepository.findByCulturalInstitutionAndAuditorium(culturalInstitution, auditorium);
		
			int duration = showing.getDuration();
			LocalTime insertedTimeStart = LocalTime.parse(time, DateTimeFormatter.ISO_LOCAL_TIME);
			
			if(userService.computeSubtractTwoDateTime(LocalDate.now(), dateLocal, LocalTime.now(), insertedTimeStart) <= 0) {
				  return false;
			}
			
			LocalTime insertedTimeEnd = insertedTimeStart.plusMinutes(duration);
			
			for(Term t : terms)
			{			
				if(userService.computeSubtractTwoDateTime(LocalDate.now(), t.getDate(), LocalTime.now(), t.getTime()) <= 0) continue;
				
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
			
			Term term = new Term(dateLocal, insertedTimeStart, culturalInstitution, auditorium, showing, price);
			term = termRepository.save(term);
			return term.getId() != null;
		}
		catch (Exception e) {
			return false;
		}
		
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
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
	
	/*
     * Logika pocinje da se izvrsava u fiksnim intervalima sa odlozenim
     * pocetkom izvrsavanja prilikom pokretanja aplikacije.
     * <code>fixedRate</code> se koristi kao indikacija u kojem intervalu
     * ce se pozivati metoda. <code>initialDelay</code> se koristi kao indikacija
     * koliko posle pokretanja aplikacije treba da se ceka do prvog pokretanja metode.
     */
	/*
   @Scheduled(initialDelayString = "5000", fixedRateString = "1800000") // 1 800 000 ms = 30 min
   @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
   @Override
    public void scheduleRemoveOldTerms() {
	   try {
		   List<Term> terms = termRepository.findAll();
		   
		   for (Term term : terms) {
			   if(userService.computeSubtractTwoDateTime(LocalDate.now(), term.getDate(), LocalTime.now(), term.getTime()) <= 0) {
				   termRepository.delete(term);
			   }
		   }
	   }
	   catch (Exception e) {}
	
    }*/
   
   /*
   @Scheduled(initialDelayString = "5000", fixedRateString = "120000") // 120 000 ms = 2 min
   @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
   @Override
   public void scheduledReleaseSeats() {
	   try {
		   List<Term> terms = termRepository.findAll();
		   Ticket  ticket;
		   boolean saveChanges;
		   
		   for (Term term : terms) {
			   saveChanges = false;
			   if(userService.computeSubtractTwoDateTime(LocalDate.now(), term.getDate(), LocalTime.now(), term.getTime()) <= 0) {
				   for (int i = 0; i < term.getSeats().length; i++) {
						if(term.getSeats()[i]) {
							ticket = ticketService.getTicket(term, i);
							if(ticket == null) {
								term.getSeats()[i] = false;
								saveChanges = true;
							}
						}
				   }
			   }
			   
			   if(saveChanges) save(term);
		   }
	   }
	   catch (Exception e) {}
   }*/
}
