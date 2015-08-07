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
    Get bank account ID using regex
    */
    if(lineNum == 2) {
      Pattern p = Pattern.compile("^Bank (\\d{2}); Branch (\\d{4}); Account (\\d{7}-\\d{2})");
      Matcher m = p.matcher(lineText);
      if(m.find()) {
        bAcc.setAccountID(m.group(1) + "-" + m.group(2) + "-" + m.group(3));
      } else {
        throw new ParseException("Line (" + lineNum + ") doesn't contain a valid bank account string: \"Bank dd; Branch dddd; Account ddddddd-dd\". Line = '" + lineText + "'", 0);
      }
    }
    
    /*
    Get date that records start from using regex
    */
    if(lineNum == 3) {
      Pattern p = Pattern.compile("^From date (\\d{4}[0-1]\\d[0-3]\\d)$");
      Matcher m = p.matcher(lineText);
      if(m.find()) {
        bAcc.getTransactionCollection().setDateFrom(new SimpleDateFormat("yyyyMMdd").parse(m.group(1)));
      } else {
        throw new ParseException("Line (" + lineNum + ") doesn't contain a valid Date string: \"From date yyyyMMdd\". Line = '" + lineText + "'", 0);
      }
    }
    
    /*
    Get date that records end at using regex
    */
    if(lineNum == 4) {
      Pattern p = Pattern.compile("^To date (\\d{4}[0-1]\\d[0-3]\\d)$");
      Matcher m = p.matcher(lineText);
      if(m.find()) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date dateTo = sdf.parse(m.group(1));
        Date dateFrom = bAcc.getTransactionCollection().getDateFrom();
        
        // Ensure that To Date is not before From Date
        if(!dateTo.before(dateFrom)) {
          bAcc.getTransactionCollection().setDateTo(dateTo);
        } else {
          throw new ParseException("Line (" + lineNum + ") Error: 'To Date' is an earlier date than 'From Date'. Date From: " + sdf.format(dateFrom) + "; Date To: " + sdf.format(dateTo) + " (" + sdf.toPattern() + ")", 0);
        }
        
      } else {
        throw new ParseException("Line (" + lineNum + ") doesn't contain a valid Date string: \"To date yyyyMMdd\". Line = '" + lineText + "'", 0);
      }
    }
    
    /*
    Get the balance of the bank account using regex
    */
    if(lineNum == 6) {
      Pattern p = Pattern.compile("Ledger Balance : (-?\\d+\\.\\d{2})\\s");
      Matcher m = p.matcher(lineText);
      if(m.find()) {
        bAcc.setBalance(Double.parseDouble(m.group(1)));
      } else {
        throw new ParseException("Line (" + lineNum + ") doesn't contain a valid account balance. Line = '" + lineText + "'", 0);
      }
    }
    
    /*
    This pattern matches the majority of lines i.e. the records of each transaction
    Get the various details of each transaction record 
     i.e. date of transaction, id number, type of payment, amount of payment, recipient of payment etc etc 
     
     Pattern match gets quite specific - ensures date is formatted relatively correctly with yyyy/MM/dd,
      - ensures id number corresponds to the transaction date,
      - ensures transaction amount is formatted correctly
    */
    if(lineNum >= 9) {
      Pattern p = Pattern.compile("^((\\d{4})/([0-1]\\d)/([0-3]\\d)),(\\2\\3\\4\\d{2,}),([A-Z/ ]+),(.*),\"(.*)\",\"(.*)\",(-?\\d+\\.\\d{2})$");
      Matcher m = p.matcher(lineText);
      if(m.find()) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String dateStr = m.group(1);
        
        // Throw exception if date of transaction lies outside expected date range (as provided earlier in CSV file)
        if(sdf.parse(dateStr).before(bAcc.getTransactionCollection().getDateFrom()) || sdf.parse(dateStr).after(bAcc.getTransactionCollection().getDateTo())) {
          throw new ParseException(
            "Line (" + lineNum + ") Error: Date " + dateStr + " lies outside of expected date range. Expected date from " + sdf.format(bAcc.getTransactionCollection().getDateFrom()) + " to " + sdf.format(bAcc.getTransactionCollection().getDateTo()) + " (" + sdf.toPattern() + ")", 0
          );
        }
        
        // Add transaction record to the transaction collection
        bAcc.getTransactionCollection().getTransactions().add(
          new Transaction(bAcc.getAccountID(), sdf.parse(dateStr), Integer.parseInt(m.group(5)), m.group(6), m.group(7), m.group(8), m.group(9), Double.parseDouble(m.group(10)))
        );
      } else {
        throw new ParseException("Line (" + lineNum + ") doesn't match expected pattern. Line = '" + lineText + "'", 0);
      }
    }
    return bAcc;
  }
}