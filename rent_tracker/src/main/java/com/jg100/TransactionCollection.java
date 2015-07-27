import java.lang.IllegalArgumentException;
import java.util.ArrayList;
import java.util.Date;

class TransactionCollection {
  private ArrayList<TransactionRecord> tRecords;
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
  
  public ArrayList<TransactionRecord> getTransactionRecords() {
    return this.tRecords;
  }
  
  /**
   * Returns a copy of the TransactionRecord arraylist containing only the entries between the given dates
   */
  public ArrayList<TransactionRecord> getTransactionRecords(Date dateFrom, Date dateTo) throws IllegalArgumentException {
    ArrayList<TransactionRecord> trOut = new ArrayList<TransactionRecord>();
    
    if(dateFrom.after(dateTo) || dateFrom.before(this.getDateFrom()) || dateTo.after(this.getDateTo())) {
      throw new IllegalArgumentException("Please check you have entered valid dates; Must be between " + this.getDateFrom() + " and " + this.getDateTo());
    }
    
    for(TransactionRecord tr : this.tRecords) {
      if((tr.getDate().equals(dateFrom) || tr.getDate().after(dateFrom)) && (tr.getDate().equals(dateTo) || tr.getDate().before(dateTo))) {
        trOut.add(tr);
      }
    }
    return trOut;
  }
}