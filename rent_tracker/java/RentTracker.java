import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

public class RentTracker {
  public static void main(String[] args) {
    String csv_file = "";
    BankAccount bAcc = new BankAccount();
    ASBParser asbParser = new ASBParser();
    
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
	}
}