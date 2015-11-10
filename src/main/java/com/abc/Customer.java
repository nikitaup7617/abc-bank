package com.abc;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class Customer {
    private String name;
    private List<Account> accounts;

    public Customer(String name) {
        this.name = name;
        this.accounts = new ArrayList<Account>();
    }

    public String getName() {
        return name;
    }

    public Customer openAccount(Account account) {
    	
    	//Added logic for checking if account is already in existance for the 
    	//current customer
    	if(accounts.size()>0){
    		for(Account existingAccount:accounts){
    			if(existingAccount.getAccountType()==account.getAccountType()){
    			System.out.println("---Account already in existance---");
    			return this;
    		 }
    	  }
    	}
        accounts.add(account);
        return this;
    }

    public int getNumberOfAccounts() {
        return accounts.size();
    }

    public double totalInterestEarned() {
        double total = 0;
        for (Account a : accounts){
            total += a.interestEarned();
        }    
        return total;
    }

    public String getStatement() {
        String statement = null;
        statement = "Statement for " + name + "\n";
        double total = 0.0;
        for (Account a : accounts) {
            statement += "\n" + statementForAccount(a) + "\n";
            total += a.sumTransactions();
        }
        statement += "\nTotal In All Accounts " + toDollars(total);
        return statement;
    }

    private String statementForAccount(Account a) {
        String s = "";

       //Translate to pretty account type
        switch(a.getAccountType()){
            case Account.CHECKING:
                s += "Checking Account\n";
                break;
            case Account.SAVINGS:
                s += "Savings Account\n";
                break;
            case Account.MAXI_SAVINGS:
                s += "Maxi Savings Account\n";
                break;
        }

        //Now total up all the transactions
        double total = 0.0;
        for (Transaction t : a.transactions) {
            //As per division of responsibility the Transaction class should be responsible for
        	//taking care of producing balance and managing running balance functionality
        	//is placed in the code but to add functionality to existing code it is not used here
        	//if want to uncomment s+=t; and comment S+= next line although output for Junit will be differnt
        	//        	s+= t+"\n";
            s += "  " + (t.amount < 0 ? "withdrawal" : "deposit") + " " + toDollars(t.amount) + "\n";
            total += t.amount;
        }
        s += "Total " + toDollars(total);
        return s;
    }

    
    
    private String toDollars(double d){
        return String.format("$%,.2f", abs(d));
    }
    
    /**
     * Method for accuring interest in the balance on daily basis
     * This method can be bind with a manual trigger or automatic event
     * for performing operation at specific time of a day like 12 AM 
     *
     * @return
     */

	public double accrueDailyInterest() {
		double totalEarningToday=0;
		for(Account account:accounts){
			totalEarningToday+=account.accrueDailyInterest();
		}
		return totalEarningToday;
		
	}
}
