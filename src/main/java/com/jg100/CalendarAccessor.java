package com.jg100;

import com.jg100.model.*;

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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * Class that handles sending/receiving data with a Google Calendar. 
 * Its essentially used to put various rent payments / misc payments into a Google Calendar so that they can be browsed
 *  through in a nice month-by-month way.
 */
public class CalendarAccessor {
  
  private static final String APP_NAME = "Rental Income Tracker";
  
  /* Unique Calendar ID that refers to a specific Google Calendar that we want to write to/read from */
  private static final String CALENDAR_ID = "8cug1mvjco2p0s57f8etcfj130@group.calendar.google.com";
  
  /* Global instance of the JSON factory. */
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

  /* Global instance of the HTTP transport. */
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
  
  /**
   * Constructor authenticates with Google (using Auth2.0) and creates the core Calendar service object that we use in later methods
   */
  public CalendarAccessor() throws Exception {
    
    /* OAuth 2.0 authentications variables - refer to Calendar API authorisation docs online */
    final String SERVICE_EMAIL = "332745442057-5b9b5rt8oqa2g5vpugp0u5qhlhkkl1ta@developer.gserviceaccount.com";
    final File P12_CERTIFICATE = new File("/home/jg100/.config/rent_tracker_login.p12");
    
    /* Set up OAuth 2.0 authentication that uses Service Email */
    GoogleCredential credentials = new GoogleCredential.Builder().setTransport(HTTP_TRANSPORT)
    .setJsonFactory(JSON_FACTORY)
    .setServiceAccountId(SERVICE_EMAIL)
    .setServiceAccountScopes(Arrays.asList(CalendarScopes.CALENDAR))
    .setServiceAccountPrivateKeyFromP12File(P12_CERTIFICATE)
    .build();
    
    // Build Calendar API service - all API calls are made through this 'service' object
    this.service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials).setApplicationName(APP_NAME).build();
    
  }
  
  /**
   * Takes relevant data about a rental payment and adds it to the calendar
   * 
   * @param house   String referring to which house the rental payment was made for 
   * @param tenant  String referring to the name of the tenant that the payment was made by 
   * @param tr      Transaction object containing information relating to the specific payment
   * @return        The unique id that the event is saved under in the Google Calendar
   */
  public String addRentPayment(String house, String tenant, Transaction tr) throws IOException {
    
    String id = tr.getFullId();
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    DecimalFormat df = new DecimalFormat("0.00");
    String title = "$" + df.format(tr.getAmount()) + " - " + tenant;
    
    /* Create a description that gets saved in the Calendar event */
    String description = tr.getPayee() + System.lineSeparator();
    description += tr.getMemo() + System.lineSeparator();
    description += System.lineSeparator() + "id: " + id;
    
    /* Save rent payments made to different Houses using different colours */
    String eventColor = "";
    switch(house) {
      case "586B Maunganui Road, Mt Maunganui": eventColor = "5";
                                                break;
      case "128A Fernhill Road, Queenstown":    eventColor = "9";
                                                break;
      default:  eventColor = "10";
                break;
    }
    
    /* Test output - print to console
    System.out.println("------------------------------------");
    System.out.println("   " + title + "    ");
    System.out.println(description);
    System.out.println("------------------------------------" + System.lineSeparator());
    */
    
    /* Create new Calendar event */
    Event event = new Event()
    .setSummary(title)
    .setDescription(description)
    .setLocation(house)
    .setId(id)
    .setColorId(eventColor);
    
    /* Set the event start date/time */
    DateTime startDateTime = new DateTime(tr.getDate());
    EventDateTime start = new EventDateTime()
    .setDateTime(startDateTime);
    event.setStart(start);

    /* Set the event end date/time */
    DateTime endDateTime = new DateTime(tr.getDate());
    EventDateTime end = new EventDateTime()
    .setDateTime(endDateTime);
    event.setEnd(end);
    
    // Check if the Event we want to add already exists in the Calendar
    try {
      Event e = getEvent(id);

      /* Compare all the event details and update if anything is different.
         - The crazy long date comparisons are because we need to break the awkward DateTime object right down into a basic string 
         in order to compare it properly. Also, when the Calendar switches to daylight saving time it messes up with the comparison 
         unless we only compare the first 10 chars of the dates.. i.e. just yyyy-MM-dd, rather than all the hours and minutes too 
      */
      if(e.getSummary().equals(title) && e.getDescription().equals(description)
         && e.getLocation().equals(house) && e.getColorId().equals(eventColor)
         && e.getStart().getDateTime().toString().substring(0, 10).equals(start.getDateTime().toString().substring(0, 10))
         && e.getEnd().getDateTime().toString().substring(0, 10).equals(end.getDateTime().toString().substring(0, 10))) {
        return id;
      } else {
        // Event exists, but details are different. Update event with new details
        service.events().update(CALENDAR_ID, event.getId(), event).execute();
        return id;
      }
    } catch (Exception e) {
      // Error getting Event - probably doesn't exist yet
      // Continue and create new event
    }
    
    /* Insert the event to the Calendar using service object */
    event = service.events().insert(CALENDAR_ID, event).execute();
    
    return id;
  }
  
  /**
   * Grabs an Event object (with given id) from the Calendar 
   */
  public Event getEvent(String id) throws IOException {
    Event event = service.events().get(CALENDAR_ID, id).execute();
    
    return event;
  }
}