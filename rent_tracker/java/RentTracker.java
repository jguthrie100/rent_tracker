import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class RentTracker {
  public static void main(String[] args) throws Exception {
    String csv_file = "";
    BankAccount bAcc = new BankAccount();
    ASBParser asbParser = new ASBParser();
    Calculator calc = new Calculator();
    
    if(args.length == 0) {
      System.out.println("Missing argument: Please pass String argument pointing to a CSV file");
      System.out.println("e.g. java RentTracker \"../csv_files/asb_23072015.csv\" ");
      System.exit(0);
    } else {
      csv_file = args[0];
    }
    
    System.out.println("Opening file \"" + csv_file + "\"...");
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
			System.out.println("Found: " + csv_file);
 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Date dateFrom = new SimpleDateFormat("yyyy/MM/dd").parse("2014/11/22");
		Date dateTo = new SimpleDateFormat("yyyy/MM/dd").parse("2014/11/25");
		System.out.println("Income: " + calc.getIncome(bAcc, dateFrom, dateTo));
		System.out.println("Outgoings: " + calc.getOutgoings(bAcc, dateFrom, dateTo));
		System.out.println("Total: " + Math.round((calc.getIncome(bAcc, dateFrom, dateTo) + calc.getOutgoings(bAcc, dateFrom, dateTo))*100)/100.00);
	}
}