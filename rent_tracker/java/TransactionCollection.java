import java.util.ArrayList;

class TransactionCollection {
  private ArrayList<TransactionRecord> tRecords;
  private String dateFrom, dateTo;
  
  TransactionCollection() {
    tRecords = new ArrayList<>();
  }
  
  public void setDateFrom(String dateFrom) {
    this.dateFrom = dateFrom;
  }
  
  public String getDateFrom() {
    return this.dateFrom;
  }
  
  public void setDateTo(String dateTo) {
    this.dateTo = dateTo;
  }
  
  public String getDateTo() {
    return this.dateTo;
  }
  
  public ArrayList<TransactionRecord> getTransactionRecords() {
    return this.tRecords;
  }
}