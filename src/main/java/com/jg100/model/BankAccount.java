package com.jg100.model;

import java.util.Date;

/**
 * Class that models a bank account.
 * Contains information such as the sort code and account number (part of accountId) and account balance
 * All the transactions (rent payments, insurance payments, mortgage payments etc) belonging to this bank account 
 * will be stored in the TransactionCollection
 */
public class BankAccount {
  private String accountId = "";
  private Double balance = 0.0;
  private TransactionCollection tCollection;
  
  /** Constructor creates a new TransactionCollection object */
  public BankAccount() {
    tCollection = new TransactionCollection();
  }
  
  public void setAccountId(String accountId) {
    if(accountId == null || accountId.isEmpty()) {
      throw new IllegalArgumentException("Error: Account ID cannot be null or empty");
    }
    this.accountId = accountId;
  }
  
  public String getAccountId() {
    return this.accountId;
  }
  
  public void setBalance(Double balance) {
    this.balance = balance;
  }
  
  public double getBalance() {
    return this.balance;
  }
  
  public TransactionCollection getTransactionCollection() {
    return this.tCollection;
  }
  
  public double getIncome() {
    return getIncome(getTransactionCollection().getDateFrom(), getTransactionCollection().getDateTo());
  }
  
  public double getIncome(Date dateFrom, Date dateTo) {
    double income = 0.0;
    
    try {
      for(Transaction tr : getTransactionCollection().getTransactions(dateFrom, dateTo)) {
        if(tr.getAmount() > 0.0) {
          income += tr.getAmount();
        }
      }
    } catch (IllegalArgumentException e) {
      throw e;
    }
    
    return income;
  }
  
  public double getOutgoings() {
    return getOutgoings(getTransactionCollection().getDateFrom(), getTransactionCollection().getDateTo());
  }
  
  public double getOutgoings(Date dateFrom, Date dateTo) {
    double outgoings = 0.0;
    
    try {
      for(Transaction tr : getTransactionCollection().getTransactions(dateFrom, dateTo)) {
        if(tr.getAmount() < 0.0) {
          outgoings += tr.getAmount();
        }
      }
    } catch (IllegalArgumentException e) {
      throw e;
    }
    
    return outgoings;
  }
}