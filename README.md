# rent_tracker
Program to track rental payments made by tenants for a rental home.

This program is a little tool that helps keep track of tenants paying rent towards a rental home(s).

It needs a CSV file from the online bank account where the rent is paid into as well as a list of houses and tenants of 
each house.

The program then basically parses the CSV file, identifying the transactions that are rental payments and matches the 
payments to the tenants, kepping track of how much each tenant has paid and how much they are due to pay.

As a bit of side functionality, the program adds the rent payments to a Google Calendar 
 (details of which are set up in one of the classes - plans for the future include putting all the config info in an 
 xml file) so that they can be easily looked through, month by month.
 
_____________________________________________________________________________________________________________________
Future functionality for v1 includes:
 - Working out exactly how much tenant owes
 - Keeping track of the date when rent price increases occur and automatically applying the increases to tenants 
   from that date
 - Reading all the tenant info from an XML file, rather than manually calling the methods in the RentTracker.java class
 - Improved Calendar functionality. i.e. updating rent events, setting reminders, sending chase up emails etc
 
v2: Look at upgrading to a more comprehensive accounting package.
 - Keep track of all mortgage / council tax etc payments and work out income yields etc
 - Create a web interface that displays bar charts etc of payments, ability to add tenants etc
 
- Also want to consider porting to Ruby on Rails (will be easier to create a web front end using it)
 
