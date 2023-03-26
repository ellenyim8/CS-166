DROP INDEX IF EXISTS Hotel_manager_index;
DROP INDEX IF EXISTS Repairs_repairID_index;
DROP INDEX IF EXISTS Bookings_bookingID_index;
DROP INDEX IF EXISTS Hotel_id_index;
DROP INDEX IF EXISTS Room_number_index;

CREATE INDEX Hotel_manager_index ON Hotel(managerUserID);
CREATE INDEX Repairs_repairID_index ON RoomRepairs(repairID);
CREATE INDEX Bookings_bookingID_index ON RoomBookings(bookingID);
CREATE INDEX Hotel_id_index ON Rooms(hotelID);
CREATE INDEX Room_number_index ON Rooms(roomNumber);