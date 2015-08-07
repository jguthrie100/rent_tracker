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
    this.dateFrom = dateFrom;
  }
  
  public Date getDateFrom() {
    return this.dateFrom;
  }
  
  public void setDateTo(Date dateTo) {
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
  public ArrayList<Transaction> getTransactions(Date dateFrom, Date dateTo) throws IllegalArgumentException {
    ArrayList<Transaction> trOut = new ArrayList<Transaction>();
    
    if(dateFrom.after(dateTo) || dateFrom.before(this.getDateFrom()) || dateTo.after(this.getDateTo())) {
      throw new IllegalArgumentException("Please check you have entered valid dates; Must be between " + this.getDateFrom() + " and " + this.getDateTo());
    }
    
    for(Transaction tr : this.tRecords) {
      if(!tr.getDate().before(dateFrom) && !tr.getDate().after(dateTo)) {
        trOut.add(tr);
      }
    }
    return trOut;
  }
}