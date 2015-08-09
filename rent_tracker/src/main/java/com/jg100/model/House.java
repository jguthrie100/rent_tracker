package com.jg100.model;

import java.util.ArrayList;
import java.text.DecimalFormat;

/**
 * House class to model houses that the landlord rents out 
 * 
 * Each House object has information relating to general properties of the house (address, number of bedrooms, rental income etc)
 *  as well as a list of tenants that lived/have lived at the house 
 */
public class House {
  private String name, address;
  private int numBedrooms;
  private double weeklyRent, agencyFees;
  private ArrayList<Tenant> tenantList;
  
  /**
   * House constructor to create new House object 
   *  - Null or empty parameters are not allowed
   * 
   * Each House object has information relating to itself as well as a list of Tenant objects (relating to tenant's that live/lived there)
   * 
   * @param name          Name of the house. Can be first line of address or any unique(ish) identifier 
   * @param address       Address of the house 
   * @param numBedrooms   Number of bedrooms in the house (must be 1 or more)
   * @param weeklyRent    Weekly cost of rent for the house
   * @param agencyFees    Weekly cost of agency fees for the house
   */
  public House(String name, String address, int numBedrooms, double weeklyRent, double agencyFees) {
    /* Use setter methods to improve exception handling */
    setName(name);
    setAddress(address);
    setNumBedrooms(numBedrooms);
    setWeeklyRent(weeklyRent);
    setAgencyFees(agencyFees);
    
    tenantList = new ArrayList<Tenant>();
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
  
  public String getAddress() {
    return this.address;
  }
  
  public void setAddress(String address) {
    if(address == null || address.isEmpty()) {
      throw new IllegalArgumentException("Error: Address cannot be null or empty");
    }
    this.address = address;
  }
  
  public int getNumBedrooms() {
    return this.numBedrooms;
  }
  
  public void setNumBedrooms(int numBedrooms) {
    if(numBedrooms < 1) {
      throw new IllegalArgumentException("Error: Number of bedrooms must be greater than or equal to 1");
    }
    this.numBedrooms = numBedrooms;
  }
  
  public double getWeeklyRent() {
    return this.weeklyRent;
  }
  
  public void setWeeklyRent(double weeklyRent) {
    if(weeklyRent < 0.0) {
      throw new IllegalArgumentException("Error: Rent must be greater than or equal to 0.0");
    }
    this.weeklyRent = weeklyRent;
  }
  
  public double getAgencyFees() {
    return this.agencyFees;
  }
  
  public void setAgencyFees(double agencyFees) {
    if(agencyFees < 0.0) {
      throw new IllegalArgumentException("Error: Agency fees must be greater than or equal to 0.0");
    }
    this.agencyFees = agencyFees;
  }
  
  public double getWeeklyRentPerBedroom() {
    return (this.weeklyRent/this.numBedrooms);
  }
  
  public ArrayList<Tenant> getTenantList() {
    return this.tenantList;
  }
  
  public String toString() {
    DecimalFormat df = new DecimalFormat("0.00");
    return ("name: \"" + this.name + "\"; address: \"" + this.address + "\"; numBedrooms: " + this.numBedrooms
            + "; weeklyRent: " + df.format(this.weeklyRent) + "; agencyFees: " + df.format(this.agencyFees));
  }
}