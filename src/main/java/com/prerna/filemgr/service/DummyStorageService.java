package com.prerna.filemgr.service;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.prerna.filemgr.storage.StorageException;
import com.prerna.filemgr.storage.StorageFileNotFoundException;
import com.prerna.filemgr.storage.StorageService;

@Profile("storage")
@Service
public class DummyStorageService implements StorageService {

	@Override
	public void init() throws StorageException {
	}

	@Override
	public void store(MultipartFile file) throws StorageException {
		throw new StorageException("Not Implemented");
	}

	@Override
	public Stream<Path> loadAll() throws StorageException {
		throw new StorageException("Not Implemented");
	}

	@Override
	public Path load(String filename) {
		return null;
	}

	@Override
	public Resource loadAsResource(String filename) throws StorageFileNotFoundException {
		throw new StorageFileNotFoundException("Not Implemented");
	}

	@Override
	public void deleteAll() {
		
	}

}
