package com.greaterbank.transactionprocessing;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/****
 * Start the application.
 * 
 * It will initialize the spring config.
 * Then the task scheduler define in xml will
 * launch the job from TransactionProcessingScheduler
 * @author NitinKalra
 *
 */
public class TransactionProcessingApplication {

	private static final Logger log = Logger.getLogger(TransactionProcessingApplication.class.getName());

	
	public static void main(String[] args) {
		System.setProperty("spring.profiles.active", "dev");
		ApplicationContext 	ctx 		= new ClassPathXmlApplicationContext("spring/config/transaction-config.xml");
		
	}
	
}
