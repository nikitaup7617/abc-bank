package com.abc;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AccountTest {

	private static final double DOUBLE_DELTA = 1e-15;
	
    @Test
    public void testDeposit() {
    	Account account=new Account(0,0);
    	account.deposit(100);
    	assertEquals(100, account.getCurrentBalance(),DOUBLE_DELTA); 
    
    }
    
    @Test
    public void testWithdraw() {
    	Account account=new Account(0,100);
    	account.withdraw(50);
    	assertEquals(50, account.getCurrentBalance(),DOUBLE_DELTA); 
    
    }
    
    @Test
    public void testTransfer(){
    	Account fromAccount=new Account(0,100);
    	Account toAccount=new Account(1,100);
    	
    	fromAccount.transferBetweenAccounts(fromAccount, toAccount, 50);
    	assertEquals(150, toAccount.getCurrentBalance(),DOUBLE_DELTA); 
    
    	
    }
    
    @Test
    public void testAccureDailyBalance(){
    	Account account=new Account(2,3650000);
    	account.transactions.add(new Transaction(-100, account.getCurrentBalance()));
    	assertEquals(3650010, account.accrueDailyInterest(),DOUBLE_DELTA); 
    
    	
    }
    

    
}
