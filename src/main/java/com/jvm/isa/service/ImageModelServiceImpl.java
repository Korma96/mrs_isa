package com.jvm.isa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageModelServiceImpl implements ImageModelService {

	@Autowired
	private ImageModelRepository imageModelRepository;
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	@Override
	public ImageModel getImageModel(String name) {
		try {
			return imageModelRepository.findByName(name);
		}
		catch(Exception e) {
			return null;
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
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
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	@Override
	public boolean delete(ImageModel imageModel) {
		try {
			imageModelRepository.delete(imageModel);
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	//@Async
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	@Override
	public void saveImageinDatabase(String fileName, MultipartFile image) {
		/*Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {*/

		ImageModel newImageModel;
		try {
			newImageModel = new ImageModel(fileName, image.getBytes());
			save(newImageModel);
			System.out.println("File successfully uploaded to : " + fileName); 
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error upload image!");
		}
		
		
				
		//	}
		//});
		//thread.start();
		
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	@Override
	public boolean removeImageFromDatabase(ImageModel imageModel) {
		
			return delete(imageModel);
		
	}

}
