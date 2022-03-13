package com.prerna.filemgr.service;

import java.io.IOException;
import java.util.List;

public interface UploaderSevice {


	void store(String fileName) throws IOException;

	List<String> getList();

	void deleteFile(String fileName);

	void getFileContent(String fileName, String storagePath);

}
