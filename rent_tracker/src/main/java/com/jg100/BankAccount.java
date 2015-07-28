class BankAccount {
  private String accountID = "";
  private Double balance = 0.0;
  private TransactionCollection tCollection;
  
  BankAccount() {
    tCollection = new TransactionCollection();
  }
  
  public void setAccountID(String accountID) {
    this.accountID = accountID;
  }
  
  public String getAccountID() {
    return this.accountID;
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