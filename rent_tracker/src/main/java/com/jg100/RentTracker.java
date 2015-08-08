package com.jg100;

import com.jg100.model.*;
import com.jg100.parsers.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Main class that runs the whole program
 */
public class RentTracker {
  
  /**
   * Main program method 
   * 
   * @param args  Takes 1 argument - String containing internet banking CSV file 
   */
  public static void main(String[] args) throws Exception {
    String csv_file = "";
    
    if(args.length == 0) {
      System.out.println("Missing argument: Please pass String argument pointing to a valid CSV file");
      System.out.println("e.g. java RentTracker \"/home/jg100/.config/asb_23072015.csv\" ");
      System.exit(0);
    } else {
      csv_file = args[0];
    }
    
    ArrayList<House> houseList = new ArrayList<House>();
    BankAccount bAcc = new BankAccount();
    ASBParser asbParser = new ASBParser(); // ASB is name of a bank in New Zealand
    Calculator calc = new Calculator();
    CalendarAccessor calendar = new CalendarAccessor();
    XMLReadWrite xmlRW = new XMLReadWrite();
    
    // Create list of houses
    houseList.add(new House("586B Maunganui Road, Mt Maunganui", "586B Maunganui Road, Mount Maunganui, Tauranga 3116, New Zealand", 3, 400.00, 34.50));
    houseList.add(new House("128A Fernhill Road, Queenstown", "128A Fernhill Road, Fernhill, Queenstown 9300, New Zealand", 3, 450.00, 0.00));
    
    // List of tenants - later versions can store this in an .xml file
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    Date date = new Date();
    houseList.get(0).getTenantList().add(new Tenant("Florence Guthrie", "F S A GUTHRIE, J D S", "0123456789", "flo_guthrie@gmail.com", sdf.parse("2014/11/25"), 350.00, 1));
    houseList.get(0).getTenantList().get(0).setLeaseEnd(sdf.parse("2015/04/17"));
    houseList.get(0).getTenantList().add(new Tenant("Realty Focus Ltd", "REALTY FOCUS LTD", "01234567890123", "realty_focus@gmail.com", sdf.parse("2015/04/15"), 365.50, 2));
    houseList.get(1).getTenantList().add(new Tenant("Florence Guthrie", "F S A GUTHRIE", "0123456789", "flo_guthrie@gmail.com", date, 450.00, 1));
    houseList.get(1).getTenantList().add(new Tenant("Hannah Caithness", "CAITHNESS, H W", "01234567890", "hannah_caithness@gmail.com", date, 450.00, 1));
    houseList.get(1).getTenantList().add(new Tenant("Josh Goodbourn", "Goodbourn J T", "012345678901", "josh_goodbourn@gmail.com", date, 150.00, 1));
    houseList.get(1).getTenantList().add(new Tenant("James Ronaldson", "RONALDSON J", "0123456789012", "james_ronaldson@gmail.com", date, 150.00, 1));
    houseList.get(1).getTenantList().add(new Tenant("Steven Anglin", "S S ANGLIN", "01234567890123", "steven_anglin@gmail.com", date, 150.00, 1));
    
    /** Read and parse CSV file containing bank transactions */
    System.out.println("Opening CSV file: " + csv_file);
    try (BufferedReader br = new BufferedReader(new FileReader(csv_file)))
		{
			String sCurrentLine;
			int counter = 1;
 
			while ((sCurrentLine = br.readLine()) != null) {
  			try {
  			  // Pass line of CSV text to the parser
          asbParser.parseLine(bAcc, counter, sCurrentLine);
        } catch (ParseException e) {
          // Exit program if CSV file doesn't parse properly
          System.out.println(e.toString());
          System.out.println(" ---> Please correct the input CSV file so that it matches the expected pattern");
          System.out.println(" ---> Exiting...");
          System.exit(0);
        }
        counter++;
			}
			System.out.println("Found file: " + csv_file);
 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*  The idea here is to loop through all the transaction records and determine whether or not each one 
		 *   matches up to a tenant. 
		 *  
		 *  In order to do this, we need to loop through each tenant and see if the transaction details match
		 *   i.e. if their name matches the transaction payee or memo, and whether the payment amount is equal to their rent. 
		 * 
		 *  For any cases where the name matches the transaction, but the amount doesn't match their rent, we prompt the user to confirm whether
		 *   its a rental payment or not 
		 */
		try { 
      BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
      boolean isRentPayment = false;
      
  		for(Transaction tr : bAcc.getTransactionCollection().getTransactions()) {
  		  // loop through each transaction record
  		  
  		  isRentPayment = false;  // reset rentPayment flag just incase its still true from the previous loop
        
        for(House h : houseList) {  // loop through each house & then each tenant 
          for(Tenant t : h.getTenantList()) {
            if((tr.getPayee().contains(t.getPaymentHandle()) || tr.getMemo().contains(t.getPaymentHandle())) && !isRentPayment) {
              // if tenant's 'payment handle' matches transaction payee or memo fields, then we know the payment was made by this tenant
              
              if(rentMultiple(tr.getAmount(), t.getWeeklyRent(), 4)) {
                // if transaction amount matches expected rent (or a multiple of), we can automatically flag the transaction as being a rental payment
                isRentPayment = true;
                
              } else {
                // otherwise we prompt the user to confirm whether its a rental payment or not
                System.out.println("----------------------------------------");
                System.out.println(tr.getDate().toString() + ": " + tr.getAmount() + " - " + t.getPaymentHandle());
                System.out.print("Is this payment a rental payment for " + h.getName() + "? (y/n): ");
                
                if(input.readLine().toLowerCase().trim().equals("y")) {
                  // if they type 'y', then flag the payment as a rental payment
                  System.out.println(" ---> Payment marked as rent");
                  isRentPayment = true;
                }
              }
              
              // Add to tenant's list of rent payments if payment is a rental payment
              if(isRentPayment) {
                t.getTransactionList().add(tr);
              }
            }
          }
        }
  		}
    } catch (Exception e) {
      System.out.println("Error opening input/output stream.");
      e.printStackTrace();
    }
    
    /** This loop adds every payment of each tenant to the calendar */
    for(House h : houseList) {  // loop through each house & then each tenant 
      for(Tenant t : h.getTenantList()) {
        for (Transaction tr : t.getTransactionList()) {
          System.out.println("----------------------------");
          System.out.println(tr.getDate().toString() + " | " + h.getName() + " : " + t.getName() + " - " + tr.getAmount());
          try {
         //   calendar.addRentPayment(h.getName(), t.getName(), tr);
          } catch (Exception e) {
            System.out.println("Failed to update Calendar. The rent payment may already be saved from a previous session");
          }
          System.out.println("");
        }
      }
    }
		
		Date d1 = sdf.parse("2015/04/15");
		Date d2 = sdf.parse("2015/04/30");
		
		System.out.println("Total rent paid by Flo: " + houseList.get(0).getTenantList().get(1).getTotalRentPaid(d1, d2));
		System.out.println("Total rent expected by Flo: " + houseList.get(0).getTenantList().get(1).getTotalRentExpected(d1, d2));
		
		xmlRW.writeTenantsXML(houseList);
	}
	
	/** Checks to see if the payment amount is a multiple of the rent.
	 *   i.e. if the payment is 750 and the rent is 250, then the payment is 3 lots of rent
	 * 
	 * @param payment     rent payment we are checking
	 * @param weeklyRent  Tenant's weekly rent amount
	 * @param maxMultiple Max amount that the payment can be compared to weekly rent. i.e. if maxMultiple is 3,
	 *                      then payment 3 times bigger than weekly rent amount will match, but payment 4 times bigger won't
	 */
	private static boolean rentMultiple(double payment, double weeklyRent, int maxMultiple) {
	  for(int i = 1; i <= maxMultiple; i++) {
//	    System.out.println("Amount: " + amount + "; Rent: " + rent + " = " + (amount / rent));
	    if(payment / weeklyRent == i) {
	      return true;
	    }
	  }
	  return false;
	}
}