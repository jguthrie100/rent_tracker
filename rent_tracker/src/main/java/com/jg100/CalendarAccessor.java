import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

class CalendarAccessor {
  
  private static final String APP_NAME = "Rental Income Tracker";
  
  /** Global instance of the JSON factory. */
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

  /** Global instance of the HTTP transport. */
  private static HttpTransport HTTP_TRANSPORT;
    
  static {
    try {
      HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    } catch (Throwable t) {
      t.printStackTrace();
      System.exit(1);
    }
  }
  
  /* API service is kept private. Intention is to write Calendar accessing methods
   *  that use the object, but that it can never been accessed from outside the class,
   *  not even using a getService() method.
   */
  private Calendar service;
        
  CalendarAccessor() throws Exception {
    
    /** OAuth 2.0 authentications variables */
    final String SERVICE_EMAIL = "332745442057-5b9b5rt8oqa2g5vpugp0u5qhlhkkl1ta@developer.gserviceaccount.com";
    final File P12_CERTIFICATE = new File("/home/jg100/.config/rent_tracker_login.p12");
    
    /* Set up OAuth 2.0 authentication */
    GoogleCredential credentials = new GoogleCredential.Builder().setTransport(HTTP_TRANSPORT)
    .setJsonFactory(JSON_FACTORY)
    .setServiceAccountId(SERVICE_EMAIL)
    .setServiceAccountScopes(Arrays.asList(CalendarScopes.CALENDAR))
    .setServiceAccountPrivateKeyFromP12File(P12_CERTIFICATE)
    .build();
    
    // Build Calendar API service - all API calls made through this 'service' object
    this.service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials).setApplicationName(APP_NAME).build();
    
  }
  
  /** Parses relevant data from a TransactionRecord and adds it to the calendar */
  public String addTransaction(TransactionRecord tRecord) {
    
    String idPrefix = APP_NAME.toLowerCase().replace(" ", "").replaceAll("[w-z]", "x");
    String id = idPrefix + tRecord.getBankAccountID().replace("-", "") + tRecord.getID();
    
    /* Needs more sophisticated way of deciding what should go in the title 
     *  Sometimes the name of the person is in the memo and not the payee section for eg,
     *  so things like that need accounting for. 
     */
    
    // If transaction amount is less than 0, put - sign infront of $
    String title = "";
    if(tRecord.getAmount() < 0) {
      title = "-";
    }
    
    // Format event title. i.e. "$350 - F S A GUTHRIE (rent)"
    title += "$" + Math.abs(tRecord.getAmount()) + " - " + tRecord.getPayee();
    if(tRecord.isRent()) {
      title += " (rent)";
    }
    
    title = title.replaceAll("D/C FROM ", "");
    
    String description = title + System.lineSeparator();
    description += tRecord.getPayee() + System.lineSeparator();
    description += tRecord.getMemo() + System.lineSeparator();
    description += System.lineSeparator() + "id: " + id;
    
    System.out.println("------------------------------------");
    System.out.println("   " + title + "    ");
    System.out.println(description);
    System.out.println("------------------------------------" + System.lineSeparator());
    
    Event event = new Event()
    .setSummary(title)
    .setDescription(description);
    
    /** return event id that the event is saved under in the calendar */
    return id;
  }
}