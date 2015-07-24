class TransactionRecord {
  private String date, type, chequeNum, payee, memo;
  private Double amount;
  private int id;
  
  TransactionRecord(String date, int id, String type, String chequeNum, String payee, String memo, Double amount) {
    this.date = date;
    this.id = id;
    this.type = type;
    this.chequeNum = chequeNum;
    this.payee = payee;
    this.memo = memo;
    this.amount = amount;
  }
  
  public String getDate() {
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
  
  public void tester(String str) {
    System.out.println(str);
  }
}