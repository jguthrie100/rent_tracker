package com.jg100.model;
import com.jg100.model.*;
import com.jg100.parsers.*;

import org.joda.time.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Tenant class to model each tenant 
 * 
 * Each Tenant has information relating to their personal information (phone, email, rent amount etc),
 *  as well as a list of transactions/payments that they have made into the bank account
 */
public class Tenant {
  private String name, paymentHandle, phoneNum, email;
  private double weeklyRent;
  private int rentFrequency;
  private Date leaseStart, leaseEnd;   // Date when tenant moved in and moved out
  private ArrayList<Transaction> transactionList;
  
  /**
   * Constructor for Tenant object 
   *  - No null/blank entries are allowed
   * 
   * @param name           Name of the tenant 
   * @param paymentHandle  Unique string identifier to identify a tenant's payments in the CSV file
   * @param phoneNum       Tenant's phone number 
   * @param email          Tenant's email 
   * @param leaseStart     Date on which the tenant moved in / started to rent the house
   * @param weeklyRent     Weekly rent amount to be paid by tenant 
   * @param rentFrequency  How often (in weeks) the tenant pays rent. 1 means once a week, 2 means once every two weeks, etc
   */
  public Tenant(String name, String paymentHandle, String phoneNum, String email, Date leaseStart, double weeklyRent, int rentFrequency) {
    /* Use setter methods to improve exception handling */
    setName(name);
    setPaymentHandle(paymentHandle);
    setPhoneNum(phoneNum);
    setEmail(email);
    setLeaseStart(leaseStart);
    setWeeklyRent(weeklyRent);
    setRentFrequency(rentFrequency);
    
    transactionList = new ArrayList<Transaction>();
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
  
  public double getWeeklyRent() {
    return this.weeklyRent;
  }
  
  public void setWeeklyRent(double weeklyRent) {
    if(weeklyRent < 0.0) {
      throw new IllegalArgumentException("Error: Rent amount must be greater than or equal to 0.0");
    }
    this.weeklyRent = weeklyRent;
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
  
  /**
   * Return the total amount of rent that the tenant has paid (from when their lease started) up until today's date
   * 
   * @return  Double value referring to the amount of rent the tenant has paid
   */
  public double getTotalRentPaid() {
    return getTotalRentPaid(this.leaseStart, new Date());
  }
  
  /**
   * Returns out the total amount of rent paid by the tenant between two dates
   * 
   * @param dateFrom  Date from which the rent calculation should start 
   * @param dateTo    Date to which the rent calculation should go until
   * @return          The amount of rent that the tenant has paid between (and including) the two dates
   */
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
  
  /**
   * Returns the total amount of rent that the tenant should have paid to date 
   * Calculation starts from when the tenant's lease started and runs until either today's date, or the date on which their lease ended
   * 
   * @return Double value relating to the amount of rent that the tenant should have paid to date
   */
  public double getTotalRentExpected() {
    Date dateTo = new Date();
    if(this.leaseEnd != null) {
      dateTo = this.leaseEnd;
    }
    return getTotalRentExpected(this.leaseStart, dateTo);
  }
  
  /**
   * Returns the total amount of rent that the tenant should have paid between two dates
   * 
   * @param dateFrom  Date from which the rent calculation should start 
   * @param dateTo    Date to which the rent calculation should go until 
   * @return          Double value specifying the amount of rent the tenant should have paid between (and including) the two passed dates 
   */
  public double getTotalRentExpected(Date dateFrom, Date dateTo) {
    if(dateFrom == null || dateTo == null) {
      throw new IllegalArgumentException("Error: dates cannot be passed as null values");
    } else if(dateFrom.after(dateTo)) {
      throw new IllegalArgumentException("Error: dateFrom (" + dateFrom + ") is after dateTo (" + dateTo + ")");
    }
    
    int days = Days.daysBetween(new DateTime(dateFrom), new DateTime(dateTo)).getDays();
    
    System.out.println("days: " + days);
    return (days * this.weeklyRent / 7);
  }
  
  /**
   * Returns ArrayList of Transaction objects (i.e. payments that the tenant has made)
   *  - Returned ArrayList can have new transactions appended to it/deleted from it using standard ArrayList methods
   * 
   * @return  ArrayList containing Transaction objects relating to this tenant
   */
  public ArrayList<Transaction> getTransactionList() {
    return this.transactionList;
  }
}