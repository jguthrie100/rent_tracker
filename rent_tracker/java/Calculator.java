import java.lang.IllegalArgumentException;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.lang.Math;

class Calculator {
  
  Calculator() {
    
  }
  
  public double getIncome(BankAccount bAcc) {
    return getIncome(bAcc, bAcc.getTransactionCollection().getDateFrom(), bAcc.getTransactionCollection().getDateTo());
  }
  
  public double getIncome(BankAccount bAcc, Date dateFrom, Date dateTo) throws IllegalArgumentException {
    double income = 0.0;
    
    if(dateFrom.after(dateTo) || dateFrom.before(bAcc.getTransactionCollection().getDateFrom()) || dateTo.after(bAcc.getTransactionCollection().getDateTo())) {
      throw new IllegalArgumentException("Please check you have entered correct dates");
    }
    
    for(TransactionRecord tr : bAcc.getTransactionCollection().getTransactionRecords()) {
      if((tr.getDate().equals(dateFrom) || tr.getDate().after(dateFrom)) && (tr.getDate().equals(dateTo) || tr.getDate().before(dateTo))) {
        if(tr.getAmount() > 0.0) {
          income += tr.getAmount();
        }
      }
    }
    return Math.round(income*100)/100.0;
  }
  
  public double getOutgoings(BankAccount bAcc) {
    return getOutgoings(bAcc, bAcc.getTransactionCollection().getDateFrom(), bAcc.getTransactionCollection().getDateTo());
  }
  
  public double getOutgoings(BankAccount bAcc, Date dateFrom, Date dateTo) throws IllegalArgumentException {
    double outgoings = 0.0;
    
    if(dateFrom.after(dateTo) || dateFrom.before(bAcc.getTransactionCollection().getDateFrom()) || dateTo.after(bAcc.getTransactionCollection().getDateTo())) {
      throw new IllegalArgumentException("Please check you have entered valid dates");
    }
    
    for(TransactionRecord tr : bAcc.getTransactionCollection().getTransactionRecords()) {
      if((tr.getDate().equals(dateFrom) || tr.getDate().after(dateFrom)) && (tr.getDate().equals(dateTo) || tr.getDate().before(dateTo))) {
        if(tr.getAmount() < 0.0) {
          outgoings += tr.getAmount();
        }
      }
    }
    return Math.round(outgoings*100)/100.0;
  }
}