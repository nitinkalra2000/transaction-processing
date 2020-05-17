package com.greaterbank.transactionprocessing.model;

import org.springframework.stereotype.Component;

/*************
 * This class will map
 * to Input file of Customer
 * Transactions
 * @author NitinKalra
 *
 */
@Component
public class TransactionFileModel {

	private String customerAccount;
	private String transactionAmount;
	
	public String getCustomerAccount() {
		return customerAccount;
	}
	public void setCustomerAccount(String customerAccount) {
		this.customerAccount = customerAccount;
	}
	public String getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	
	@Override
	public String toString() {
		return "TransactionFileModel [customerAccount=" + customerAccount + ", transactionAmount=" + transactionAmount
				+ "]";
	}
	
	
}
