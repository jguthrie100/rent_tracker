package com.jg100.model;

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
}