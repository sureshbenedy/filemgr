package com.prerna.filemgr.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prerna.filemgr.storage.StorageException;
import com.prerna.filemgr.storage.StorageFileNotFoundException;
import com.prerna.filemgr.storage.StorageService;

@Controller
public class FileUploadController {

	private final StorageService storageService;

	@Autowired
	public FileUploadController(StorageService storageService) {
		this.storageService = storageService;
	}

	@GetMapping("/")
	public String listUploadedFiles(Model model) throws IOException {

		try {
			model.addAttribute("files", storageService.loadAll().map(
					path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
							"serveFile", path.getFileName().toString()).build().toUri().toString())
					.collect(Collectors.toList()));
		} catch (StorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "uploadForm";
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource file;
		try {
			file = storageService.loadAsResource(filename);
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
					"attachment; filename=\"" + file.getFilename() + "\"").body(file);
		} catch (StorageFileNotFoundException e) {
			
			//e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}

	@PostMapping("/")
	public String handleFileUpload(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) {

		try {
			storageService.store(file);
			redirectAttributes.addFlashAttribute("message",
					"You successfully uploaded " + file.getOriginalFilename() + "!");
		} catch (StorageException e) {
			
			//e.printStackTrace();
		}

		return "redirect:/";
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

}