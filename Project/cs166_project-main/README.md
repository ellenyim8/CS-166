# cs166_project

## What this project is about
This is a hotel management database system. There are many useful functions implemented in the program that can find the query needed like browsing hotels nearby, booking a room, viewing a room, (for managers only) updating room info, placing a room repair request, and etc. 

## Features 
- Browse Hotels - viewHotels(esql) [Ellen]
    - Allows user to input latitude and longitude and gives a list of hotel ID and hotel names within 30 units distance of given input (using calculate_distance() function); selected columns are hotelID, hotelName for query result.

- Browse/View Rooms: viewRooms(Hotel esql) [Paulian] 
    - User inputs their desired hotel ID and a date they want to book a room on. Program then outputs a list of available rooms and unavailable rooms (if there are any). If there are no rooms available at all, the program should indicate this too.
    
- Book Room [Paulian] 
    - User inputs their user ID, desired hotel ID, desired room number and a day they want to book that room on. Function then books the room if that it is available on that day in that hotel.
   
- Update Room Information - updateRoomInfo(esql) [Ellen] 
    - Checks manager ID, asks for hotelID and roomNumber, then asks if want to change the price of the room or remain the same, and also ask if wants to update the image; Rooms and RoomUpdateLog tables are updated and inserted into, respectfully 

- Browse booking history: viewBookingHistoryofHotel(Hotel esql) [Paulian] 
    - Is a function for just managers. Asks managers to input their user ID and let’s them see all the booking information for the hotels they manage. They can see the following: bookingID, customerName, hotelID, roomNumber, roomPrice, and bookingDate.
    - Managers also have an option to input a range of dates. System will show all bookings in that range of dates.

- View Recent Updates: viewRecentUpdates(Hotel esql) [Paulian] 
    - Managers can see the 5 most recent updates they did to a room. The function returns the following info of the 5 most recent updates: when the room was updated, the hotelID, room number, price, and imageURL.
 
 - Place Room Repair Request - placeRoomRepairRequests(esql)  [Ellen]
    - Checking to see if user is a manager by asking for managerID, asks user for hotelID, roomNumber, and companyID, then updates RoomRepairs and RoomRepairs request tables respectively

- View Room Repair History - viewRoomRepairHistory(esql) [Ellen] 
    - Given manager id (to check if user is a manager), will ask for a hotel ID that they manage, and then returns list of repairs made for that room (companyID, hotelID, roomNumber, repairDate) 

- View (5) Regular Customers - viewRegularCustomers(esql) [Ellen] 
    - For Managers (enter manager ID), enter a hotel ID that manager is managing, top 5 customers who made most bookings for the given hotel will be listed
    
- View Recent bookings from Customer: viewRecentBookingsfromCustomer(Hotel esql)  [Paulian]
    - Gives the 5 most recent bookings of a user. The user should be able to see the following information after inputting their userID (which is the same thing as customerID): hotelID, roomNumber, billingInfo, dateOfBooking
    - Note - that the customer can’t see the information of the other customers
