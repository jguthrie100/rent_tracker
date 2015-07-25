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
}