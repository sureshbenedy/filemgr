package com.prerna.filemgr.cli;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prerna.filemgr.service.UploaderSevice;

public class FileUploaderClient implements UploaderSevice {
	private static final String DEFAULT_DOWNLOAD_LOCATION = "/home/suresh/Downloads/";

	private static final String DOWNLOAD_URL = "http://localhost:8080/cli/remove/";
	private static final String UPLOAD_URL = "http://localhost:8080/cli/upload";
	private static final String LIST_URL = "http://localhost:8080/cli/list";
	private static final String DELETE_URL = "http://localhost:8080/cli/remove/";

	@Override
	public void store(String fileName) throws IOException {
		URL url = new URL(UPLOAD_URL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");

		FileBody fileBody = new FileBody(new File(fileName));
		MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.STRICT);
		multipartEntity.addPart("file", fileBody);

		connection.setRequestProperty("Content-Type", multipartEntity.getContentType().getValue());
		OutputStream out = connection.getOutputStream();
		int status = -1;
		String content = "";
		try {
			multipartEntity.writeTo(out);
			status = connection.getResponseCode();

			if (status == 200) {
				InputStream contentStream = (InputStream) connection.getContent();
				content = new String(contentStream.readAllBytes());
				System.out.println(String.format("Status : %3d\nMsg : %s", status, content));
			} else {
				System.out.println(String.format("Failed With Status : %3d", status));
			}
		} finally {
			out.close();
		}
	}

	public void download(String fileName) {
		download(fileName, DEFAULT_DOWNLOAD_LOCATION);
	}

	public void download(String fileName, String targetPath) {
		try {
			CloseableHttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(DOWNLOAD_URL + fileName);

			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();

			int responseCode = response.getStatusLine().getStatusCode();

			System.out.println("Request Url: " + request.getURI());
			System.out.println("Response Code: " + responseCode);

			InputStream is = entity.getContent();

			String filePath = targetPath + fileName;
			FileOutputStream fos = new FileOutputStream(new File(filePath));

			int inByte;
			while ((inByte = is.read()) != -1) {
				fos.write(inByte);
			}

			is.close();
			fos.close();

			client.close();
			System.out.println("File Download Completed!!!");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<String> getList() {
		List<String> fileNames = new ArrayList<>();
		URL url;
		try {
			url = new URL(LIST_URL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("GET");
			connection.connect();
			int status = connection.getResponseCode();
			InputStream contentStream = (InputStream) connection.getContent();
			String content = new String(contentStream.readAllBytes());
			
			if (status == 200) {
				System.out.println("Request to get List of files succeded\n File List\n=============\n");
				ObjectMapper mapper = new ObjectMapper();
				mapper.readerForArrayOf(String.class);
				JsonNode values = mapper.readerForArrayOf(String.class).readTree(content);
				values.forEach(p -> fileNames.add(p.textValue()));
				fileNames.forEach(System.out::println);
			} else {
				System.out.println("Request to get List of files failed with status : " + status);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return fileNames;
	}

	@Override
	public void getFileContent(String fileName, String storagePath) {
		if (StringUtils.hasText(storagePath)) {
			download(fileName);
		}
		download(fileName, storagePath);
	}

	@Override
	public void deleteFile(String fileName) {
		URL url;
		try {
			String path = DELETE_URL  + fileName;
			url = new URL(path );
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("DELETE");
			connection.connect();
			int status = connection.getResponseCode();	
			if (status == 200) {
				System.out.println("Request to DELETE file succeded\n");
			} else {
				System.out.println("Request to DELETE file failed with status : " + status);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}
