package com.prerna.filemgr.storage;

import java.net.MalformedURLException;

public class StorageFileNotFoundException extends Exception {

	private static final long serialVersionUID = -7337797234289747412L;

	public StorageFileNotFoundException(String msg) {
		super(msg);
	}

	public StorageFileNotFoundException(String msg, MalformedURLException e) {
		super(msg, e);
	}
}
