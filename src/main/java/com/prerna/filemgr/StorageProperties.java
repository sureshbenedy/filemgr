package com.prerna.filemgr;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {
	public String getLocation() throws URISyntaxException {
		return "<setme>";
	}

}
