package com.greaterbank.transactionprocessing;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/*******************************************
 * This class will be used to launch the
 * file processing job
 * This gets invoked as a scheduler define
 * in config xml
 * 
 * @author NitinKalra
 *
 *******************************************/
@Component
public class TransactionProcessingScheduler {
	
	private static final Logger log = Logger.getLogger(TransactionProcessingScheduler.class.getName());
	
	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;
	
	
	public void run() {
		BatchStatus 		batchStatus	= null;
		
		//If not using task scheduler then use these--
		//JobLauncher 		jobLauncher = ctx.getBean(JobLauncher.class);
		//Job 				job 		= ctx.getBean("transactionProcessingJob", Job.class);
		
		log.debug("***launching the job*** TIME ***" + new Date());
		JobParametersBuilder 	jpBuilder 	  = new JobParametersBuilder().addLong("timestamp", new Date().getTime());
		JobParameters 			jobParameters = jpBuilder.toJobParameters();  

    	JobExecution jobExecution;
		try {
			
			jobExecution = jobLauncher.run(job, jobParameters);
			batchStatus  = jobExecution.getStatus();
			
			/*long jobExecutionId = jobOperator.startNextInstance(job.getName());
			String result = jobOperator.getSummary(jobExecutionId);*/
			
			log.debug("batchStatus - " + batchStatus);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException  e) {
			e.printStackTrace();
		} 
	}

}
