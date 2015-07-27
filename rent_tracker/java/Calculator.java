import java.lang.IllegalArgumentException;
import java.lang.Math;
import java.util.Date;

class Calculator {
  
  Calculator() {
    
  }
  
  public double getIncome(BankAccount bAcc) {
    return getIncome(bAcc, bAcc.getTransactionCollection().getDateFrom(), bAcc.getTransactionCollection().getDateTo());
  }
  
  public double getOutgoings(BankAccount bAcc) {
    return getOutgoings(bAcc, bAcc.getTransactionCollection().getDateFrom(), bAcc.getTransactionCollection().getDateTo());
  }
  
  public double getIncome(BankAccount bAcc, Date dateFrom, Date dateTo) throws IllegalArgumentException {
    double income = 0.0;
    
    try {
      for(TransactionRecord tr : bAcc.getTransactionCollection().getTransactionRecords(dateFrom, dateTo)) {
        if(tr.getAmount() > 0.0) {
          income += tr.getAmount();
        }
      }
    } catch (IllegalArgumentException e) {
      throw e;
    }
    
    return income;
  }
  
  public double getOutgoings(BankAccount bAcc, Date dateFrom, Date dateTo) throws IllegalArgumentException {
    double outgoings = 0.0;
    
    try {
      for(TransactionRecord tr : bAcc.getTransactionCollection().getTransactionRecords(dateFrom, dateTo)) {
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