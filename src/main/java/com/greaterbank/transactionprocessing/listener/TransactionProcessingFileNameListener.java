package com.greaterbank.transactionprocessing.listener;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.greaterbank.transactionprocessing.constant.TransactionProcessingConstants;
import com.greaterbank.transactionprocessing.model.TransactionReportModel;
import com.greaterbank.transactionprocessing.utils.TransactionProcessingUtils;


/**
 * This class will act as a step listener
 * for TransactionProcessing step
 * @author NitinKalra
 *
 */
public class TransactionProcessingFileNameListener {
	private static final Logger log = Logger.getLogger(TransactionProcessingFileNameListener.class.getName());
	
	private String fileNamePattern;
	private String inputDirectory;
	private String processedDirectory;
	private String outputDirectory;
	private Long   waitTimeFileProcessing;
	
	@Autowired
	private TransactionReportModel reportModel;

	
	public void setWaitTimeFileProcessing(Long waitTimeFileProcessing) {
		this.waitTimeFileProcessing = waitTimeFileProcessing;
	}

	public void setInputDirectory(String inputDirectory) {
		this.inputDirectory = inputDirectory;
	}

	public void setFileNamePattern(String fileNamePattern) {
		this.fileNamePattern = fileNamePattern;
	}
	public void setProcessedDirectory(String processedDirectory) {
		this.processedDirectory = processedDirectory;
	}

	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	/**
	 * this method will be executed before transactionProcessing
	 * step to find the file to be picked from input folder
	 * and the file name will be store into stepExecution
	 * 
	 * @param stepExecution
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	@BeforeStep
	public void fetchFilesFromPattern(StepExecution stepExecution) throws IOException, InterruptedException {
		ExecutionContext 	execContext = stepExecution.getExecutionContext();
		File[] 				files 		= TransactionProcessingUtils.getFilesFromDirectory
										  (inputDirectory, fileNamePattern);
		if(files.length == 0) {
			log.error("**No files to be processed in the input directory***");
		}
		
		Resource[] 			res   		= TransactionProcessingUtils.getResources(files);
		
		if(res.length > 0) {
			execContext.putString(TransactionProcessingConstants.INPUT_FILE_NAME_KEY, "file:" + res[0].getFile().getPath());
			log.debug("**File received - "+res[0].getFile().getPath()+", Time to wait for processing- " + waitTimeFileProcessing +" ms");
			Thread.sleep(waitTimeFileProcessing); //run the job after 5 minutes;300000
		}
	
		
	}
	
	/***
	 *  This method will be executed after the execution
	 *  of TransactionProcessin step to generate
	 *  the report and move the file
	 *  
	 * @param stepExecution
	 * @throws IOException
	 */
	@AfterStep
	public void writeReport(StepExecution stepExecution) throws IOException {
		ExecutionContext 		execContext = stepExecution.getExecutionContext();
		String					fileName	= null;
		
		if(execContext.containsKey(TransactionProcessingConstants.INPUT_FILE_NAME_KEY)) {
			fileName = execContext.getString(TransactionProcessingConstants.INPUT_FILE_NAME_KEY);
		} 
		
		if(reportModel == null) {
			log.error("**Nothing to write in Report File**");
		}
		if(fileName != null) {
			log.debug("***Creating Report File*** " + fileName);
			String inputFile = TransactionProcessingUtils.fetchFileName(fileName);
			TransactionProcessingUtils.createFile(outputDirectory + "//"+inputFile.replace("csv", "txt"), reportModel.toString());
			
			log.debug("***Copying input file to processed folder***");
			TransactionProcessingUtils.copyFile(StringUtils.remove(fileName, "file:"), processedDirectory);
		}
		
	}
		
}
