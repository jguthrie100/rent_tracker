import java.util.Date;
import java.lang.Math;

class TransactionRecord {
  private Date date;
  private String type, chequeNum, payee, memo;
  private int id;
  private Double amount;
  
  TransactionRecord(Date date, int id, String type, String chequeNum, String payee, String memo, Double amount) {
    this.date = date;
    this.id = id;
    this.type = type;
    this.chequeNum = chequeNum;
    this.payee = payee;
    this.memo = memo;
    this.amount = Math.round(amount*100)/100.0;
  }
  
  public Date getDate() {
    return this.date;
  }
  
  public int getID() {
    return this.id;
  }
  
  public String getType() {
    return this.type;
  }
  
  public String getChequeNum() {
    return this.chequeNum;
  }
  
  public String getPayee() {
    return this.payee;
  }
  
  public String getMemo() {
    return this.memo;
  }
  
  public Double getAmount() {
    return this.amount;
  }
}