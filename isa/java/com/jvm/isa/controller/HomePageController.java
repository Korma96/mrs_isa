package com.jvm.isa.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jvm.isa.domain.CulturalInstitution;
import com.jvm.isa.domain.CulturalInstitutionDTO;
import com.jvm.isa.domain.CulturalInstitutionType;
import com.jvm.isa.service.CulturalInstitutionService;

@RestController
@RequestMapping(value = "/home_page")
public class HomePageController 
{		
	@Autowired
	private CulturalInstitutionService culturalInstitutionService;
	
	
	@RequestMapping(value = "/cultural_institutions", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CulturalInstitutionDTO>> getCulturalInstitutions(@RequestBody HashMap<String, String> hm)
	{
		int type = Integer.parseInt(hm.get("type"));
		CulturalInstitutionType cit = CulturalInstitutionType.values()[type];
		List<CulturalInstitution> cis = culturalInstitutionService.getCulturalInstitutionsByType(cit);
		ArrayList<CulturalInstitutionDTO> ciDTOs = new ArrayList<CulturalInstitutionDTO>();
		for (CulturalInstitution culturalInstitution : cis) {
			ciDTOs.add(new CulturalInstitutionDTO(culturalInstitution));
		}
		
		return new ResponseEntity<List<CulturalInstitutionDTO>>(ciDTOs, HttpStatus.OK);
	}
}

