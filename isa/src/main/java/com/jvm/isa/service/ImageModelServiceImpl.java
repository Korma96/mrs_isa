package com.jvm.isa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jvm.isa.domain.ImageModel;
import com.jvm.isa.repository.ImageModelRepository;

@Service
public class ImageModelServiceImpl implements ImageModelService {

	@Autowired
	private ImageModelRepository imageModelRepository;
	
	@Override
	public ImageModel getImageModel(String name) {
		return imageModelRepository.findByName(name);
	}

	@Override
	public boolean save(ImageModel imageModel) {
		try {
			imageModelRepository.save(imageModel);
			
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}

}
