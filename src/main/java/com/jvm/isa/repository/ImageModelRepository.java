package com.jvm.isa.repository;

import org.springframework.data.repository.Repository;

import com.jvm.isa.domain.ImageModel;

public interface ImageModelRepository extends Repository<ImageModel, Long> {

	ImageModel save(ImageModel imageModel);
	
	ImageModel findByName(String name);
	
	void delete(ImageModel imageModel);
}
