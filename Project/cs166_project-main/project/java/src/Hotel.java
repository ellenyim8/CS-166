/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class Hotel {

   // reference to physical database connection.
   private Connection _connection = null;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

   //String userID;
   //String userType;
   /**
    * Creates a new instance of Hotel 
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public Hotel(String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end Hotel

   // Method to calculate euclidean distance between two latitude, longitude pairs. 
   public double calculateDistance (double lat1, double long1, double lat2, double long2){
      double t1 = (lat1 - lat2) * (lat1 - lat2);
      double t2 = (long1 - long2) * (long1 - long2);
      return Math.sqrt(t1 + t2); 
   }
   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQueryAndPrintResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      System.out.println("*************************************************************");
      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
		 if(outputHeader){
			for(int i = 1; i <= numCol; i++){
			System.out.print(rsmd.getColumnName(i) + "\t\t");
			}
			System.out.println();
			outputHeader = false;
		 }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t\t");
         System.out.println ();
         ++rowCount;
      }//end while
      System.out.println("*************************************************************");
      stmt.close ();
      return rowCount;
   }//end executeQuery

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the results as
    * a list of records. Each record in turn is a list of attribute values
    *
    * @param query the input query string
    * @return the query result as a list of records
    * @throws java.sql.SQLException when failed to execute the query
    */
   public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and saves the data returned by the query.
      boolean outputHeader = false;
      List<List<String>> result  = new ArrayList<List<String>>();
      while (rs.next()){
        List<String> record = new ArrayList<String>();
		for (int i=1; i<=numCol; ++i)
			record.add(rs.getString (i));
        result.add(record);
      }//end while
      stmt.close ();
      return result;
   }//end executeQueryAndReturnResult

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the number of results
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       int rowCount = 0;

       // iterates through the result set and count nuber of results.
       while (rs.next()){
          rowCount++;
       }//end while
       stmt.close ();
       return rowCount;
   }

   /**
    * Method to fetch the last value from sequence. This
    * method issues the query to the DBMS and returns the current
    * value of sequence used for autogenerated keys
    *
    * @param sequence name of the DB sequence
    * @return current value of a sequence
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int getCurrSeqVal(String sequence) throws SQLException {
      Statement stmt = this._connection.createStatement ();

      ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
      if (rs.next())
         return rs.getInt(1);
      return -1;
   }

   public int getNewUserID(String sql) throws SQLException {
      Statement stmt = this._connection.createStatement ();
      ResultSet rs = stmt.executeQuery (sql);
      if (rs.next())
         return rs.getInt(1);
      return -1;
   }
   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            Hotel.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if

      Greeting();
      Hotel esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the Hotel object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new Hotel (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
            System.out.println("MAIN MENU");
            System.out.println("---------");
            System.out.println("1. Create user");
            System.out.println("2. Log in");
            System.out.println("9. < EXIT");
            String authorisedUser = null;
            switch (readChoice()){
               case 1: CreateUser(esql); break;
               case 2: authorisedUser = LogIn(esql); break;
               case 9: keepon = false; break;
               default : System.out.println("Unrecognized choice!"); break;
            }//end switch
            if (authorisedUser != null) {
              boolean usermenu = true;
              while(usermenu) {
                System.out.println("MAIN MENU");
                System.out.println("---------");
                System.out.println("1. View Hotels within 30 units");
                System.out.println("2. View Rooms");
                System.out.println("3. Book a Room");
                System.out.println("4. View recent booking history");

                //the following functionalities basically used by managers
                System.out.println("5. Update Room Information");
                System.out.println("6. View 5 recent Room Updates Info");
                System.out.println("7. View booking history of the hotel");
                System.out.println("8. View 5 regular Customers");
                System.out.println("9. Place room repair Request to a company");
                System.out.println("10. View room repair Requests history");
                System.out.println(".........................");
                System.out.println("20. Log out");

                switch (readChoice()){
                   case 1: viewHotels(esql); break;
                   case 2: viewRooms(esql); break;
                   case 3: bookRooms(esql); break;
                   case 4: viewRecentBookingsfromCustomer(esql); break;
                   case 5: updateRoomInfo(esql); break;
                   case 6: viewRecentUpdates(esql); break;
                   case 7: viewBookingHistoryofHotel(esql); break;
                   case 8: viewRegularCustomers(esql); break;
                   case 9: placeRoomRepairRequests(esql); break;
                   case 10: viewRoomRepairHistory(esql); break;
                   case 20: usermenu = false; break;
                   default : System.out.println("Unrecognized choice!"); break;
                }
              }
            }
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main

   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              User Interface      	               \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

   /*
    * Creates a new user
    **/
   public static void CreateUser(Hotel esql){
      try{
         System.out.print("\tEnter name: ");
         String name = in.readLine();
         System.out.print("\tEnter password: ");
         String password = in.readLine(); 
         String type="Customer";
			String query = String.format("INSERT INTO USERS (name, password, userType) VALUES ('%s','%s', '%s')", name, password, type);
         esql.executeUpdate(query);
         System.out.println ("User successfully created with userID = " + esql.getNewUserID("SELECT last_value FROM users_userID_seq"));
         
      }catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }//end CreateUser


   /*
    * Check log in credentials for an existing user
    * @return User login or null is the user does not exist
    **/
   public static String LogIn(Hotel esql){
      try{
         System.out.print("\tEnter userID: ");
         String userID = in.readLine();
         System.out.print("\tEnter password: ");
         String password = in.readLine();

         String query = String.format("SELECT * FROM USERS WHERE userID = '%s' AND password = '%s'", userID, password);
         int userNum = esql.executeQuery(query);
         if (userNum > 0)
            return userID;
         return null;
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end

// Rest of the functions definition go in here

   public static void viewHotels(Hotel esql) {  // Ellen
      try {
         System.out.print("\tEnter latitude: ");
         String latitude = in.readLine();
         System.out.print("\tEnter longitude: ");
         String longitude = in.readLine();
         String query = String.format("SELECT hotelID, hotelName FROM Hotel WHERE calculate_distance('%s', '%s', Hotel.latitude, Hotel.longitude) < 30; ", latitude, longitude); 
         esql.executeQueryAndPrintResult(query);

      } catch(Exception e) {
         System.err.println(e.getMessage());
      }
   }

   /* == viewRooms() function= == // Paulian
   (also known as "browse rooms")
    * Allows user to input a hotelID AND a date.
    * Should return the list of rooms w/ the price and avaliablity of the rooms on the given date
    **/
   public static void viewRooms(Hotel esql) {
      try {
         System.out.println(
         "\n\n*******************************************************\n" +
         "              Viewing Avaliable Rooms      	               \n" +
         "*******************************************************\n");

         // gets hotelID from user
         System.out.print("\tEnter your hotel ID: ");
         int chosenHotelID = Integer.parseInt(in.readLine());

         // gets date from user
         System.out.print("\tEnter the day you want to book a room and check its avaliability: (MM/DD/YYYY): ");
         String chosenDate = in.readLine();

         
         String query = String.format("SELECT DISTINCT R.hotelID, R.roomNumber, R.price FROM RoomBookings B, Rooms R WHERE R.hotelID = '%s' AND R.roomNumber NOT IN (SELECT B.roomNumber FROM RoomBookings B WHERE B.hotelID = R.hotelID AND B.bookingDate = '%s') ORDER BY R.roomNumber", chosenHotelID, chosenDate);

         // prints out whether or not any results were produced 
         if(esql.executeQuery(query) != 0) { // there are rooms at that hotel avaliable on that day!
            System.out.println("\n== These are the rooms avaliable that day! ==\n");
         }
         else if(esql.executeQuery(query) == 0) {
            System.out.println("\nSorry, there are no rooms avaliable on that day.");
         }
         // actually executes the query here :)
         esql.executeQueryAndPrintResult(query);

         String notFreeRoomsQuery = String.format("SELECT DISTINCT R.hotelID, R.roomNumber, R.price FROM RoomBookings B, Rooms R WHERE R.hotelID = '%s' AND R.roomNumber IN (SELECT B.roomNumber FROM RoomBookings B WHERE B.hotelID = R.hotelID AND B.bookingDate = '%s') ORDER BY R.roomNumber", chosenHotelID, chosenDate);
         if(esql.executeQuery(notFreeRoomsQuery) != 0) { // there are also room that aren't avaliable 
            System.out.println("\n\n== These are the rooms that are not avaliable ==\n");
            esql.executeQueryAndPrintResult(notFreeRoomsQuery);
         }
         else
         {
            System.out.println("You're in luck! All rooms are avaliable at this hotel on this date!\n");
         }
      }
      catch(Exception e) {
         System.err.println (e.getMessage ());
      }
   }//end viewRooms()

   /* == bookRooms() function= ==
    * Asks users to input ID, hotelID, roomNumber, day to book hotel room
    * then books the room if possible!
    **/

   // AMY - USERID 2, desiredHotelID 1, desireRoomNumber 5, bookingDate 05/13/2015 (booking avaliable!)
   // managerID 25
   public static void bookRooms(Hotel esql) {
      try {
         System.out.println(
         "\n\n*******************************************************\n" +
         "              Booking Rooms      	               \n" +
         "*******************************************************\n");

         // gets userID from user as well (just for convience sake)
         System.out.print("\tEnter userID: ");
         String custID = in.readLine();

         // gets hotelID from user
         System.out.print("\tEnter your desired hotel ID: ");
         int chosenHotelID = Integer.parseInt(in.readLine());

         // gets a roomNumber from the user
         System.out.print("\tEnter your desired room number: ");
         int chosenRoomNumber = Integer.parseInt(in.readLine());
         
         // gets a day to book the room from the user
         System.out.print("\tEnter the day you want to book your room (MM/DD/YYYY): ");
         String chosenBookDate = in.readLine();
         
         // == checks if the query from the user is even possible ==
         // (1) checks if roomNumber exists or not
         String roomAvaliableQuery = String.format("SELECT * FROM Rooms R WHERE R.hotelID = '%s' AND R.roomNumber = '%s'", chosenHotelID, chosenRoomNumber);

         if(esql.executeQuery(roomAvaliableQuery) > 0) {// there is a room that exists in that hotel!
            // (2) checks if room is avaliable or not to book
            String bookingAvaliabileQuery = String.format("SELECT * FROM RoomBookings B, Rooms R WHERE R.hotelID = '%s' AND R.roomNumber = '%s' AND B.bookingDate = '%s' AND B.hotelID = R.hotelID AND B.roomNumber = R.roomNumber", chosenHotelID, chosenRoomNumber, chosenBookDate);

            // executes bookingAvaliabilityQuery --> checks if room is avaliable or not
            // if returns 0 --> then there is no booking yet --> can book!
            if(esql.executeQuery(bookingAvaliabileQuery) == 0) { 
               System.out.println("Your room is avaliable to book. Let's get it booked right now.\n");

               String bookRoomQuery = String.format("INSERT INTO RoomBookings(customerID, hotelID, roomNumber, bookingDate) VALUES ('%s', '%s', '%s', '%s')", custID, chosenHotelID, chosenRoomNumber, chosenBookDate);
               esql.executeUpdate(bookRoomQuery); 

               System.out.println("Room BOOKED!");
            }
            else { // room is booked and unavaliable :(
               System.out.println("Your room is not avaliable. Book your room on another day or make a new choice\n");
            }
         } 
         else { // no room exists
            System.out.println("You indicated a room that does not exist at this hotel! Pleease pick another room number or make a new choice.\n");
         }
      }
      catch(Exception e) {
         System.err.println (e.getMessage ());
      }
   }//end bookRooms()

   /* == viewRecentBookingsfromCustomer() function ==
    * Customers can see last 5 of their recent bookings from RoomBookings
    * able to see hotelID, roomNumber, billingInfo, and date of booking
    * customer can't see booking history of other customers
    **/

    /* == USERID USED: 4 == all the bookings of 4
         bookingID   customerID  hotelID  roomNumber  bookingDate
         273,        4,          13,      9,          11/25/2018
         95,         4,          15,      9,          6/15/2016
         142,        4,          2,       2,          5/11/2016
         428,        4,          8,       3,          7/26/2012
         421,        4,          1,       1,          12/4/2010
    */
   public static void viewRecentBookingsfromCustomer(Hotel esql) {
      try {
         System.out.println(
         "\n\n*******************************************************\n" +
         "            viewRecentBookingsfromCustomer      	               \n" +
         "*******************************************************\n");

         // gets userID
         System.out.print("\tEnter userID: ");
         String userIdentif = in.readLine().trim();

         String query = String.format("SELECT B.bookingDate, B.hotelID, B.roomNumber, R.price FROM RoomBookings B, Rooms R WHERE B.hotelID = R.hotelID AND B.roomNumber = R.roomNumber AND B.customerID = '%s' ORDER BY B.bookingDate DESC LIMIT 5;", userIdentif);

         // checks if there's any bookings yes 
         if(esql.executeQuery(query) != 0) { // yes there are bookings
            System.out.println("== Here's your most recent bookings! ==");
         }
         else {
            System.out.println("You don't have any bookings yet! Go and book some rooms!\n");
         }

         esql.executeQueryAndPrintResult(query);
      }
      catch(Exception e) {
         System.err.println (e.getMessage ());
      }
   }//end viewRecentBookingsfromCustomer()
   
   
   
   /* --- Following functions are for managers only -- */
   public static void updateRoomInfo(Hotel esql) { // Ellen
      try {
         // as kuser for manager id input first
         String[] values = {null, null, null, null, null};
         List<String> validRoomInfo = null; 
         String query; 

         System.out.print("\t(Manager) Enter ID: "); //
         values[0] = in.readLine(); 
         query = String.format("SELECT H.managerUserID, H.hotelID, H.hotelName FROM Users U, Hotel H WHERE U.userID=%s AND U.userID=H.managerUserID AND U.userType='manager';", values[0]);
         System.out.println();
         esql.executeQueryAndPrintResult(query);

         System.out.print("\tEnter a hotel ID: ");
         values[1] = in.readLine();
         query = String.format("SELECT * FROM Rooms R WHERE R.hotelID=%s ORDER BY R.hotelID;", values[1]);
         System.out.println();
         esql.executeQueryAndPrintResult(query);
         
         do {
            try {
               System.out.print("\tEnter a room number: ");
               values[2] = in.readLine();
               query = String.format("SELECT * FROM Rooms WHERE roomNumber='%s' AND hotelID=%s;", values[2], values[1]);
               if (esql.executeQuery(query)==0) {
                  System.out.format("Room number '%s' does not exist at Hotel %s. Please select a valid room number. \n", values[2], values[1]);
                  continue; 
               }else {break;}
            } catch(Exception e) {
               System.err.println("your input is invalid. ");continue; 
            }
         }while(true);
         
         validRoomInfo = esql.executeQueryAndReturnResult(query).get(0);
         System.out.format("Current Room Info: %s at Hotel %s \n", validRoomInfo.get(1).trim(), validRoomInfo.get(0).trim());
         System.out.format("Current Price: %s\nNew Price: (Press Enter to keep current): ", validRoomInfo.get(2));
         values[3] = in.readLine();
         if (values[3].equals("")) {
            values[3] = validRoomInfo.get(2);
         }
         System.out.format("Current image url: %s\nNew image url: (Press Enter to keep current): ", validRoomInfo.get(3).trim());
         values[4] = in.readLine(); 
         if (values[4].equals("")) { 
            values[4] = validRoomInfo.get(3); 
         } 
         System.out.println("\nOriginal Room Info: ");
         esql.executeQueryAndPrintResult(query);
         String update = String.format("UPDATE Rooms SET price = %s, imageURL = '%s' WHERE hotelID = %s AND roomNumber = %s; ", values[3], values[4], values[1], values[2]);
         esql.executeUpdate(update);

         System.out.println("\nUpdated Room Info: ");
         esql.executeQueryAndPrintResult(query); 
         update = String.format("INSERT INTO RoomUpdatesLog (managerID, hotelID, roomNumber, updatedOn) VALUES (%s, %s, '%s', DATE_TRUNC('second', CURRENT_TIMESTAMP::timestamp));", values[0], values[1], values[2] ); 
         esql.executeUpdate(update);

         // RoomUpdatesLog 
         // update room info: price, image url 
         
      }catch(Exception e) {
         System.err.println(e.getMessage());
      }
   } 
   
   
   /* == viewRecentUpdates() function ==
    * Managers can see the info of the last 5 recent updates of their hotel.
    * Managers usually update the following: room information (price, image url) 
    of the hotels they manage
    **/

    /* == USERID USED: 10,Paige,xyz,manager==
         updateNumber,     managerID,     hotelID,    roomNumber,    updatedOn
         46,               10,            13,         6,             2016-09-10 13:45:00
         45,               10,            12,         5,             2016-09-10 13:44:00
         35,               10,            13,         5,             2016-09-10 13:34:00
         34,               10,            12,         4,             2016-09-10 13:33:00
         24,               10,            13,         4,             2016-09-10 13:19:00
    */
   public static void viewRecentUpdates(Hotel esql) { // choice 6
      try {
         System.out.println(
         "\n\n*************************************************************\n" +
         "       Viewing your recent updates to rooms (managerView)     \n" +
         "*************************************************************\n");

         // gets userID
         System.out.print("\tEnter userID: ");
         String userValInput = in.readLine().trim();

         // check if manager or not
         String managerCheckQuery = String.format("SELECT * FROM Users U WHERE U.userID = '%s' and U.userType = 'manager'", userValInput);
         int userIdentity = esql.executeQuery(managerCheckQuery);

         if(userIdentity > 0) { // when user is a manager
            System.out.print("\nHere's your most recent updates to your rooms:\n\n");
            String query = String.format("SELECT U.updatedOn, U.hotelID, U.roomNumber, R.price, R.imageURL FROM RoomUpdatesLog U, Rooms R WHERE U.hotelID = R.hotelID AND U.roomNumber = R.roomNumber AND U.managerID = '%s' ORDER BY U.updatedOn DESC LIMIT 5;", userValInput);
            esql.executeQueryAndPrintResult(query);
         }
         else { // user isn't a manager :(
            System.out.print("This choice is only for managers. Bye!\n");
         }
      }
      catch(Exception e) {
         System.err.println (e.getMessage ());
      }
   }//end viewRecentUpdates()

   /* == viewBookingHistoryofHotel() function ==
    * Managers can see all the booking info of the hotels they manage...
    * can see: bookingID, customerName, hotelID, roomNumber, billing info, dateOfBooking
    * Managers also have the option to input a range of dates.
    * System will show all bookings in that range of dates.
    **/

    /* == USERID USED: 10,Paige,xyz,manager==
                           hotelID,hotelName,   latitude,      longitude,     dateEstablished,  managerUserID
         * hotels managed: 3,      tblw,        9.25529,       14.25107,      12/30/1938,       10
    */
   public static void viewBookingHistoryofHotel(Hotel esql) { // choice 7
      try {
         System.out.println(
         "\n\n*******************************************************\n" +
         "        Viewing your hotel's booking history!      \n" +
         "*******************************************************\n");

         // gets userID
         System.out.print("\tEnter userID: ");
         String userIdentif = in.readLine().trim();

         // query that checks if user is of userType manager or not
         String query = String.format("SELECT * FROM Users U WHERE U.userID = '%s' and U.userType = 'manager'", userIdentif);
         int userIdentity = esql.executeQuery(query);

         // code below executes on whether or not the userType is "manager" or not
         // 1/30/2002
         // 5/17/2002
         if(userIdentity > 0) { // when user is a manager
            System.out.print("\tDo you want your hotel's booking history in a certain range of dates (y/n)?: ");
            String dateRangeIdentif = in.readLine();

            if(dateRangeIdentif.equals("y")) { // yes, user wants a range
               // enter starting date of range
               System.out.print("\tEnter the starting date of the bookings you want in this format (MM/DD/YYYY): ");
               String startDate = in.readLine();

               // enter ending date of range
               System.out.print("\tEnter the ending date of the bookings you want in this format (MM/DD/YYYY): ");
               String endDate = in.readLine();

               String query1 = String.format("SELECT B.bookingID, U.name, B.hotelID, B.roomNumber, R.price, B.bookingDate FROM RoomBookings B, Users U, Rooms R, Hotel H WHERE H.hotelID = B.hotelID AND H.hotelID = R.hotelID AND B.roomNumber = R.roomNumber AND H.managerUserID = '%s' AND U.userID = B.customerID AND B.bookingDate BETWEEN '%s' AND '%s' ORDER BY B.bookingDate", userIdentif, startDate, endDate);
               System.out.println();
               esql.executeQueryAndPrintResult(query1);
               
            }
            else if(dateRangeIdentif.equals("n")) { // no range :(
               // query prints out ALL the bookings of the hotel the manager manages
               System.out.println();
               String query2 = String.format("SELECT B.bookingID, U.name, B.hotelID, B.roomNumber, R.price, B.bookingDate FROM RoomBookings B, Users U, Rooms R, Hotel H WHERE H.hotelID = B.hotelID AND H.hotelID = R.hotelID AND B.roomNumber = R.roomNumber AND H.managerUserID = '%s' AND U.userID = B.customerID ORDER BY B.bookingDate", userIdentif);
               esql.executeQueryAndPrintResult(query2);
            }
            else {
               System.out.print("Please pick either y for yes or n for no. Try again!\n");
            }
         }
         else { // when user isn't a manager
            System.out.print("\nThis choice is only for managers. Bye!\n\n");
         }
      }
      catch(Exception e) {
         System.err.println (e.getMessage ());
      }
   }//end viewBookingHistoryofHotel()



   public static void viewRegularCustomers(Hotel esql) { //Ellen
      try {
         String query; 
         System.out.print("\t(Manager) Enter ID: "); // is there a global variable that has the saved userID (when user logs in) ?? if not, i'm going to leave it as just entering manager_id because that works, if i do String userID in class defined above, then I get an error (not-null constraint)
         String manager_id = in.readLine();  //  
                
         query = String.format("SELECT H.managerUserID, H.hotelID, H.hotelName FROM Users U, Hotel H WHERE U.userID=%s AND U.userID=H.managerUserID AND U.userType='manager';", manager_id); // 
         esql.executeQueryAndPrintResult(query); // 

         System.out.print("\tEnter hotel ID: ");
         String hotel_id = in.readLine().trim();

         query = String.format("SELECT H.hotelName FROM Hotel H, Users U WHERE U.userID=%s AND U.userID=H.managerUserID AND H.hotelID=%s; \n", manager_id, hotel_id);
         esql.executeQueryAndPrintResult(query);

         System.out.print("\n=============== REGULAR CUSTOMERS ===============\n");
         query = String.format("SELECT B.customerID, U.name, COUNT(*) as NumOfBookings FROM RoomBookings B, Users U WHERE B.hotelID='%s' AND B.customerID=U.userID GROUP BY B.customerID, U.name ORDER BY COUNT(*) DESC LIMIT 5; ", hotel_id);
         esql.executeQueryAndPrintResult(query);

      } catch(Exception e) {
         System.err.println(e.getMessage());
      }
   }
   
   
   public static void placeRoomRepairRequests(Hotel esql) { // Ellen // check (updating tables part)
      try{ 
         String query; 
         String[] values = {null, null, null, null};
                        // managerID, hotelID, roomNumber, companyID
         List<String> updatedRequest = null; 

         System.out.print("\tEnter manager ID: ");
         values[0] = in.readLine();
         query = String.format("SELECT H.managerUserID, H.hotelID, H.hotelName FROM Users U, Hotel H WHERE U.userID=%s AND U.userID=H.managerUserID AND U.userType='manager';", values[0]);
         System.out.println();
         esql.executeQueryAndPrintResult(query);

         do {
            try {
               System.out.print("\tEnter hotel ID: ");
               values[1] = in.readLine();
               query = String.format("SELECT hotelID, hotelName, managerUserID FROM Hotel WHERE hotelID='%s' AND managerUserID=%s;", values[1], values[0]); //
               esql.executeQueryAndPrintResult(query);
               if (esql.executeQuery(query)==0) {
                  System.out.format("This manager doesn't manage that hotel.\n");
                  continue;
               }else{break;}
            }catch(Exception e) {
               System.err.println("invalid input");
               continue; 
            }
         }while(true);
         
         query = String.format("SELECT * FROM Rooms R WHERE R.hotelID=%s ORDER BY R.hotelID;", values[1]);
         System.out.println();
         esql.executeQueryAndPrintResult(query);

         do {
            try {
               System.out.print("\tEnter room number: ");
               values[2] = in.readLine();
               
               query = String.format("SELECT * FROM Rooms WHERE hotelID=%s AND roomNumber='%s';", values[1], values[2]);
               if (esql.executeQuery(query)==0) {
                  System.out.format("Room number does not exist at this hotel. ");
                  continue;
               }else {break;}
            }catch(Exception e) {
               System.err.println("invalid input.\n"); continue;
            }
         }while(true);
         
         do {
            try {
               query = String.format("SELECT * FROM MaintenanceCompany ORDER BY companyID; ");
               esql.executeQueryAndPrintResult(query);
               
               System.out.print("\tEnter company ID: (Maintenance) ");
               values[3] = in.readLine();
               
               if (esql.executeQuery(query)==0) {
                  System.out.format("maintenance company doesn't exist. \n");
                  continue;
               }else{break;}
            }catch(Exception e) {
               System.err.println("invalid input.\n"); continue; 
            }
         }while(true);
         
         System.out.println("Insert into RoomRepairs");
         query = String.format("INSERT INTO RoomRepairs(companyID, hotelID, roomNumber, repairDate) VALUES(%s, %s, %s, DATE_TRUNC('second', CURRENT_TIMESTAMP::timestamp)); ", values[3], values[1], values[2]);
         esql.executeUpdate(query); 

         String getRepairID = String.format("SELECT repairID FROM RoomRepairs WHERE companyID=%s AND hotelID=%s AND roomNumber=%s; ", values[3], values[1], values[2]);
         updatedRequest = esql.executeQueryAndReturnResult(getRepairID).get(0);
         String rid = updatedRequest.get(0);
         
         System.out.println("Insert into RoomRepairRequests"); // note for me: maybe (?) create a trigger for this. (CREATE SEQUENCE repairID_sequence ) 
         query = String.format("INSERT INTO RoomRepairRequests (managerID, repairID) VALUES(%s, %s); ", values[0], rid ); 
         esql.executeUpdate(query);

         System.out.println("\tRoom Repair Request submitted...");
         System.out.println("====RoomRepairs and RoomRepairRequests tables updated...");

         query = String.format("SELECT * FROM RoomRepairs ORDER BY repairID DESC LIMIT 1; ");
         esql.executeQueryAndPrintResult(query);
         query = String.format("SELECT * FROM RoomRepairRequests ORDER BY repairID DESC LIMIT 1; ");
         esql.executeQueryAndPrintResult(query);

      }catch(Exception e){
         System.err.println(e.getMessage());
      }
   }
   
   public static void viewRoomRepairHistory(Hotel esql) { //Ellen
      try {  
         System.out.print("\tEnter manager ID: "); // is there somewhere where when user logs in the ID is saved?
         String manager_id = in.readLine();
         String query = String.format("SELECT H.managerUserID, H.hotelID, H.hotelName FROM Users U, Hotel H WHERE U.userID=%s AND U.userID=H.managerUserID AND U.userType='manager';", manager_id);
         esql.executeQueryAndPrintResult(query);

         System.out.print("\tEnter hotel ID: ");
         String hotel_id = in.readLine();
         
         // assumption: hotel listed are being managed by managers given manager ID input  
         System.out.print("\n=============== Room Repair History ===============\n"); 
         query = String.format("SELECT companyID, hotelID, roomNumber, repairDate FROM RoomRepairs M WHERE EXISTS (SELECT H.managerUserID FROM Hotel H, RoomRepairRequests R WHERE hotelID=%s AND M.hotelID=H.hotelID);", hotel_id);
         esql.executeQueryAndPrintResult(query);

         if (esql.executeQuery(query) == 0) {
            System.out.println("There are no room repairs for this hotel.\n"); 
         }

         //int rows = esql.executeQuery(query);
         //System.out.println("total row(s): " + rows);

         // Managers: 
         //see all room requests history - companyID, hotelID, roomNumber, repairDate  - for the hotels they manage. 
      }catch(Exception e) {
         System.err.println(e.getMessage());
      }
   }
};//end Hotel