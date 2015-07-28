import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;

public class RentTracker {
  public static void main(String[] args) throws Exception {
    String csv_file = "";
    BankAccount bAcc = new BankAccount();
    ASBParser asbParser = new ASBParser();
    Calculator calc = new Calculator();
    CalendarAccessor calendar = new CalendarAccessor();
    
    if(args.length == 0) {
      System.out.println("Missing argument: Please pass String argument pointing to a CSV file");
      System.out.println("e.g. java RentTracker \"/home/jg100/.config/asb_23072015.csv\" ");
      System.exit(0);
    } else {
      csv_file = args[0];
    }
    
    System.out.println("Opening file: " + csv_file);
    try (BufferedReader br = new BufferedReader(new FileReader(csv_file)))
		{
			String sCurrentLine;
			int counter = 1;
 
			while ((sCurrentLine = br.readLine()) != null) {
  			try {
          asbParser.parseLine(bAcc, counter, sCurrentLine);
        } catch(ParseException e) {
          e.printStackTrace();
        }
        counter++;
			}
			System.out.println("Found file: " + csv_file);
 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(TransactionRecord tr : bAcc.getTransactionCollection().getTransactionRecords()) {
		  calendar.addTransaction(tr);
		}
		
		Date dateFrom = new SimpleDateFormat("yyyy/MM/dd").parse("2014/11/18");
		Date dateTo = new SimpleDateFormat("yyyy/MM/dd").parse("2015/07/23");
		DecimalFormat df = new DecimalFormat("0.00");
		
		System.out.println("Income: $" + df.format(calc.getIncome(bAcc, dateFrom, dateTo)));
		System.out.println("Outgoings: $" + df.format(calc.getOutgoings(bAcc, dateFrom, dateTo)));
		System.out.println("Total: $" + df.format((calc.getIncome(bAcc, dateFrom, dateTo) + calc.getOutgoings(bAcc, dateFrom, dateTo))));
	}
}