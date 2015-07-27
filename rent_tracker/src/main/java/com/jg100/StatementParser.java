import java.text.ParseException;

/**
 * Specifies a set of methods that are required to parse bank account statements.
 * Different banks have different formatting in the CSV files, so separate classes have to be written
 *   to handle the different formatting styles.
 */
 public interface StatementParser {
  
  /**
   * Reads a line of the CSV file and splits it up into the various variables and details. 
   * 
   * @param bAcc A bank account object that the parser can populate by passing the parsed data to
   * @param lineNum The current line of the CSV file that is being read.
   * @param lineText A String containing the line of the CSV file that needs to be parsed
   */
   BankAccount parseLine(BankAccount bAcc, int lineNum, String lineText) throws ParseException;
}