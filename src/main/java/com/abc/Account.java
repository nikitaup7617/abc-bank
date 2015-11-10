package com.abc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.*;

public class Account {
    //No way to determine if available balance is less than what is
	//withdrawn from the account
	public double currentBalance; 
	private static final double DAYS_IN_YEAR_INTEREST=365;
	
	//Vaiable reprsenting account types
    public static final int CHECKING = 0;
    public static final int SAVINGS = 1;
    public static final int MAXI_SAVINGS = 2;
    
    //Variable representing threshold amount where
    //interest rate changes for various accounts
    private static final int FIRST_INCREASED_INTEREST_AMOUNT=1000;
    private static final int SECOND_INCREASED_INTEREST_AMOUNT=2000;
    
    //Variables representing different interest rates for 
    //different amounts in different accounts
    private static final double CHECKING_RATE=0.001;
    private static final double SAVING_1000_RATE=0.001;
    private static final double SAVING_MORE_THAN_1000_RATE=0.002;
    private static final double MAXI_SAVING_UPTO_1000=0.02;
    private static final double MAXI_SAVING_MORE_THAN_1000=0.05;
    private static final double MAXI_SAVING_MORE_THAN_2000=0.10;
    

    private final int accountType;
    public List<Transaction> transactions;

    public Account(int accountType) {
        this.accountType = accountType;
        this.currentBalance=0;
        this.transactions = new ArrayList<Transaction>();
    }
    
    public Account(int accountType,int currentBalance) {
        this.accountType = accountType;
        this.transactions = new ArrayList<Transaction>();
        deposit(currentBalance);
    }

    
    public void deposit(double amount) {
    	
        if (amount <= 0) {
            System.out.println("------Amount to be deposited must be grater greater than 0------");
        } 
        else {
        	currentBalance+=amount;
        	Transaction depositTransaction=new Transaction(amount,currentBalance);
            depositTransaction.doTransaction(amount);
        	transactions.add(depositTransaction);
        }
    }

    public void withdraw(double amount) {
    if (amount <= 0) {
    	System.out.println("------Amount to be withdrawn must be grater greater than 0------");
    }
    //checks whether the amount withdrawn is greater than available balance
    //Assumed over-draft is not applicable
    else if(amount > currentBalance){
    	System.out.println("------Insufficient balance------");
    }
    else {
    	currentBalance+=-amount;
    	Transaction withdrawTransaction=new Transaction(-amount,currentBalance);
    	withdrawTransaction.doTransaction(amount);
        transactions.add(withdrawTransaction);
    }
}
    
    /**
     * This method checks if account has sufficient balance then initiates 
     * the transfer process. 
     * 
     * @param fromAccount
     * @param toAccount
     * @param amount
     */
    public void transferBetweenAccounts(Account fromAccount,Account toAccount,double amount){
      if(fromAccount.getCurrentBalance()>=amount){
    	  fromAccount.setCurrentBalance(fromAccount.getCurrentBalance()-amount);
    	  Transaction withdrawTransaction=new Transaction(-amount,currentBalance);
      	  withdrawTransaction.doTransaction(amount);
          List<Transaction> transactionsFrom=fromAccount.getTransactions();
          transactionsFrom.add(withdrawTransaction);
          
          toAccount.setCurrentBalance(toAccount.getCurrentBalance()+amount);
      	  Transaction depositTransaction=new Transaction(amount,currentBalance);
      	  depositTransaction.doTransaction(amount);
          List<Transaction> transactionsTo=fromAccount.getTransactions();
          transactionsTo.add(depositTransaction);
          
      }
      else{
    	  System.out.println("---Insufficient funds in "+fromAccount.getAccountTypeName(fromAccount.getAccountType())+"---");
      }
    }
    
    public double interestEarned() {
        double amount = currentBalance;
        double interetEarned=0;
        switch(accountType){
            case SAVINGS:
                if (amount <= 1000){
                	interetEarned=amount * SAVING_1000_RATE;
                    return interetEarned;
                }    
                else{
                    interetEarned= FIRST_INCREASED_INTEREST_AMOUNT*SAVING_1000_RATE + (amount-FIRST_INCREASED_INTEREST_AMOUNT) * SAVING_MORE_THAN_1000_RATE;
                    return interetEarned;
                }
            case MAXI_SAVINGS:
//                if (amount <= FIRST_INCREASED_INTEREST_AMOUNT){
//                 interetEarned = amount * MAXI_SAVING_UPTO_1000;
//                 return interetEarned;
//                }
//                else if (amount <= SECOND_INCREASED_INTEREST_AMOUNT){
//                	interetEarned = (MAXI_SAVING_UPTO_1000*FIRST_INCREASED_INTEREST_AMOUNT) + (amount-1000) * MAXI_SAVING_MORE_THAN_1000;
//                    return interetEarned;
//                }
//                else{
//                	interetEarned = MAXI_SAVING_MORE_THAN_1000*SECOND_INCREASED_INTEREST_AMOUNT + (amount-2000) * 0.1;
//                    return interetEarned;
//                }
            //Changed the interest rate as per the new requirement
            	
            boolean flag=false;	
            for(Transaction transaction:transactions){
            	long diff = transaction.getTransactionDate().getTime()-(new Date().getTime());
                long differenceDays= TimeUnit.DAYS.convert(diff,TimeUnit.MILLISECONDS);
            	if(differenceDays<=10 && transaction.getAmount()<0){
            		flag=true;
            		break;
            	}
            	
            }
            if(flag){
            	interetEarned = amount * CHECKING_RATE;
        		return interetEarned;
            }
            else{
            interetEarned=MAXI_SAVING_MORE_THAN_1000*amount;
        	return interetEarned;
            }	
            case CHECKING:
            	interetEarned=amount * CHECKING_RATE;
            	return interetEarned;
            default:
                return 0;
        }
    }

    public double sumTransactions() {
       return checkIfTransactionsExist(true);
    }

    private double checkIfTransactionsExist(boolean checkAll) {
        double amount = 0.0;
        for (Transaction t: transactions)
            amount += t.amount;
        return amount;
    }

    public int getAccountType() {
        return accountType;
        
    }
    
    public String getAccountTypeName(int accontType){
    	
    	switch(accontType){
    	case 0: return "Checking Account";
    	case 1: return "Saving Account";
    	case 2: return "Max-Saving Account";
     }
      return "";	
    }
    
    public double getCurrentBalance(){
    	return currentBalance;
    	    	
    }
    
    public void setCurrentBalance(double currentBalance){
    	this.currentBalance=currentBalance;
    	    	
    }

    public List<Transaction> getTransactions(){
    	return transactions;
    }

	public double accrueDailyInterest() {
		double dailyInterest=interestEarned()/DAYS_IN_YEAR_INTEREST;
		deposit(dailyInterest);
		return currentBalance;
		
	}
}
