package com.greaterbank.transactionprocessing.writer;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;

import com.greaterbank.transactionprocessing.model.TransactionReportModel;

/**
 * This is a writer class for future expansion.
 * 
 * Just printing the report model which
 * gets updated after every record is read
 * 
 * @author NitinKalra
 *
 */
public class TransactionItemWriter implements ItemWriter<TransactionReportModel> {
	private static final Logger log = Logger.getLogger(TransactionItemWriter.class.getName());

	@Value("#{stepExecution}")
	private StepExecution stepExecution;
	
	public void write(List<? extends TransactionReportModel> reportModelList) throws Exception {
		log.info("reportModel in Writer - " + reportModelList);
		
	}

}
