import java.lang.IllegalArgumentException;
import org.joda.time.*;
import java.util.ArrayList;
import java.util.Date;

/** Tenant object to model each of the tenants */
class Tenant {
  private String name, paymentHandle, phoneNum, email;
  private double rentAmount;
  private int rentFrequency;
  private Date leaseStart, leaseEnd;   // Date when tenant moved in and moved out
  private ArrayList<Transaction> transactionList;
  
  public Tenant(String name, String paymentHandle, String phoneNum, String email, Date leaseStart, double rentAmount, int rentFrequency) {
    transactionList = new ArrayList<Transaction>();
    this.name = name;
    this.paymentHandle = paymentHandle;
    this.phoneNum = phoneNum;
    this.email = email;
    this.leaseStart = leaseStart;
    this.rentAmount = rentAmount;
    this.rentFrequency = rentFrequency;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    if(name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Error: Name cannot be null or empty");
    }
    this.name = name;
  }
  
  /** Name that appears on the statement when they pay rent */
  public String getPaymentHandle() {
    return this.paymentHandle;
  }
  
  public void setPaymentHandle(String paymentHandle) {
    if(paymentHandle == null || paymentHandle.isEmpty()) {
      throw new IllegalArgumentException("Error: Payment handle cannot be null or empty");
    }
    this.paymentHandle = paymentHandle;
  }
  
  public String getPhoneNum() {
    return this.phoneNum;
  }
  
  public void setPhoneNum(String phoneNum) {
    if(phoneNum == null || phoneNum.isEmpty()) {
      throw new IllegalArgumentException("Error: Phone number cannot be null or empty");
    }
    this.phoneNum = phoneNum;
  }
  
  public String getEmail() {
    return this.email;
  }
  
  public void setEmail(String email) {
    if(email == null || email.isEmpty()) {
      throw new IllegalArgumentException("Error: Email address cannot be null or empty");
    }
    this.email = email;
  }
  
  public Date getLeaseStart() {
    return this.leaseStart;
  }
  
  public void setLeaseStart(Date leaseStart) {
    if(leaseStart == null) {
      throw new IllegalArgumentException("Error: Lease start date cannot be null or empty");
    } else if(getLeaseEnd() != null && leaseStart.after(getLeaseEnd())) {
      throw new IllegalArgumentException("Error: Lease start date cannot be set to a date after the lease end date");
    }
    this.leaseStart = leaseStart;
  }
  
  public Date getLeaseEnd() {
    return this.leaseEnd;
  }
  
  /**
   * Lease end is allowed to be set as null, as tenant may be on open-ended lease, where and end date has not been decided
   */
  public void setLeaseEnd(Date leaseEnd) {
    if(leaseEnd != null) {
      if(leaseEnd.before(getLeaseStart())) {
        throw new IllegalArgumentException("Error: Lease end date cannot be set to a date before the lease start date");
      }
    }
    this.leaseEnd = leaseEnd;
  }
  
  public double getRentAmount() {
    return this.rentAmount;
  }
  
  public void setRentAmount(double rentAmount) {
    if(rentAmount < 0.0) {
      throw new IllegalArgumentException("Error: Rent amount must be greater than or equal to 0.0");
    }
    this.rentAmount = rentAmount;
  }
  
  public int getRentFrequency() {
    return this.rentFrequency;
  }
  
  /**
   * Rent frequency is basically how often (in weeks) the tenant pays rent. 
   * rentFrequency == 1 means they pay once a week,
   * rentFrequency == 2 means they pay once every 2 weeks etc
   */
  public void setRentFrequency(int rentFrequency) {
    if(rentFrequency <= 0) {
      throw new IllegalArgumentException("Error: Rent frequency must be set to 1 or more");
    }
    this.rentFrequency = rentFrequency;
  }
  
  public double getTotalRentPaid() {
    return getTotalRentPaid(this.leaseStart, new Date());
  }
  
  public double getTotalRentPaid(Date dateFrom, Date dateTo) {
    if(dateFrom == null || dateTo == null) {
      throw new IllegalArgumentException("Error: dates cannot be passed as null values");
    } else if(dateFrom.after(dateTo)) {
      throw new IllegalArgumentException("Error: dateFrom (" + dateFrom + ") is after dateTo (" + dateTo + ")");
    }
    
    double rentPaid = 0.0;
    
    for(Transaction tr : this.transactionList) {
      if((tr.getDate().equals(dateFrom) || tr.getDate().after(dateFrom)) && (tr.getDate().equals(dateTo) || tr.getDate().before(dateTo))) {
        rentPaid += tr.getAmount();
      }
    }
    
    return rentPaid;
  }
  
  public double getTotalRentExpected() {
    Date dateTo = new Date();
    if(this.leaseEnd != null) {
      dateTo = this.leaseEnd;
    }
    return getTotalRentExpected(this.leaseStart, dateTo);
  }
  
  public double getTotalRentExpected(Date dateFrom, Date dateTo) {
    if(dateFrom == null || dateTo == null) {
      throw new IllegalArgumentException("Error: dates cannot be passed as null values");
    } else if(dateFrom.after(dateTo)) {
      throw new IllegalArgumentException("Error: dateFrom (" + dateFrom + ") is after dateTo (" + dateTo + ")");
    }
    
    int days = Days.daysBetween(new DateTime(dateFrom), new DateTime(dateTo)).getDays();
    
    System.out.println("days: " + days);
    return (days * this.rentAmount / 7);
  }
  
  public ArrayList<Transaction> getTransactionList() {
    return this.transactionList;
  }
}