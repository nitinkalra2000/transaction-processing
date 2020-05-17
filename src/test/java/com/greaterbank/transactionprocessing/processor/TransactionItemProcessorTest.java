package com.greaterbank.transactionprocessing.processor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
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
import com.greaterbank.transactionprocessing.model.TransactionFileModel;
import com.greaterbank.transactionprocessing.model.TransactionReportModel;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/config/transaction-config.xml")
@ActiveProfiles("test")
public class TransactionItemProcessorTest {
	
	@Mock
	private JobExecution jobExecution;
	
	@Mock
	private StepExecution stepExecution;
	
	@Mock
	private Map<Integer, Double> customersTransactions;
	
	@Value("#{inputDirectory}")
	private String inputDirectory;
	
	@InjectMocks
	private TransactionItemProcessor processor = new TransactionItemProcessor();
	
	private TransactionFileModel fileModel = new TransactionFileModel();

	@Before
    public void setup() {
        MockitoAnnotations.initMocks(this);//so that Mockito runs with SpringJUnit
        
        fileModel.setCustomerAccount("123456789");
        fileModel.setTransactionAmount("1000");
        
        when(customersTransactions.containsKey("123456789")).thenReturn(true);
        when(customersTransactions.get("123456789")).thenReturn(1000D);
        
        List<StepExecution> stepsList = new ArrayList<StepExecution>();
        stepsList.add(stepExecution);
        ExecutionContext exec = new ExecutionContext();
        exec.put(TransactionProcessingConstants.INPUT_FILE_NAME_KEY, "file:"+inputDirectory+"//someNonExistent.txt");
        when(stepExecution.getExecutionContext()).thenReturn(exec);
    }
	
	@Test
	public void testProcess() throws Exception {
		TransactionReportModel reportModel = processor.process(fileModel);
		
		assertNotNull(reportModel);
	}
	
	@Test
	public void testProcessInvalidAccount() throws Exception {
		fileModel.setCustomerAccount("ss123456789");
		TransactionReportModel reportModel = processor.process(fileModel);
		
		assertNull(reportModel);
	}
	
	@Test
	public void testProcessAccRepetition() throws Exception {

		TransactionReportModel reportModel = processor.process(fileModel);
        fileModel.setCustomerAccount("123456789");
        fileModel.setTransactionAmount("2000");
		
        reportModel = processor.process(fileModel);
		
		assertNotNull(reportModel);
		assertTrue(reportModel.getTotalCredits() == 3000D);
		assertNotNull(reportModel.getSkippedTransactions());
		assertNotNull(reportModel.getTotalDebits());
		assertNotNull(reportModel.getTotalAccounts());
		assertNotNull(reportModel.getFileProcessed());
		
	}
}
