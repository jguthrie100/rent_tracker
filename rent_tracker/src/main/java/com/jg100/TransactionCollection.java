import java.util.ArrayList;
import java.util.Date;

/**
 * Class that stores a list of transactions that are found in an internet banking CSV file 
 *  The dateFrom, dateTo values are essentially the boundaries of the collection. All the Transactions in the collection 
 *  must lie between or on those dates
 */
class TransactionCollection {
  private ArrayList<Transaction> tRecords;
  private Date dateFrom, dateTo;
  
  /** Constructor creates new empty collection of transactions */
  TransactionCollection() {
    tRecords = new ArrayList<>();
  }
  
  /** Constructor creates new empty collection of transactions that are between given dates */
  TransactionCollection(Date dateFrom, Date dateTo) {
    /* Use setter methods for improved exception handling */
    setDateFrom(dateFrom);
    setDateTo(dateTo);
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
  
  /**
   * Returns ArrayList of all Transaction objects stored in this collection 
   */
  public ArrayList<Transaction> getTransactions() {
    return this.tRecords;
  }
  
  /**
   * Returns a copy of the Transaction arraylist containing only the entries between the given dates
   * 
   * @param dateFrom  Date from which the list of Transactions should start 
   * @param dateTo    Date to which the list of Transactions should go until
   * @return          ArrayList containing a list of Transactions between (and including) the given dates
   */
  public ArrayList<Transaction> getTransactions(Date dateFrom, Date dateTo) {
    ArrayList<Transaction> trOut = new ArrayList<Transaction>();
    
    /* Exception handling to ensure parameter Dates are valid */
    if(dateFrom == null || dateTo == null) {
      throw new IllegalArgumentException("Error: dates cannot be passed as null values");
    }if(dateFrom.after(dateTo) || dateFrom.before(this.getDateFrom()) || dateTo.after(this.getDateTo())) {
      throw new IllegalArgumentException("Error: Please check you have entered valid dates; Must be between " + this.getDateFrom() + " and " + this.getDateTo());
    }
    
    /* Loop through transaction list and add relevant transactions to new ArrayList */
    for(Transaction tr : this.tRecords) {
      if(!tr.getDate().before(dateFrom) && !tr.getDate().after(dateTo)) {
        trOut.add(tr);
      }
    }
    return trOut;
  }
}