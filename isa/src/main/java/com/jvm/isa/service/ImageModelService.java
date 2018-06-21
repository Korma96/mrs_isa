package com.jvm.isa.service;

import com.jvm.isa.domain.ImageModel;

public interface ImageModelService {
	
	ImageModel getImageModel(String name);
	
	boolean save(ImageModel imageModel);
}
