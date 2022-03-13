package com.prerna.filemgr.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prerna.filemgr.StorageProperties;
import com.prerna.filemgr.storage.StorageException;
import com.prerna.filemgr.storage.StorageFileNotFoundException;
import com.prerna.filemgr.storage.StorageService;

@Controller
@RequestMapping("/cli")
public class FileUploadController{

	private final StorageService storageService;
	StorageProperties rootLocation;
	@Autowired
	public FileUploadController(StorageService storageService, StorageProperties properties) {
		this.storageService = storageService;		
		rootLocation = properties;
	}

	@PostMapping("/upload")
	public ResponseEntity<String> handleFileUploadCli(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) {

		try {
			storageService.store(file);
			redirectAttributes.addFlashAttribute("message",
					"You successfully uploaded " + file.getOriginalFilename() + "!");
			return new ResponseEntity<>("You successfully uploaded " , HttpStatus.OK);
		} catch (StorageException e) {
			//e.printStackTrace();
		}

		return new ResponseEntity<>("Not Done" , HttpStatus.INSUFFICIENT_STORAGE);
	}
	
	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}
	@GetMapping("/list")
	public @ResponseBody List<String> getList() throws StorageException {
		return storageService.loadAll().map(p->p.toFile().getName()).collect(Collectors.toList());
	}

	@DeleteMapping("/remove/{file}")
	public @ResponseBody String deleteFile(@PathVariable(name="file") String fileName) {
		storageService.remove(fileName);
		System.out.println("Receice request to delete File" + fileName);
		return "Success";
	}

}