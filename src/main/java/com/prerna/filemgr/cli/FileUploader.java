package com.prerna.filemgr.cli;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;

/* HttpClient file uploads with Apache Commons */
public class FileUploader {
  private static String uurl = "http://localhost:8080/upload";

  public static void main(String[] args) throws IOException {
	  ClassLoader loader = FileUploader.class.getClassLoader();
	  System.out.println(loader.getResource("com/prerna/filemgr/cli/FileUploader.class"));
	  String fileName = loader.getResource("com/prerna/filemgr/cli/FileUploader.class").toString().substring(5);
	  
	  URL url = new URL(uurl);
	  HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	  connection.setDoOutput(true);
	  connection.setRequestMethod("POST");


	  FileBody fileBody = new FileBody(new File(fileName ));
	  MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.STRICT);
	  multipartEntity.addPart("file", fileBody);

	  connection.setRequestProperty("Content-Type", multipartEntity.getContentType().getValue());
	  OutputStream out = connection.getOutputStream();
	  int status = -1;
	  String content = "";
	  try {
	      multipartEntity.writeTo(out);
		  status = connection.getResponseCode();
		  
		  if(status == 200) {
			  InputStream contentStrean = (InputStream)connection.getContent();
			  content = new String(contentStrean.readAllBytes());
			  System.out.println(String.format("Status : %3d\nMsg : %s", status, content));
		  }else {
			  System.out.println(String.format("Failed With Status : %3d", status));
		  }		  
	  } finally {
	      out.close();
	  }
  }
}
