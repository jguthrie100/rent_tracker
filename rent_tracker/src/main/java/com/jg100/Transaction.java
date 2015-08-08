import java.util.Date;

class Transaction {
  private String bankAccountId, type, chequeNum, payee, memo;
  private Date date;
  private int id;
  private Double amount;
  
  Transaction(String bankAccountId, Date date, int id, String type, String chequeNum, String payee, String memo, Double amount) {
    // Use private setter methods to allow for better exception handling
    setBankAccountId(bankAccountId);
    setDate(date);
    setId(id);
    setType(type);
    setChequeNum(chequeNum);
    setPayee(payee);
    setMemo(memo);
    setAmount(amount);
  }
  
  public String getBankAccountId() {
    return this.bankAccountId;
  }
  
  private void setBankAccountId(String bankAccountId) {
    if(bankAccountId == null || bankAccountId.isEmpty()) {
      throw new IllegalArgumentException("Error: Bank Account ID cannot be null or empty");
    }
    this.bankAccountId = bankAccountId;
  }
  
  /** FullId is a combination of bank account id and transaction id.
   *   Ensures id is unique when comparing to transactions from other bank accounts */
  public String getFullId() {
    return this.bankAccountId.replace("-", "") + id;
  }
  
  public Date getDate() {
    return this.date;
  }
  
  private void setDate(Date date) {
    if(date == null) {
      throw new IllegalArgumentException("Error: Transaction date cannot be null or empty");
    }
    this.date = date;
  }
  
  public int getId() {
    return this.id;
  }
  
  private void setId(int id) {
    if(id < 1) {
      throw new IllegalArgumentException("Error: Transaction ID must be greater than or equal to 1");
    }
    this.id = id;
  }
  
  public String getType() {
    return this.type;
  }
  
  private void setType(String type) {
    if(type == null || type.isEmpty()) {
      throw new IllegalArgumentException("Error: Transaction type cannot be null or empty");
    }
    this.type = type;
  }
  
  public String getChequeNum() {
    return this.chequeNum;
  }
  
  private void setChequeNum(String chequeNum) {
    // Cheque num is allowed to be an empty string
    if(chequeNum == null) {
      throw new IllegalArgumentException("Error: Cheque Num cannot be null");
    }
    this.chequeNum = chequeNum;
  }
  
  public String getPayee() {
    return this.payee;
  }
  
  private void setPayee(String payee) {
    // Payee is allowed to be an empty string
    if(payee == null) {
      throw new IllegalArgumentException("Error: Payee cannot be null");
    }
    this.payee = payee;
  }
  
  public String getMemo() {
    return this.memo;
  }
  
  private void setMemo(String memo) {
    // Memo is allowed to be an empty string
    if(memo == null) {
      throw new IllegalArgumentException("Error: Memo cannot be null");
    }
    this.memo = memo;
  }
  
  public Double getAmount() {
    return this.amount;
  }
  
  private void setAmount(double amount) {
    this.amount = amount;
  }
  
  public String toString() {
    return (getDate().toString() + "; ID: " + getId() + "; Type: " + getType()
            + "; Cheque Num: " + getChequeNum() + "; Payee: " + getPayee() + "; Memo: "
            + getMemo() + "; Amount: " + getAmount());
  }
}