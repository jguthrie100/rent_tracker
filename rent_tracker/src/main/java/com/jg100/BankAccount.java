import java.lang.IllegalArgumentException;

class BankAccount {
  private String accountId = "";
  private Double balance = 0.0;
  private TransactionCollection tCollection;
  
  BankAccount() {
    tCollection = new TransactionCollection();
  }
  
  public void setAccountId(String accountId) {
    if(accountId == null || accountId.isEmpty()) {
      throw new IllegalArgumentException("Error: Account ID cannot be null or empty");
    }
    this.accountId = accountId;
  }
  
  public String getAccountId() {
    return this.accountId;
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