package com.jvm.isa.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.repository.Repository;

import com.jvm.isa.domain.Auditorium;
import com.jvm.isa.domain.CulturalInstitution;
import com.jvm.isa.domain.Showing;
import com.jvm.isa.domain.Term;

public interface TermRepository extends Repository<Term, Long> {
	
	List<Term> findByCulturalInstitution(CulturalInstitution culturalInstitution);
	
	List<Term> findByCulturalInstitutionAndShowing(CulturalInstitution culturalInstitution, Showing showing);
	
	Term findByCulturalInstitutionAndShowingAndDateAndTimeAndAuditorium(CulturalInstitution culturalInstitution, Showing showing, LocalDate date, LocalTime time, Auditorium auditorium);

	List<Term> findByCulturalInstitutionAndAuditorium(CulturalInstitution culturalInstitution, Auditorium auditorium);
	
	Term save(Term term);

	List<Term> findAll();

	List<Term> findByDateAndCulturalInstitutionAndShowing(LocalDate date, CulturalInstitution culturalInstitution,
			Showing showing);
	
	List<Term> findByDateAndAuditoriumAndShowing(LocalDate date, Auditorium auditorium, Showing showing);
	
	Term findById(Long id);
	
	void delete(Term term);

	List<Term> findByCulturalInstitutionAndShowingAndDateAndAuditorium(CulturalInstitution culturalInstitution,
			Showing showing, LocalDate date, Auditorium auditorium);
}
