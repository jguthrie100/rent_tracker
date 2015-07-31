import java.text.ParseException;
import java.util.regex.*;
import java.util.Date;
import java.text.SimpleDateFormat;

class ASBParser implements StatementParser {
  public ASBParser() {
    
  }
  
  /**
   * Parses a line of text based on specific regex rules and 
   *  the regex rules are different depending on which line of the file is being read.
   * 
   * So line 2 of an ASB bank CSV file is formatted in such a way that a specific regex
   *  is used to get athe bank account number
   * 
   * Line 3 needs a different regex pattern in order to get the date that the records start from 
   * 
   * Line 4 is different yet again, as is Line 5 etc etc 
   * 
   * @param bAcc BankAccount object that refers to the list of transactions that is being parsed 
   * @param lineNum Integer that refers to the current line of the CSV file which is being parsed
   * @param lineText String containing the line of text that is to be parsed 
   */
  public BankAccount parseLine(BankAccount bAcc, int lineNum, String lineText) throws ParseException {
    
    /*
    Get bank account ID 
    */
    if(lineNum == 2) {
      Pattern p = Pattern.compile("^Bank (\\d+); Branch (\\d+); Account ([\\d\\-]+)");
      Matcher m = p.matcher(lineText);
      if(m.find()) {
        bAcc.setAccountID(m.group(1) + "-" + m.group(2) + "-" + m.group(3));
      } else {
        throw new ParseException("Unable to parse line " + lineNum + ": " + lineText, 0);
      }
    }
    
    /*
    Get date that records start from 
    */
    if(lineNum == 3) {
      Pattern p = Pattern.compile("From date (\\d{8})");
      Matcher m = p.matcher(lineText);
      if(m.find()) {
        bAcc.getTransactionCollection().setDateFrom(new SimpleDateFormat("yyyyMMdd").parse(m.group(1)));
      } else {
        throw new ParseException("Unable to parse line " + lineNum + ": " + lineText, 0);
      }
    }
    
    /*
    Get date that records go until 
    */
    if(lineNum == 4) {
      Pattern p = Pattern.compile("To date (\\d{8})");
      Matcher m = p.matcher(lineText);
      if(m.find()) {
        // m.group(1).substring(0, 4) + "/" + m.group(1).substring(4, 6) + "/" + m.group(1).substring(6, 8)
        bAcc.getTransactionCollection().setDateTo(new SimpleDateFormat("yyyyMMdd").parse(m.group(1)));
      } else {
        throw new ParseException("Unable to parse line " + lineNum + ": " + lineText, 0);
      }
    }
    
    /*
    Get the balance of the bank account 
    */
    if(lineNum == 6) {
      Pattern p = Pattern.compile("Ledger Balance : ([\\d\\.]+)");
      Matcher m = p.matcher(lineText);
      if(m.find()) {
        bAcc.setBalance(Double.parseDouble(m.group(1)));
      } else {
        throw new ParseException("Unable to parse line " + lineNum + ": " + lineText, 0);
      }
    }
    
    /*
    This pattern matches the majority of lines i.e. the records of each transation
    Get the various details of each transaction record 
     i.e. date of transaction, id number, type of payment, amount of payment, recipient of payment etc etc 
    */
    if(lineNum >= 9) {
      Pattern p = Pattern.compile("^(.*),(.*),(.*),(.*),\"(.*)\",\"(.*)\",(.*)$");
      Matcher m = p.matcher(lineText);
      if(m.find()) {
         
        // Add transaction record to the transaction collection
        bAcc.getTransactionCollection().getTransactionRecords().add(
          new TransactionRecord(bAcc.getAccountID(), new SimpleDateFormat("yyyy/MM/dd").parse(m.group(1)), Integer.parseInt(m.group(2)), m.group(3), m.group(4), m.group(5), m.group(6), Double.parseDouble(m.group(7)))
        );
      } else {
        throw new ParseException("Unable to parse line " + lineNum + ": " + lineText, 0);
      }
    }
    
    return bAcc;
  }
}