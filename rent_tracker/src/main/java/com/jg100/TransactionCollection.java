import java.lang.IllegalArgumentException;
import java.util.ArrayList;
import java.util.Date;

class TransactionCollection {
  private ArrayList<Transaction> tRecords;
  private Date dateFrom, dateTo;
  
  TransactionCollection() {
    tRecords = new ArrayList<>();
  }
  
  public void setDateFrom(Date dateFrom) {
    if(dateFrom == null) {
      throw new IllegalArgumentException("Error: Date From cannot be null or empty");
    } else if(getDateTo() != null && dateFrom.after(getDateTo())) {
      throw new IllegalArgumentException("Error: DateFrom cannot be set to a date after the DateTo date");
    }
    this.dateFrom = dateFrom;
  }
  
  public Date getDateFrom() {
    return this.dateFrom;
  }
  
  public void setDateTo(Date dateTo) {
    if(dateTo == null) {
      throw new IllegalArgumentException("Error: Date To cannot be null or empty");
    } else if(getDateFrom() != null && dateTo.before(getDateFrom())) {
      throw new IllegalArgumentException("Error: DateTo cannot be set to a date before the DateFrom date");
    }
    this.dateTo = dateTo;
  }
  
  public Date getDateTo() {
    return this.dateTo;
  }
  
  public ArrayList<Transaction> getTransactions() {
    return this.tRecords;
  }
  
  /**
   * Returns a copy of the Transaction arraylist containing only the entries between the given dates
   */
  public ArrayList<Transaction> getTransactions(Date dateFrom, Date dateTo) {
    ArrayList<Transaction> trOut = new ArrayList<Transaction>();
    
    if(dateFrom == null || dateTo == null) {
      throw new IllegalArgumentException("Error: dates cannot be passed as null values");
    }if(dateFrom.after(dateTo) || dateFrom.before(this.getDateFrom()) || dateTo.after(this.getDateTo())) {
      throw new IllegalArgumentException("Error: Please check you have entered valid dates; Must be between " + this.getDateFrom() + " and " + this.getDateTo());
    }
    
    for(Transaction tr : this.tRecords) {
      if(!tr.getDate().before(dateFrom) && !tr.getDate().after(dateTo)) {
        trOut.add(tr);
      }
    }
    return trOut;
  }
}