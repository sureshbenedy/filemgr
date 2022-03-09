package com.prerna.filemgr;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

	@Value("app.storge.root.dir")
	private String rootDir; 
	
	public String getLocation() throws URISyntaxException {
		// TODO Auto-generated method stub
		return "/home/suresh/temp/";
	}

}
