package com.jg100;

import com.jg100.model.*;
import com.jg100.parsers.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Main class that runs the whole program
 */
public class RentTracker {
  
  /**
   * Main program method 
   * 
   * @param args  Takes 1 argument - A string containing internet banking CSV file 
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
    
    // Variables used throughout main()
    ArrayList<House> houseList = new ArrayList<House>();
    BankAccount bAcc = new BankAccount();
    ASBParser asbParser = new ASBParser(); // ASB is name of a bank in New Zealand
    CalendarAccessor calendar = new CalendarAccessor();
    XMLReadWrite xmlRW = new XMLReadWrite("/home/jg100/.config/tenants.xml");
    
    //populate houseList using data from XML file
	  houseList = xmlRW.readTenantsXML();
	  
	  // option to populate using hardcoded methods...
	  //houseList = populateHouseList();
    
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
		
		houseList = identifyRentPayments(houseList, bAcc.getTransactionCollection(), calendar);
    
    if(houseList.size() != 0) {
		  xmlRW.writeTenantsXML(houseList);
    }
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
	
	private static ArrayList<House> populateHouseList() throws ParseException {
	  ArrayList<House> houseList = new ArrayList<House>();
	  SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    Date date = new Date();
    
    // Create list of houses
    houseList.add(new House("586B Maunganui Road, Mt Maunganui", "586B Maunganui Road, Mount Maunganui, Tauranga 3116, New Zealand", 3, 400.00, 34.50));
    houseList.add(new House("128A Fernhill Road, Queenstown", "128A Fernhill Road, Fernhill, Queenstown 9300, New Zealand", 3, 450.00, 0.00));
    
    // List of tenants - later versions can store this in an .xml file
    houseList.get(0).getTenantList().add(new Tenant("Florence Guthrie", "F S A GUTHRIE, J D S", "0123456789", "flo_guthrie@gmail.com", sdf.parse("2015/04/01"), 350.00, 1));
    houseList.get(0).getTenantList().get(0).setLeaseEnd(sdf.parse("2015/05/19"));
    houseList.get(0).getTenantList().add(new Tenant("Realty Focus Ltd", "REALTY FOCUS LTD", "01234567890123", "realty_focus@gmail.com", sdf.parse("2015/04/15"), 365.50, 2));
    houseList.get(1).getTenantList().add(new Tenant("Florence Guthrie", "F S A GUTHRIE", "0123456789", "flo_guthrie@gmail.com", date, 450.00, 1));
    houseList.get(1).getTenantList().add(new Tenant("Hannah Caithness", "CAITHNESS, H W", "01234567890", "hannah_caithness@gmail.com", date, 450.00, 1));
    houseList.get(1).getTenantList().add(new Tenant("Josh Goodbourn", "Goodbourn J T", "012345678901", "josh_goodbourn@gmail.com", date, 150.00, 1));
    houseList.get(1).getTenantList().add(new Tenant("James Ronaldson", "RONALDSON J", "0123456789012", "james_ronaldson@gmail.com", date, 150.00, 1));
    houseList.get(1).getTenantList().add(new Tenant("Steven Anglin", "S S ANGLIN", "01234567890123", "steven_anglin@gmail.com", date, 150.00, 1));
    
    return houseList;
	}
	
	private static ArrayList<House> identifyRentPayments(ArrayList<House> houseList, TransactionCollection tc) {
	  return identifyRentPayments(houseList, tc, null);
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
	private static ArrayList<House> identifyRentPayments(ArrayList<House> houseList, TransactionCollection tc, CalendarAccessor calendar) {
	  SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	  DecimalFormat df = new DecimalFormat("0.00");
	  
		try { 
      BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
      boolean isRentPayment = false;
      
  		transactionLoop: for(Transaction tr : tc.getTransactions()) {
  		  // loop through each transaction record
  		  
  		  isRentPayment = false;  // reset rentPayment flag just incase its still true from the previous loop
        
        for(House h : houseList) {  // loop through each house & then each tenant 
          for(Tenant t : h.getTenantList()) {
            /* Loop through the transactions that have already been processed when reading the XML file of tenants
               - If a transaction has already been marked as being a rental payment, then we can just set isRentPayment to true
                 and skip checking again
            */
            for(int i = 0; i < t.getTransactionList().size(); i++) {
              Transaction tr2 = t.getTransactionList().get(i);
              
              // If transaction has already been saved when parsing tenants.xml
              if(tr2.getFullId().equals(tr.getFullId())) {
                isRentPayment = true;
                System.out.println("Already marked as rent: id = " + tr2.getFullId() + " (" + t.getName() + ")");
                t.getTransactionList().remove(i);
                break;
              } else if(tr2.getBankAccountId().equals(tr.getBankAccountId()) && tr2.getId() > tr.getId()) {
                break; // No point looping through rest of transactions
              }
            }
            if(!isRentPayment && (tr.getPayee().contains(t.getPaymentHandle()) || tr.getMemo().contains(t.getPaymentHandle()))) {
              // if tenant's 'payment handle' matches transaction payee or memo fields, then we know the payment was made by this tenant
              
              if(rentMultiple(tr.getAmount(), t.getWeeklyRent(), 4)) {
                // if transaction amount matches expected rent (or a multiple of), we can automatically flag the transaction as being a rental payment
                isRentPayment = true;
                
              } else {
                // otherwise we prompt the user to confirm whether its a rental payment or not
                System.out.println("----------------------------------------");
                System.out.println(sdf.format(tr.getDate()) + ": $" + df.format(tr.getAmount()) + " - " + t.getPaymentHandle());
                System.out.print("Is this payment a rental payment for " + h.getName() + "? (y/n): ");
 
                if(input.readLine().toLowerCase().trim().equals("y")) {
                  // if they type 'y', then flag the payment as a rental payment
                  System.out.println(" ---> Payment marked as rent");
                  isRentPayment = true;
                }
              }
            }
            // Add to tenant's list of rent payments if payment is a rental payment
            if(isRentPayment) {
              System.out.println("Saved rent payment: " + sdf.format(tr.getDate()) + ": $" + df.format(tr.getAmount()) + " - " + t.getPaymentHandle());
              t.getTransactionList().add(tr);
              
              if(calendar != null) {
                try {
                  calendar.addRentPayment(h.getName(), t.getName(), tr);
                } catch (Exception e) {
                  System.out.println("Error: Failed to update Calendar");
                }
              }
              continue transactionLoop;
            }
          }
        }
  		}
    } catch (IOException e) {
      System.out.println("Error opening input/output stream.");
      e.printStackTrace();
    }
    
    /** This loop adds every payment of each tenant to the calendar */
/*    for(House h : houseList) {  // loop through each house & then each tenant 
      for(Tenant t : h.getTenantList()) {
        for (Transaction tr : t.getTransactionList()) {
          System.out.println("----------------------------");
          System.out.println(sdf.format(tr.getDate()) + " | " + h.getName() + " : " + t.getName() + " - " + df.format(tr.getAmount()));

          try {
            calendar.addRentPayment(h.getName(), t.getName(), tr);
          } catch (Exception e) {
            System.out.println("Failed to update Calendar. The rent payment may already be saved from a previous session");
          }
          System.out.println("");
        }
        System.out.println(t.getName() + " - Days rented: " + (int) Math.round(t.getTotalRentExpected() * 7 / t.getWeeklyRent()));
        System.out.println("Total rent expected: " + df.format(t.getTotalRentExpected()));
        System.out.println("Total rent paid: " + df.format(t.getTotalRentPaid()));
        System.out.println("----");
      }
    }*/
    
    return houseList;
	}
}