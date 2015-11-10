package com.abc;

import java.util.Calendar;
import java.util.Date;

public class Transaction {
    public final double amount;

    private Date transactionDate;
    private double currentBalance;
    private String type;
     
    
    public Transaction(double amount,double currentBalance) {
        this.amount = amount;
        this.transactionDate = DateProvider.getInstance().now();
        doTransaction(amount);
    }
    
    public void doTransaction(double amount){
    	if(amount>0){
    		type="Deposit";
    	}else{
    		type="Withdraw";
    	}
    		
     	currentBalance+=amount;
        	
    }
    
    public String toString(){
    	return type+"---$"+amount+"---"+" running balance $"+currentBalance; 
    }
    
    
    public Date getTransactionDate(){
    	return transactionDate;
    }
    
    public double getAmount(){
    	return amount;
    }

}
