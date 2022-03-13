package com.prerna.filemgr.cli;

import java.io.IOException;

import com.prerna.filemgr.service.UploaderSevice;


public class FileUploader {
	private static final String UPLOAD_CMD = "upload";
	private static final String LIST_CMD = "list";
	private static final String LOAD_CMD = "load";
	private static final String DELETE_CMD = "delete";
	private static final String HELP_CMD = "help";
	
	
	private static final int UPLOAD = 0;
	private static final int LIST = 1;
	private static final int LOAD = 2;
	private static final int DELETE = 3;
	private static final int HELP = 4;
	private static final int DEFAULT = 5;
	
	private static UploaderSevice client = new FileUploaderClient();
	public static void main(String[] args) throws IOException {
		int command = getCommand(args);
		switch (command) {
		case UPLOAD:
			client.store(args[1]);
			break;
		case LIST:
			client.getList();
			break;
		case LOAD:
			client.getFileContent(args[1],args[2]);
			break;
		case DELETE:
			client.deleteFile(args[1]);
			break;
		case HELP:
			printHelp(args[1]);
			break;
		default:
			printUsage();			
		}
	}

	private static void printUsage() {
		System.out.println("Usage to execute command : java FileUploader <upload|list|load|delete> [<filename>]");
		System.out.println("Usage to get help : java FileUploader help <upload|list|load|delete>");
	}

	private static void printHelp(String helpFor) {
		if(helpFor.equalsIgnoreCase(UPLOAD_CMD)) {
			System.out.println("Usage to execute command : java FileUploader upload [<filename>]");			
			System.out.println("Example for windows environment  : java FileUploader upload c:/upload/test.txt");
			System.out.println("Example for mac/linux environment : java FileUploader upload /home/user/data/test.txt");
		}
		if(helpFor.equalsIgnoreCase(LOAD_CMD)) {
			System.out.println("Usage to execute command : java FileUploader load <filename> <targetPath>");			
			System.out.println("Example for windows environment  : java FileUploader load test.txt c:/download/");
			System.out.println("Example for mac/linux environment : java FileUploader load test.txt /home/user/download/");
		}
		if(helpFor.equalsIgnoreCase(LIST_CMD)) {
			System.out.println("Example command : java FileUploader list");			
		}
		if(helpFor.equalsIgnoreCase(DELETE_CMD)) {
			System.out.println("Usage to execute command : java FileUploader delete <serverFilename>");			
			System.out.println("Example for windows environment  : java FileUploader delete HELP.md");
		}
		// TODO: Fix any missing
	}

	private static int getCommand(String[] args) {
		if(args.length<1)
			return DEFAULT;
		String cmd = args[0];
		if(cmd.equalsIgnoreCase(UPLOAD_CMD)) {
			return UPLOAD;
		}
		if(cmd.equalsIgnoreCase(LIST_CMD)) {
			return LIST;
		}

		if(cmd.equalsIgnoreCase(LOAD_CMD)) {
			return LOAD;
		}

		if(cmd.equalsIgnoreCase(DELETE_CMD)) {
			return DELETE;
		}
		if(cmd.equalsIgnoreCase(HELP_CMD)) {
			return HELP;
		}
		return DEFAULT;
	}
}
