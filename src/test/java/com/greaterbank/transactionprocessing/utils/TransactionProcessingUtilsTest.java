package com.greaterbank.transactionprocessing.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/config/transaction-config.xml")
@ActiveProfiles("test")
public class TransactionProcessingUtilsTest {

	@Value("#{inputDirectory}")
	private String inputDirectory;
	
	@Value("#{processedDirectory}")
	private String processedDirectory;
	
	@Value("#{outputDirectory}")
	private String outputDirectory;
	
	@Value("#{fileNamePattern}")
	private String fileNamePattern;
	
	@Value("#{waitTimeFileProcessing}")
	private Long waitTimeFileProcessing;
	
	@Test
	public void testGetFilesFromDirectory() {
		File[] files  = TransactionProcessingUtils.getFilesFromDirectory(inputDirectory, fileNamePattern);
		assertNotNull(files);
		assertTrue(files.length > 0);
	}
	
	@Test
	public void testGetResources() {
		File[] files  = TransactionProcessingUtils.getFilesFromDirectory(inputDirectory, fileNamePattern);
		assertNotNull(files);
		assertTrue(files.length > 0);
		
		Resource[] resource = TransactionProcessingUtils.getResources(files);
		
		assertNotNull(resource);
		assertTrue(resource.length > 0);
	}
	
	@Test
	public void testCreateFile() throws IOException {
		TransactionProcessingUtils.createFile(outputDirectory+"//samplereport.txt", "hw r  u");
		
		File[] files  = TransactionProcessingUtils.getFilesFromDirectory(outputDirectory, "samplereport.txt");
		assertNotNull(files);
		assertTrue(files.length > 0);
	}
	
	@Test
	public void testFetchFileName() {
		String fileName = TransactionProcessingUtils.fetchFileName(outputDirectory+"//samplereport.txt");
		
		assertTrue(fileName.equalsIgnoreCase("samplereport.txt"));
		
	}
	
	@Test
	public void testCopyFile() throws IOException {
		TransactionProcessingUtils.copyFile(outputDirectory+"//samplereport.txt", processedDirectory);
		
		File[] files  = TransactionProcessingUtils.getFilesFromDirectory(outputDirectory, "samplereport.txt");
		assertNotNull(files);
		assertTrue(files.length > 0);
		
	}
}

