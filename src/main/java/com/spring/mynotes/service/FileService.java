package com.spring.mynotes.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import com.spring.mynotes.controller.AppController;

@Service
public class FileService {
	
	private final Path fileStorageLocation = Paths.get(AppController.UPLOAD_DIR);
	
	public Resource loadFileAsResource(String fileName) {
		Resource resource = null;
        try {
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found: " + fileName);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return resource;
    }
}
