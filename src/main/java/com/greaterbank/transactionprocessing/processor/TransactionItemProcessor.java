package com.greaterbank.transactionprocessing.processor;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.greaterbank.transactionprocessing.constant.TransactionProcessingConstants;
import com.greaterbank.transactionprocessing.model.TransactionFileModel;
import com.greaterbank.transactionprocessing.model.TransactionReportModel;
import com.greaterbank.transactionprocessing.utils.TransactionProcessingUtils;

/*******************************************
 * Its a processor class which will
 * process the records one by one
 * and will generate the ReportModel
 * @author NitinKalra
 *
 ******************************************/
public class TransactionItemProcessor implements ItemProcessor<TransactionFileModel, TransactionReportModel>{
	
	private static final Logger log = Logger.getLogger(TransactionItemProcessor.class.getName());

	private static Map<Integer, Double> customersTransactions = new HashMap<Integer, Double>();
	private static int 		skippedTransactions = 0;
	private static Double 	totalCredits = 0.0;
	private static Double 	totalDebits  = 0.0;
	private static Integer  totalAccnts  = 0; 
	
	@Value("#{stepExecution}")
	private StepExecution stepExecution;
	
	
	private TransactionReportModel reportModel = new TransactionReportModel();
	
    public TransactionReportModel process(final TransactionFileModel transactions) throws Exception {
    	totalAccnts++;
        log.info("Converting (" + transactions + ")");
        String customerAcc = transactions.getCustomerAccount();
        String amount	   = transactions.getTransactionAmount();
        
        

        if(StringUtils.isNumeric(customerAcc) == false) {
        	log.info(customerAcc + " is not a valid account");
        	skippedTransactions++;
        	return null;
        } else {
        	Integer customerAccInt = Integer.valueOf(customerAcc);
        	Double  amountDbl      = Double.valueOf(amount);
        	if(customersTransactions.containsKey(customerAccInt)) {
        		Double newAmount = customersTransactions.get(customerAccInt) + amountDbl;
        		customersTransactions.put(customerAccInt, newAmount);
        	} else {
        		customersTransactions.put(customerAccInt, amountDbl);
        	}
        	
        	if(amountDbl < 0) {
        		totalDebits = totalDebits + amountDbl;
        	} else {
        		totalCredits = totalCredits + amountDbl;
        	}
        }
        
        String inputFileName = stepExecution.getExecutionContext().
        							   getString(TransactionProcessingConstants.INPUT_FILE_NAME_KEY);
        
        
        reportModel.setFileProcessed(TransactionProcessingUtils.fetchFileName(inputFileName));
        reportModel.setTotalAccounts(totalAccnts);
        reportModel.setTotalCredits(totalCredits);
        reportModel.setTotalDebits(totalDebits);
        reportModel.setSkippedTransactions(skippedTransactions);
        
        log.info(reportModel);
        return reportModel;
    }

}
