package com.jg100;

import com.jg100.model.*;

import java.lang.Math;
import java.util.Date;

/**
 * Calculator class contains a number of methods that can be used to calculate various values, such as total payments or outgoings or whatnot
 */
class Calculator {
  
  /** Creates new Calculator object */
  Calculator() {
    
  }
  
  public double getIncome(BankAccount bAcc) {
    return getIncome(bAcc, bAcc.getTransactionCollection().getDateFrom(), bAcc.getTransactionCollection().getDateTo());
  }
  
  public double getIncome(BankAccount bAcc, Date dateFrom, Date dateTo) {
    double income = 0.0;
    
    try {
      for(Transaction tr : bAcc.getTransactionCollection().getTransactions(dateFrom, dateTo)) {
        if(tr.getAmount() > 0.0) {
          income += tr.getAmount();
        }
      }
    } catch (IllegalArgumentException e) {
      throw e;
    }
    
    return income;
  }
  
  public double getOutgoings(BankAccount bAcc) {
    return getOutgoings(bAcc, bAcc.getTransactionCollection().getDateFrom(), bAcc.getTransactionCollection().getDateTo());
  }
  
  public double getOutgoings(BankAccount bAcc, Date dateFrom, Date dateTo) {
    double outgoings = 0.0;
    
    try {
      for(Transaction tr : bAcc.getTransactionCollection().getTransactions(dateFrom, dateTo)) {
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