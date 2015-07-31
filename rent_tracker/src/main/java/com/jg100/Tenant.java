import java.util.ArrayList;
import java.util.Date;

/** Tenant object to model each of the tenants */
class Tenant {
  private String name, paymentHandle, phoneNum, email;
  private double rentAmount;
  private int rentFrequency;
  private Date startDate, endDate;   // Date when tenant moved in and moved out
  private ArrayList<TransactionRecord> rentPayments;
  
  public Tenant(String name, String paymentHandle, String phoneNum, String email, Date startDate, double rentAmount, int rentFrequency) {
    rentPayments = new ArrayList<TransactionRecord>();
    this.name = name;
    this.paymentHandle = paymentHandle;
    this.phoneNum = phoneNum;
    this.email = email;
    this.startDate = startDate;
    this.rentAmount = rentAmount;
    this.rentFrequency = rentFrequency;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  /** Name that appears on the statement when they pay rent */
  public String getPaymentHandle() {
    return this.paymentHandle;
  }
  
  public void setPaymentHandle(String paymentHandle) {
    this.paymentHandle = paymentHandle;
  }
  
  public String getPhoneNum() {
    return this.phoneNum;
  }
  
  public void setPhoneNum() {
    this.phoneNum = phoneNum;
  }
  
  public String getEmail() {
    return this.email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
  public Date getStartDate() {
    return this.startDate;
  }
  
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }
  
  public Date getEndDate() {
    return this.endDate;
  }
  
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }
  
  public double getRentAmount() {
    return this.rentAmount;
  }
  
  public void setRentAmount(double rentAmount) {
    this.rentAmount = rentAmount;
  }
  
  public int getRentFrequency() {
    return this.rentFrequency;
  }
  
  public void setRentFrequency(int rentFrequency) {
    this.rentFrequency = rentFrequency;
  }
  
  public ArrayList<TransactionRecord> getRentPayments() {
    return this.rentPayments;
  }
}