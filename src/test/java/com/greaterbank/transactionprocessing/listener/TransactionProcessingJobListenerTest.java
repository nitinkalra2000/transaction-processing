package com.greaterbank.transactionprocessing.listener;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.greaterbank.transactionprocessing.constant.TransactionProcessingConstants;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/config/transaction-config.xml")
@ActiveProfiles("test")
public class TransactionProcessingJobListenerTest {

	@Mock
	private JobExecution jobExecution;
	
	@Mock
	private StepExecution stepExecution;
	
	private TransactionProcessingJobListener jobListener = new TransactionProcessingJobListener();
	
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
	
	@Before
    public void setup() {
        MockitoAnnotations.initMocks(this);//so that Mockito runs with SpringJUnit
        
        List<StepExecution> stepsList = new ArrayList<StepExecution>();
        stepsList.add(stepExecution);
        when(stepExecution.getStepName()).thenReturn(TransactionProcessingConstants.STEP_1);
        ExecutionContext exec = new ExecutionContext();
        exec.put(TransactionProcessingConstants.INPUT_FILE_NAME_KEY, "file:"+inputDirectory+"//someNonExistent.txt");
        when(stepExecution.getExecutionContext()).thenReturn(exec);
        when(jobExecution.getStepExecutions()).thenReturn(stepsList);
    }
	
	@Test
	public void testAfterJob() {
		jobListener.afterJob(jobExecution);
	}
}
