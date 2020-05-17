package com.greaterbank.transactionprocessing.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/*****************************************
 * Utility methods to help 
 * in achieving desired 
 * solution in Transaction Processing
 * @author NitinKalra
 *
 ****************************************/
@Component
public class TransactionProcessingUtils {
	
	private static final Logger log = Logger.getLogger(TransactionProcessingUtils.class.getName());
	
	
	/**
	 * return the list of Files object
	 * 
	 * @param path - Directory path to look for files
	 * @param filePattern - its a regext patter to pick the file names
	 * @return
	 */
	public static File[] getFilesFromDirectory(String path, String filePattern) {
		File 		directory 	= new File(path);
		File[] 		files 		= directory.listFiles();
		FileFilter 	filter 		= new RegexFileFilter(filePattern);
		
		log.info("all files and directories : " + directory);
		files = directory.listFiles(filter);
		log.info("No of files matched in directory : " + files.length);
		
		return files;
	}
	
	
	/**
	 * Return the resource object from file object
	 * @param files
	 * @return
	 */
	public static Resource[] getResources(File[] files) {
		FileSystemResource 	fileSystemRes 	= null;
		Resource[] 			resources 		= null;
		
		log.debug("***Converting Files to Resources***");
		resources = new Resource[files.length];
		
		for(int i = 0; i < files.length; i++) {
			fileSystemRes = new FileSystemResource(files[i]);
			resources[i] = fileSystemRes;
		}
		
		return resources;
	}
	
	/**
	 * create a new file
	 * @param file - file name with path to be created
	 * @param data - data to be dumped into the file
	 * @throws IOException
	 */
	public static void createFile(String file, String data) throws IOException {
		File fileName = new File(file);
		FileUtils.write(fileName, data); //closes itself
	}
	
	/**
	 * fetch the file name from a path.
	 * It will give name of file after last appearance of
	 * '\'
	 * @param path
	 * @return
	 */
	public static String fetchFileName(String path) {
		int 		index 		= path.lastIndexOf("\\");
		if(index < 0) {
			index = path.lastIndexOf("//");
		}
		String	 	fileName    = path.substring(index + 1);

		if(fileName.contains("/")) {
			fileName = fileName.replace("/", "");
		}
		if(fileName.contains("\\")) {
			fileName = fileName.replace("\\", "");
		}
		return fileName;
	}
	
	/**
	 * move file
	 * @param fileName - file to be moved
	 * @param dest - destination directory
	 * @throws IOException
	 */
	public static void moveFile(String fileName, String dest) throws IOException {
		File destDir = new File(dest);
		File srcFile = new File(fileName);
		
		FileUtils.moveFileToDirectory(srcFile, destDir, true);
	}

	/**
	 * copy file 
	 * @param fileName - src file name
	 * @param dest - its a directory path
	 * @throws IOException
	 */
	public static void copyFile(String fileName, String dest) throws IOException {
		File destDir = new File(dest);
		File srcFile = new File(fileName);
		
		FileUtils.copyFileToDirectory(srcFile, destDir, true);
	}

}
