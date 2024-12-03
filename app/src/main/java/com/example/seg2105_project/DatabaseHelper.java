package com.example.seg2105_project;// Step 1: Import necessary classes

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "EAMS.db";
    private static final int DATABASE_VERSION = 12;

    private static final String TABLE_EVENTS = "events";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_ORGANIZER_EMAIL = "organizer_email";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // This table table will store all the information about attendees, organizers, and administrators.
        String createUsersTable = "CREATE TABLE Users ("
                + "user_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "first_name TEXT NOT NULL, "
                + "last_name TEXT NOT NULL, "
                + "email TEXT NOT NULL UNIQUE, "
                + "password TEXT NOT NULL, "
                + "phone_number TEXT NOT NULL, "
                + "address TEXT NOT NULL, "
                + "registration_status TEXT, " // pending approved rejected
                + "organization_name TEXT, "
                + "user_role TEXT CHECK(user_role IN ('Attendee', 'Organizer', 'Administrator')) NOT NULL" // user_role: defines if the user is an Attendee, Organizer, or Admin.
                + ");";


        db.execSQL(createUsersTable);


        //This table will store the information about events
        String createEventsTable = "CREATE TABLE Events ("
                + "event_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "title TEXT NOT NULL, "
                + "description TEXT NOT NULL, "
                + "date TEXT NOT NULL ,"
                + "start_time TEXT NOT NULL ,"
                + "end_time TEXT NOT NULL, "
                + "event_address TEXT NOT NULL ,"
                + "eventState TEXT NOT NULL, "
                + "organizer_id INTEGER NOT NULL, "
                + "isManualApproval INTEGER DEFAULT 0, "  // 0 = automatic, 1 = manual
                + "FOREIGN KEY (organizer_id) REFERENCES Users(user_id) "
                + ");";


        db.execSQL(createEventsTable);


        String createEventAttendeesTable = "CREATE TABLE EventAttendees ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "event_id INTEGER NOT NULL, "
                + "attendee_id INTEGER NOT NULL, "
                + "registration_status TEXT DEFAULT 'pending', "
                + "FOREIGN KEY (event_id) REFERENCES Events(event_id), "
                + "FOREIGN KEY (attendee_id) REFERENCES Users(user_id)"
                + ");";
        db.execSQL(createEventAttendeesTable);
        // Debugging code
        Cursor cursor = db.rawQuery("SELECT * FROM EventAttendees", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Log.d("Database Debug",
                        "Row - Attendee ID: " + cursor.getInt(cursor.getColumnIndexOrThrow("attendee_id")) +
                                ", Event ID: " + cursor.getInt(cursor.getColumnIndexOrThrow("event_id")) +
                                ", Status: " + cursor.getString(cursor.getColumnIndexOrThrow("registration_status")));
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            Log.e("Database Debug", "No rows found in EventAttendees table.");
        }
        Log.d("Schema Debug", "Starting PRAGMA table_info query...");
        Cursor cursor1 = db.rawQuery("PRAGMA table_info(EventAttendees);", null);
        if (cursor1 != null && cursor1.moveToFirst()) {
            do {
                Log.d("Schema Debug", "Column: " + cursor1.getString(cursor1.getColumnIndexOrThrow("name")) +
                        ", Type: " + cursor1.getString(cursor1.getColumnIndexOrThrow("type")));
            } while (cursor1.moveToNext());
            cursor1.close();
        } else {
            Log.e("Schema Debug", "No schema info found for EventAttendees table.");
        }
        Log.d("Schema Debug", "Finished PRAGMA table_info query...");
        // Table for storing registrations
        String createRegistrationsTable = "CREATE TABLE Registrations ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "event_id INTEGER NOT NULL, "
                + "attendee_id INTEGER NOT NULL, "
                + "FOREIGN KEY(event_id) REFERENCES Events(event_id), "
                + "FOREIGN KEY(attendee_id) REFERENCES Users(user_id)"
                + ");";
        db.execSQL(createRegistrationsTable);



    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // To upgrade the database to version 2 (added registration_status)
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE Users ADD COLUMN registration_status TEXT");
        }

        if (oldVersion < 3) {
            String createEventsTable = "CREATE TABLE Events ("
                    + "event_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "title TEXT NOT NULL, "
                    + "description TEXT NOT NULL, "
                    + "date TEXT NOT NULL ,"
                    + "start_time TEXT NOT NULL ,"
                    + "end_time TEXT NOT NULL, "
                    + "event_address TEXT NOT NULL ,"
                    + "eventState TEXT NOT NULL, "
                    + "organizer_id INTEGER NOT NULL, "
                    + "isManualApproval INTEGER DEFAULT 0, "  // 0 = automatic, 1 = manual
                    + "FOREIGN KEY (organizer_id) REFERENCES Users(user_id) "
                    + ");";
            db.execSQL(createEventsTable);
        }

        if (oldVersion < 4) {  // added eventState
            db.execSQL("ALTER TABLE Events ADD COLUMN eventState TEXT");
        }
        if (oldVersion < 5) {  // Added EventAttendees table
            String createEventAttendeesTable = "CREATE TABLE EventAttendees ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "event_id INTEGER NOT NULL, "
                    + "attendee_id INTEGER NOT NULL, "
                    + "registration_status TEXT DEFAULT 'pending', "
                    + "FOREIGN KEY (event_id) REFERENCES Events(event_id), "
                    + "FOREIGN KEY (attendee_id) REFERENCES Users(user_id)"
                    + ");";
            db.execSQL(createEventAttendeesTable);
        }

        if (oldVersion < 6) {   // added isManualApproval column
            db.execSQL("ALTER TABLE Events ADD COLUMN isManualApproval INTEGER");
        }
        if (oldVersion < 7) {  // Increment database version for Registrations table
            String createRegistrationsTable = "CREATE TABLE Registrations (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "event_id INTEGER NOT NULL, " +
                    "attendee_id INTEGER NOT NULL, " +
                    "FOREIGN KEY(event_id) REFERENCES Events(event_id), " +
                    "FOREIGN KEY(attendee_id) REFERENCES Users(user_id)" +
                    ");";
            db.execSQL(createRegistrationsTable);
        }

    }


    public long addUser(String firstName, String lastName, String email, String password,
                           String phone, String address, String registrationStatus, String organizationName, String userRole) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("first_name", firstName);
        values.put("last_name", lastName);
        values.put("email", email);
        values.put("password", password);
        values.put("phone_number", phone);
        values.put("address", address);
        values.put("registration_status", "pending"); // pending approved rejected
        values.put("organization_name", organizationName); // null for Attendees
        values.put("user_role", userRole);

        // Insert the data into the Users table
        long userId = db.insert("Users", null, values);

        // for logcat

        if (userId == -1) {
            Log.e("Debug", "Failed to add user: " + email);
        } else {
            Log.d("Debug", "User added with ID: " + userId);
        }

        // Return true if the data was successfully inserted, false otherwise
        return userId;
    }


    public long addEvent(String title,String description, String date, String start_time, String end_time, String event_address, int organizer_id,boolean isManualApproval){


        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues values= new ContentValues();
        values.put("title", title);
        values.put("description",description);
        values.put("date", date);
        values.put("start_time",start_time);
        values.put("end_time",end_time);
        values.put("event_address", event_address);
        values.put("eventState", "upcoming");
        values.put("organizer_id",organizer_id);
        values.put("isManualApproval", isManualApproval ? 1 : 0);  // Store the approval mode (0 or 1)


        long eventId = db.insert("Events", null, values);

        //return 1 if the insertion has been done
        return eventId;
    }

    public String checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Users", new String[]{"user_role"}, "email=? AND password=?", new String[]{email, password}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String userRole = cursor.getString(cursor.getColumnIndex("user_role"));

            cursor.close();
            return userRole;  // Return the user role
        }

        if (cursor != null) {
            cursor.close(); // Make sure to close the cursor if it’s not null
        }

        return null; // Return null if no user found
    }

    // Method to fetch all users with 'pending' registration status
    public Cursor getPendingRegistrationRequests() {
        // Get a readable version of the database
        SQLiteDatabase db = this.getReadableDatabase();

        // Perform the query to fetch users where registration_status = 'pending'
        return db.query(
                "Users",                 // Table name
                null,                         // Columns to return (null means all columns)
                "registration_status=?",     // WHERE clause to filter by pending status
                new String[]{"pending"},    // Argument for the WHERE clause (pending status)
                null,                      // GROUP BY clause (not needed)
                null,                     // HAVING clause (not needed)
                null                     // ORDER BY clause (null means no specific order)
        );
    }


    // Method to fetch all users with 'rejected' registration status
    public Cursor getRejectedRegistrationRequests() {
        // Get a readable version of the database
        SQLiteDatabase db = this.getReadableDatabase();

        // Perform the query to fetch users where registration_status = 'pending'
        return db.query(
                "Users",                 // Table name
                null,                         // Columns to return (null means all columns)
                "registration_status=?",     // WHERE clause to filter by pending status
                new String[]{"rejected"},    // Argument for the WHERE clause (pending status)
                null,                      // GROUP BY clause (not needed)
                null,                     // HAVING clause (not needed)
                null                     // ORDER BY clause (null means no specific order)
        );
    }

    //update the registration_status of the user  based on the email
    public boolean approveRegistrationRequest(String email){
        //open the dataBase to modify it
        SQLiteDatabase db=this.getWritableDatabase();
        //create an abject to store the update
        ContentValues values=new ContentValues();
        //set the new value
        values.put("registration_status","approved");
        //Update the user Table where the email matches
        int rowAffected= db.update("Users",values,"email=?",new String[]{email});
        //return true if the row was updated and false if not
        return(rowAffected>0);


    }

    //update the registration_status of the user based on the email
    public boolean rejectRegistrationRequest(String email){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("registration_status","rejected");
        int rowAffected= db.update("Users",values,"email=?",new String[]{email});
        return(rowAffected>0);
    }


    public String getRegistrationStatus(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Users", new String[]{"registration_status"}, "email = ?", new String[]{email}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String status = cursor.getString(cursor.getColumnIndex("registration_status"));
            cursor.close();
            return status;
        }
        return null; // Si l'utilisateur n'existe pas ou s'il n'a pas de statut
    }



    //get Organizer Id
    /*
        @SuppressLint("Range")
    public int getUserId(String email){
    SQLiteDatabase db=this.getReadableDatabase(); // access data base
    int userId=-1; // no organizer found with the given email
    Cursor cursor=db.query(
            "Users",                 // Table name
            null,                         // Columns to return (null means all columns)
            "email= ?",     // WHERE clause to filter by the state of the event (past or upcoming)
            new String[]{email},    // Argument for the WHERE clause (upcoming event)
            null,                      // GROUP BY clause (not needed)
            null,                     // HAVING clause (not needed)
            null                     // ORDER BY clause (null means no specific order)

    );

    if ( cursor!=null && cursor.moveToFirst()){
        userId=cursor.getInt(cursor.getColumnIndex("userId"));
        cursor.close();
    }
        return userId;
    }
     */




    // Method to fetch all upcoming events
    public Cursor getUpcomingEvents() {
        // Get a readable version of the database
        SQLiteDatabase db = this.getReadableDatabase();

        // Perform the query to fetch upcoming events
        return db.query(
                "Events",                 // Table name
                null,                         // Columns to return (null means all columns)
                "eventState= ?",     // WHERE clause to filter by the state of the event (past or upcoming)
                new String[]{"upcoming"},    // Argument for the WHERE clause (upcoming event)
                null,                      // GROUP BY clause (not needed)
                null,                     // HAVING clause (not needed)
                null                     // ORDER BY clause (null means no specific order)
        );

    }



    // Method to fetch all past events
    public Cursor getPastEvents() {
        // Get a readable version of the database
        SQLiteDatabase db = this.getReadableDatabase();

        // Perform the query to fetch past events
        return db.query(
                "Events",                 // Table name
                null,                         // Columns to return (null means all columns)
                "eventState= ?",     // WHERE clause to filter by the state of the event (past or upcoming)
                new String[]{"past"},    // Argument for the WHERE clause (upcoming event)
                null,                      // GROUP BY clause (not needed)
                null,                     // HAVING clause (not needed)
                null                     // ORDER BY clause (null means no specific order)
        );
    }

    //method to fetch attendee for a specific event
    public Cursor getAttendeeForEvent(String eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT EA.id, U.first_name, U.last_name, U.email, U.phone_number, EA.registration_status " +
                        "FROM Users U " +
                        "JOIN EventAttendees EA ON EA.attendee_id = U.user_id " +
                        "WHERE EA.event_id = ? AND EA.registration_status = 'pending'",
                new String[]{eventId}
        );
    }

    public Cursor getUpcomingEventsWithApprovalMode(int approvalMode) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(
                "Events",
                null,                        // All columns
                "eventState=? AND isManualApproval=?",   // Filter by event state and approval mode
                new String[]{"upcoming", String.valueOf(approvalMode)},
                null,
                null,
                null
        );
    }
    public boolean updateEventApprovalMode(int eventId, int isManualApproval) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("isManualApproval", isManualApproval);

        int rowsAffected = db.update("Events", values, "event_id=?", new String[]{String.valueOf(eventId)});
        return rowsAffected > 0;
    }
    public boolean approveAllRegistrationsForEvent(int eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("registration_status", "approved");

        int rowsAffected = db.update(
                "EventAttendees",
                values,
                "event_id = ? AND registration_status = ?",
                new String[]{String.valueOf(eventId), "pending"}
        );

        return rowsAffected > 0;
    }

    public boolean updateRegistrationStatus(int attendeeId, int eventId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("registration_status", status);
        Log.d("Update Debug", "Attempting to update Attendee ID: " + attendeeId + ", Event ID: " + eventId + ", New Status: " + status);
        int rowsAffected = db.update(
                "EventAttendees",
                values,
                "attendee_id = ? AND event_id = ?" ,
                new String[]{String.valueOf(attendeeId), String.valueOf(eventId)}
        );
        if (rowsAffected > 0) {
            Log.d("Update Debug", "Update successful. Rows affected: " + rowsAffected);
        } else {
            Log.e("Update Debug", "Update failed. No rows affected. Verify IDs and database schema.");
        }
        // Debug current row
        Cursor cursor = db.rawQuery("SELECT * FROM EventAttendees WHERE attendee_id = ? AND event_id = ?",
                new String[]{String.valueOf(attendeeId), String.valueOf(eventId)});
        if (cursor != null && cursor.moveToFirst()) {
            Log.d("Database Debug",
                    "Row - Attendee ID: " + cursor.getInt(cursor.getColumnIndexOrThrow("attendee_id")) +
                            ", Event ID: " + cursor.getInt(cursor.getColumnIndexOrThrow("event_id")) +
                            ", Status: " + cursor.getString(cursor.getColumnIndexOrThrow("registration_status")));
            cursor.close();
        } else {
            Log.e("Database Debug", "No matching row found after update attempt.");
        }
        return rowsAffected > 0;
    }

    public boolean checkEventConflict(int organizerId, String startTime, String endTime, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM events WHERE organizer_id = ? AND event_date = ? AND " +
                "(start_time < ? AND end_time > ? OR start_time < ? AND end_time > ?)";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(organizerId), date, endTime, startTime, startTime, endTime});

        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return true; // Conflict exists
        }
        return false; // No conflict
    }

    public boolean deleteEventById(int event_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete("events", "event_id = ?", new String[]{String.valueOf(event_id)});
        db.close();
        return rowsDeleted > 0;
    }

    public Cursor getEvent(int event_id){
        // Get a readable version of the database
        SQLiteDatabase db = this.getReadableDatabase();

        // Perform the query to fetch upcoming events
        return db.query(
                "Events",                 // Table name
                null,                         // Columns to return (null means all columns)
                "event_id= ?",               // WHERE clause to filter by the state of the event (event_id)
                new String[]{String.valueOf(event_id)},    // Argument for the WHERE clause (event_id)
                null,                      // GROUP BY clause (not needed)
                null,                     // HAVING clause (not needed)
                null                     // ORDER BY clause (null means no specific order)
        );
    }

    public boolean registerAttendeetoEvent(int attendeeId, int eventId){

        SQLiteDatabase dbHelper= this.getWritableDatabase();

        // verify if the attendee is already registered

        Cursor cursor = dbHelper.rawQuery("SELECT * FROM EventAttendees WHERE attendee_id = ? AND event_id = ?",
                new String[]{String.valueOf(attendeeId), String.valueOf(eventId)});

        if (cursor.moveToFirst()){
            cursor.close();
            return false; // attendee already registered
        }

        cursor.close();
        // Check the approval mode of the event
        Cursor eventCursor = dbHelper.query(
                "Events",
                new String[]{"isManualApproval"},
                "event_id = ?",
                new String[]{String.valueOf(eventId)},
                null, null, null
        );

        String registrationStatus = "approved"; // Default for automatic approval
        if (eventCursor != null && eventCursor.moveToFirst()) {
            int isManualApproval = eventCursor.getInt(eventCursor.getColumnIndexOrThrow("isManualApproval"));
            if (isManualApproval == 1) { // Manual approval
                registrationStatus = "pending";
            }
            eventCursor.close();
        }


        // attendee not registered
        ContentValues values = new ContentValues();
        values.put("attendee_id", attendeeId);
        values.put("event_id", eventId);
        values.put("registration_status", registrationStatus); // default value is "registered"

        long result= dbHelper.insert("EventAttendees", null,values);
        return result !=-1 ;// TRUE if successful registration


    }

//    public Cursor getRegisteredEventsForAttendee(int attendeeId){
//        SQLiteDatabase dbHelper= this.getWritableDatabase();
//
//        String query =  "SELECT E.* " +
//                "FROM Events E " +
//                "JOIN EventAttendees EA ON E.event_id = EA.event_id " +
//                "WHERE EA.attendee_id = ?"+
//                "ORDER BY E.date DESC"; // Newest events at the top
//
//        return dbHelper.rawQuery(query, new String [] {String.valueOf(attendeeId)});
//    }
public Cursor getRegisteredEventsForAttendee(int attendeeId){
    SQLiteDatabase dbHelper= this.getWritableDatabase();

    String query =  "SELECT E.*, EA.registration_status " +
            "FROM Events E " +
            "JOIN EventAttendees EA ON E.event_id = EA.event_id " +
            "WHERE EA.attendee_id = ?"+
            "ORDER BY E.date DESC"; // Newest events at the top

    return dbHelper.rawQuery(query, new String [] {String.valueOf(attendeeId)});
}


    // Methods to verify if the email and the phoneNumber exist in the SQLite Database
    public boolean emailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                "Users",
                new String[]{"email"},
                "email = ?",
                new String[]{email},
                null, null, null
        );

        boolean exists = cursor.getCount() > 0;
        return exists;
    }

    public boolean phoneExists(String phoneNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                "Users",
                new String[]{"phone_number"},
                "phone_number = ?",
                new String[]{phoneNumber},
                null, null, null
        );

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    public boolean requestEventRegistration(String attendeeId, String eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("attendeeId", attendeeId); // Add attendee ID
        values.put("eventId", eventId);       // Add event ID
        values.put("status", "Pending");      // Default status is Pending
        values.put("timestamp", System.currentTimeMillis()); // Add request time

        long result = db.insert("EventRegistrations", null, values);
        db.close();

        return result != -1; // Return true if insertion succeeded
    }
    // Convert date and time string to milliseconds
    private long parseDateTimeToMillis(String dateTime) {
        try {
            // Define the format of your date and time strings
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

            // Parse the dateTime string into a Date object
            Date date = sdf.parse(dateTime);

            // Return the time in milliseconds
            return date != null ? date.getTime() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0; // Return 0 in case of an error
        }
    }
    //Handle event registration cancellation
    public boolean cancelRegistration(int attendeeId, int eventId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Step 1: Check the event timing
        Cursor eventCursor = db.query(
                "Events",
                new String[]{"date", "start_time"},
                "event_id = ?",
                new String[]{String.valueOf(eventId)},
                null, null, null
        );

        if (eventCursor != null && eventCursor.moveToFirst()) {
            String eventDate = eventCursor.getString(eventCursor.getColumnIndexOrThrow("date"));
            String startTime = eventCursor.getString(eventCursor.getColumnIndexOrThrow("start_time"));
            eventCursor.close();

            // Combine date and start_time
            String eventDateTime = eventDate + " " + startTime;
            long eventTimeMillis = parseDateTimeToMillis(eventDateTime);
            long currentTimeMillis = System.currentTimeMillis();

            // Check if event starts in less than 24 hours
            if (eventTimeMillis - currentTimeMillis < 24 * 60 * 60 * 1000) {
                return false; // Event is too close to start
            }
        }

        // Step 2: Check if the registration status is pending
        Cursor regCursor = db.query(
                "EventAttendees",
                new String[]{"registration_status"},
                "attendee_id = ? AND event_id = ?",
                new String[]{String.valueOf(attendeeId), String.valueOf(eventId)},
                null, null, null
        );

        if (regCursor != null && regCursor.moveToFirst()) {
            String status = regCursor.getString(regCursor.getColumnIndexOrThrow("registration_status"));
            regCursor.close();

            if (!status.equals("pending")) {
                return false; // Registration is not pending
            }
        } else {
            // No matching registration found
             return false;
        }

        // Step 3: Cancel the registration
        int rowsDeleted = db.delete(
                "EventAttendees",
                "attendee_id = ? AND event_id = ?",
                new String[]{String.valueOf(attendeeId), String.valueOf(eventId)}
        );

        return rowsDeleted > 0; // Return true if a row was deleted
    }

    public boolean isEventRegistered(int eventId, int attendeeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT 1 FROM Registrations WHERE event_id = ? AND attendee_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(eventId), String.valueOf(attendeeId)});
        boolean isRegistered = cursor.getCount() > 0;
        cursor.close();
        return isRegistered;
    }



// exclude events from search bar
    public Cursor getUpcomingEventsExcludingRegistered(int attendeeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Events WHERE eventState = 'upcoming' AND event_id NOT IN (" +
                "SELECT event_id FROM EventAttendees WHERE attendee_id = ?)";
        return db.rawQuery(query, new String[]{String.valueOf(attendeeId)});
    }


    // fetch attendee's id
    public int getUserId(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Log l'email pour voir ce qui est passé à la méthode
        Log.d("Debug", "Fetching User ID for email: " + email);

        Cursor cursor = db.query(
                "Users",
                new String[]{"user_id"},
                "email = ?",
                new String[]{email},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
            Log.d("Debug", "User ID found: " + userId); // Affiche l'ID trouvé
            cursor.close();
            return userId;
        }

        // Si aucun ID trouvé, log l'erreur
        Log.e("Debug", "No User ID found for email: " + email);

        if (cursor != null) {
            cursor.close();
        }
        return -1;
    }


    public boolean isUserRegisteredForEvent(int attendeeId, int eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT 1 FROM EventAttendees WHERE attendee_id = ? AND event_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(attendeeId), String.valueOf(eventId)});

        boolean isRegistered = cursor.moveToFirst(); // Si un enregistrement existe, retourne vrai
        cursor.close();
        return isRegistered;
    }



    public HashSet<Integer> getRegisteredEventIds(int attendeeId) {
        HashSet<Integer> registeredEventIds = new HashSet<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT event_id FROM EventAttendees WHERE attendee_id = ?",
                new String[]{String.valueOf(attendeeId)}
        );

        if (cursor.moveToFirst()) {
            do {
                registeredEventIds.add(cursor.getInt(0)); // Ajoute chaque ID d'événement à l'ensemble
            } while (cursor.moveToNext());
        }

        cursor.close();
        return registeredEventIds;
    }






}

