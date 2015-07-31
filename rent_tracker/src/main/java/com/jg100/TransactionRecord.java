import java.util.Date;

class TransactionRecord {
  private String bankAccountID, type, chequeNum, payee, memo;
  private Date date;
  private int id;
  private Double amount;
  
  TransactionRecord(String bankAccountID, Date date, int id, String type, String chequeNum, String payee, String memo, Double amount) {
    this.bankAccountID = bankAccountID;
    this.date = date;
    this.id = id;
    this.type = type;
    this.chequeNum = chequeNum;
    this.payee = payee;
    this.memo = memo;
    this.amount = amount;
  }
  
  public String getBankAccountID() {
    return this.bankAccountID;
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
  
  public String toString() {
    return (getDate().toString() + "; ID: " + getID() + "; Type: " + getType()
            + "; Cheque Num: " + getChequeNum() + "; Payee: " + getPayee() + "; Memo: "
            + getMemo() + "; Amount: " + getAmount());
  }
}