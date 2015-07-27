class BankAccount {
  private String accountNum = "";
  private Double balance = 0.0;
  private TransactionCollection tCollection;
  
  BankAccount() {
    tCollection = new TransactionCollection();
  }
  
  public void setAccountNum(String accountNum) {
    this.accountNum = accountNum;
  }
  
  public String getAccountNum() {
    return this.accountNum;
  }
  
  public void setBalance(Double balance) {
    this.balance = balance;
  }
  
  public double getBalance() {
    return this.balance;
  }
  
  public TransactionCollection getTransactionCollection() {
    return this.tCollection;
  }
}