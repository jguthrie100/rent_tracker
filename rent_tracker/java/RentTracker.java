import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RentTracker {
  public static void main(String[] args) {
    String csv_file = "../csv_files/asb_23072015.csv";
    
    if(args.length > 0) {
      csv_file = args[0];
    }
    
    try (BufferedReader br = new BufferedReader(new FileReader(csv_file)))
		{
 
			String sCurrentLine;
 
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} 
 
	}
}