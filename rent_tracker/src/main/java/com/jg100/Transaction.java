import java.util.Date;

class Transaction {
  private String bankAccountId, type, chequeNum, payee, memo;
  private Date date;
  private int id;
  private Double amount;
  
  Transaction(String bankAccountId, Date date, int id, String type, String chequeNum, String payee, String memo, Double amount) {
    this.bankAccountId = bankAccountId;
    this.date = date;
    this.id = id;
    this.type = type;
    this.chequeNum = chequeNum;
    this.payee = payee;
    this.memo = memo;
    this.amount = amount;
  }
  
  public String getBankAccountId() {
    return this.bankAccountId;
  }
  
  /** FullId is a combination of bank account id and transaction id.
   *   Ensures id is unique when including transactions from other bank accounts */
  public String getFullId() {
    return this.bankAccountId.replace("-", "") + id;
  }
  
  public Date getDate() {
    return this.date;
  }
  
  public int getId() {
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
    return (getDate().toString() + "; ID: " + getId() + "; Type: " + getType()
            + "; Cheque Num: " + getChequeNum() + "; Payee: " + getPayee() + "; Memo: "
            + getMemo() + "; Amount: " + getAmount());
  }
}