package com.greaterbank.transactionprocessing.model;

import org.springframework.stereotype.Component;

/**************************
 * This class will map
 * to report model 
 * of customer transactions 
 * @author NitinKalra
 *
 **************************/
@Component
public class TransactionReportModel extends TransactionFileModel{

	private String 	fileProcessed;
	private Integer totalAccounts;
	private Double 	totalCredits;
	private Double 	totalDebits;
	private Integer skippedTransactions;
	
	
	public String getFileProcessed() {
		return fileProcessed;
	}
	public void setFileProcessed(String fileProcessed) {
		this.fileProcessed = fileProcessed;
	}
	public Integer getTotalAccounts() {
		return totalAccounts;
	}
	public void setTotalAccounts(Integer totalAccounts) {
		this.totalAccounts = totalAccounts;
	}
	public Double getTotalCredits() {
		return totalCredits;
	}
	public void setTotalCredits(Double totalCredits) {
		this.totalCredits = totalCredits;
	}
	public Double getTotalDebits() {
		return totalDebits;
	}
	public void setTotalDebits(Double totalDebits) {
		this.totalDebits = totalDebits;
	}
	public Integer getSkippedTransactions() {
		return skippedTransactions;
	}
	public void setSkippedTransactions(Integer skippedTransactions) {
		this.skippedTransactions = skippedTransactions;
	}
	@Override
	public String toString() {
		return "File Processed :" + fileProcessed + "\n" +
			   "Total Accounts :" + totalAccounts + "\n" +
			   "Total Credits  :" + totalCredits + "\n" + 
			   "Total Debits   :" + totalDebits + "\n" +
			   "Skipped Transactions:" + skippedTransactions;
	}
	
	
}
