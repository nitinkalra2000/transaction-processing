package com.greaterbank.transactionprocessing.listener;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Component;

import com.greaterbank.transactionprocessing.constant.TransactionProcessingConstants;

/***
 * Its a Job listener 
 * for TransactionProcessing Job
 * 
 * @author NitinKalra
 *
 */
@Component
public class TransactionProcessingJobListener implements JobExecutionListener {
	private static final Logger log = Logger.getLogger(TransactionProcessingJobListener.class.getName());
	/**
	 * After the processing job has completed
	 * lets delete the input file which has
	 * already been copied to processed folder.
	 */
	@Override
	public void afterJob(JobExecution jobExecution) {
		Collection<StepExecution> steps  = jobExecution.getStepExecutions();
		String fileName 				 = "";
		String fileNameModifed			 = "";
		
		for(StepExecution step : steps) {
			if(step.getStepName().equals(TransactionProcessingConstants.STEP_1)) {
				if(step.getExecutionContext().get(TransactionProcessingConstants.INPUT_FILE_NAME_KEY) != null) {
					fileName 		= step.getExecutionContext().getString(TransactionProcessingConstants.INPUT_FILE_NAME_KEY);
					log.debug("Path of file to be deleted - "+fileName);
					fileNameModifed = StringUtils.remove(fileName, "file:");
					try {
						log.debug("**Going to Delete Input file**" + fileNameModifed);
						FileUtils.forceDelete(new File(fileNameModifed));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void beforeJob(JobExecution arg0) {
		// TODO Auto-generated method stub
	}
}
