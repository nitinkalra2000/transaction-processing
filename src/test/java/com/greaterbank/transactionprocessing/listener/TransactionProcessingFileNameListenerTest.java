package com.greaterbank.transactionprocessing.listener;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.greaterbank.transactionprocessing.constant.TransactionProcessingConstants;
import com.greaterbank.transactionprocessing.model.TransactionReportModel;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/config/transaction-config.xml")
@ActiveProfiles("test")
public class TransactionProcessingFileNameListenerTest {

	@Mock
	private StepExecution stepExecution;
	
	@Mock
	private TransactionReportModel reportModel = new TransactionReportModel();
	
	@InjectMocks
	private TransactionProcessingFileNameListener fileNameListener = new TransactionProcessingFileNameListener();
	
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
        fileNameListener.setInputDirectory(inputDirectory);
        fileNameListener.setOutputDirectory(outputDirectory);
        fileNameListener.setProcessedDirectory(processedDirectory);
        fileNameListener.setFileNamePattern(fileNamePattern);
        fileNameListener.setWaitTimeFileProcessing(waitTimeFileProcessing);
        
        when(stepExecution.getExecutionContext()).thenReturn(new ExecutionContext());
    }
	
	@Test
	public void testFetchFilesFromPatternSuccess() throws IOException, InterruptedException {
		fileNameListener.fetchFilesFromPattern(stepExecution);
		
		assertNotNull(stepExecution.getExecutionContext().get(TransactionProcessingConstants.INPUT_FILE_NAME_KEY));
	}
	
	@Test
	public void testWriteReport() throws IOException {
		ExecutionContext execContext = new ExecutionContext();
		execContext.put(TransactionProcessingConstants.INPUT_FILE_NAME_KEY, 
				"file:F:\\MyData\\BitBucket_Repo\\TransactionProcessingCode\\"
				+ "src\\test\\resources\\testsamples\\pending\\finance_customer_transactions-02082107T0900.csv");
		
		when(stepExecution.getExecutionContext()).thenReturn(execContext);
		fileNameListener.writeReport(stepExecution);
	}
	
}
