package com.jg100.model;

import java.util.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/** Transaction class that models every payment that is recorded in the online banking CSV file */
public class Transaction {
  private String bankAccountId, type, chequeNum, payee, memo;
  private Date date;
  private int id;
  private Double amount;
  
  /**
   * Creates a new Transaction object (modelling a transaction on the bank account).
   *  - bankAccountId refers to the bank account that the transaction belongs to.
   *  - All other parameters relate to the columns in the bank account CSV file.
   *  - Values can only be set during construction, and after that they are read only
   * 
   * @param bankAccountId   String value relating to unique sort code/account number id of the bank account this transaction belongs to 
   * @param date            Date that the transaction was made
   * @param id              Unique id of the transaction
   * @param type            Type/Method of transaction 
   * @param chequeNum       
   * @param payee           String value referring to the payee of the payment
   * @param memo            Customisable memo for the transaction where further information relating to the transaction is stored
   * @param amount          Cash amount of the transaction. Positive value is an incoming payment; negative value is an outgoing payment
   */
  public Transaction(String bankAccountId, Date date, int id, String type, String chequeNum, String payee, String memo, Double amount) {
    // Use private setter methods to allow for better exception handling
    setBankAccountId(bankAccountId);
    setDate(date);
    setId(id);
    setType(type);
    setChequeNum(chequeNum);
    setPayee(payee);
    setMemo(memo);
    setAmount(amount);
  }
  
  public Transaction(String bankAccountId, int id) {
    setBankAccountId(bankAccountId);
    setId(id);
  }
  
  public String getBankAccountId() {
    return this.bankAccountId;
  }
  
  private void setBankAccountId(String bankAccountId) {
    if(bankAccountId == null || bankAccountId.isEmpty()) {
      throw new IllegalArgumentException("Error: Bank Account ID cannot be null or empty");
    }
    this.bankAccountId = bankAccountId;
  }
  
  /** FullId is a combination of bank account id and transaction id.
   *   Ensures id is unique when comparing to transactions from other bank accounts */
  public String getFullId() {
    return this.bankAccountId.replace("-", "") + id;
  }
  
  public Date getDate() {
    return this.date;
  }
  
  private void setDate(Date date) {
    if(date == null) {
      throw new IllegalArgumentException("Error: Transaction date cannot be null or empty");
    }
    this.date = date;
  }
  
  public int getId() {
    return this.id;
  }
  
  private void setId(int id) {
    if(id < 1) {
      throw new IllegalArgumentException("Error: Transaction ID must be greater than or equal to 1");
    }
    this.id = id;
  }
  
  public String getType() {
    return this.type;
  }
  
  private void setType(String type) {
    if(type == null || type.isEmpty()) {
      throw new IllegalArgumentException("Error: Transaction type cannot be null or empty");
    }
    this.type = type;
  }
  
  public String getChequeNum() {
    return this.chequeNum;
  }
  
  /**
   * chequeNum tends to always be left blank in the CSV file. Hence allow for an empty string 
   */
  private void setChequeNum(String chequeNum) {
    // Cheque num is allowed to be an empty string (but not null)
    if(chequeNum == null) {
      throw new IllegalArgumentException("Error: Cheque Num cannot be null");
    }
    this.chequeNum = chequeNum;
  }
  
  public String getPayee() {
    return this.payee;
  }
  
  private void setPayee(String payee) {
    // Payee is allowed to be an empty string
    if(payee == null) {
      throw new IllegalArgumentException("Error: Payee cannot be null");
    }
    this.payee = payee;
  }
  
  public String getMemo() {
    return this.memo;
  }
  
  /**
   * memo is an optional string entered by user at time of making the payment. Empty strings are hence valid
   */
  private void setMemo(String memo) {
    // Memo is allowed to be an empty string
    if(memo == null) {
      throw new IllegalArgumentException("Error: Memo cannot be null");
    }
    this.memo = memo;
  }
  
  public Double getAmount() {
    return this.amount;
  }
  
  private void setAmount(double amount) {
    this.amount = amount;
  }
  
  public String toString() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    DecimalFormat df = new DecimalFormat("0.00");
    
    return (
      "bankAccountId: " + this.bankAccountId + "; date: " + sdf.format(getDate()) + "; id: " + this.id + "; type: \"" + this.type +
      "\"; chequeNum: \"" + this.chequeNum + "\"; payee: \"" + this.payee + "\"; memo: \"" + this.memo + "\"; amount: " + df.format(this.amount)
    );
  }
}