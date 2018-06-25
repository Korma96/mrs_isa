package com.jvm.isa.service;

import com.jvm.isa.domain.ImageModel;
import org.springframework.web.multipart.MultipartFile;

public interface ImageModelService {
	
	ImageModel getImageModel(String name);
	
	boolean save(ImageModel imageModel);
	
	boolean delete(ImageModel imageModel);
	
	void saveImageinDatabase(String fileName, MultipartFile image);
	
	boolean removeImageFromDatabase(ImageModel imageModel);
}
