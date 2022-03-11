package com.prerna.filemgr;

import java.net.URISyntaxException;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;

@Profile("local")
@ConfigurationProperties(prefix = "app.storge")
public class StorageProperties {
	String dir;
	public StorageProperties() {
		
	}

	public String getLocation() throws URISyntaxException {
		return dir;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

}
