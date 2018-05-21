package com.jvm.isa.repository;

import java.time.LocalDate;

import org.springframework.data.repository.Repository;

import com.jvm.isa.domain.CulturalInstitution;
import com.jvm.isa.domain.Showing;
import com.jvm.isa.domain.Term;

public interface TermRepository extends Repository<Term, Long> {
	
	Term findByDateAndCulturalInstitutionAndShowing(LocalDate date, CulturalInstitution culturalInstitution, Showing showing);

	Term save(Term term);
}
