package com.jvm.isa.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jvm.isa.domain.CulturalInstitution;
import com.jvm.isa.domain.CulturalInstitutionType;
import com.jvm.isa.service.CulturalInstitutionService;

@RestController
@RequestMapping(value = "/home_page")
public class HomePageController 
{		
	@Autowired
	private CulturalInstitutionService culturalInstitutionService;
	
	@Autowired
	private HttpSession httpSession;
	
	@RequestMapping(value = "/cultural_institutions", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CulturalInstitution>> getCinemas(@RequestBody HashMap<String, String> hm)
	{
		int type = Integer.parseInt(hm.get("type"));
		List<CulturalInstitution> cinemas = culturalInstitutionService.getCulturalInstitutionsByType(CulturalInstitutionType.values()[type]);
		return new ResponseEntity<List<CulturalInstitution>>(cinemas, HttpStatus.OK);
	}
}

