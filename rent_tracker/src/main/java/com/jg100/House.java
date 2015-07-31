import java.util.ArrayList;

class House {
  private String name, address;
  private int numBedrooms;
  private double rent, agencyFees;
  private ArrayList<Tenant> tenantList;
  
  House(String name, String address, int numBedrooms, double rent, double agencyFees) {
    this.name = name;
    this.address = address;
    this.numBedrooms = numBedrooms;
    this.rent = rent;
    this.agencyFees = agencyFees;
    
    tenantList = new ArrayList<Tenant>();
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getAddress() {
    return this.address;
  }
  
  public int getNumBedrooms() {
    return this.numBedrooms;
  }
  
  public void setNumBedrooms(int numBedrooms) {
    this.numBedrooms = numBedrooms;
  }
  
  public double getRent() {
    return this.rent;
  }
  
  public void setRent(double rent) {
    this.rent = rent;
  }
  
  public double getAgencyFees() {
    return this.agencyFees;
  }
  
  public void setAgencyFees(double agencyFees) {
    this.agencyFees = agencyFees;
  }
  
  public double getRentPerBedroom() {
    return (this.rent/this.numBedrooms);
  }
  
  public ArrayList<Tenant> getTenantList() {
    return this.tenantList;
  }
}