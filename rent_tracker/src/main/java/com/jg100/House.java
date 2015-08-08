import java.lang.IllegalArgumentException;
import java.util.ArrayList;

class House {
  private String name, address;
  private int numBedrooms;
  private double rent, agencyFees;
  private ArrayList<Tenant> tenantList;
  
  House(String name, String address, int numBedrooms, double rent, double agencyFees) {
    
    setName(name);
    setAddress(address);
    setNumBedrooms(numBedrooms);
    setRent(rent);
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
  
  public double getRent() {
    return this.rent;
  }
  
  public void setRent(double rent) {
    if(rent < 0.0) {
      throw new IllegalArgumentException("Error: Rent must be greater than or equal to 0.0");
    }
    this.rent = rent;
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
  
  public double getRentPerBedroom() {
    return (this.rent/this.numBedrooms);
  }
  
  public ArrayList<Tenant> getTenantList() {
    return this.tenantList;
  }
}