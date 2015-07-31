import java.util.ArrayList;

class House {
  private String name, address;
  private int numRooms;
  private double rent, agencyFees;
  private ArrayList<Tenant> tenantList;
  
  House(String name, String address, int numRooms, double rent, double agencyFees) {
    this.name = name;
    this.address = address;
    this.numRooms = numRooms;
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
  
  public int getNumRooms() {
    return this.numRooms;
  }
  
  public void setNumRooms(int numRooms) {
    this.numRooms = numRooms;
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
  
  public double getRentPerRoom() {
    return (this.rent/this.numRooms);
  }
  
  public ArrayList<Tenant> getTenantList() {
    return this.tenantList;
  }
}