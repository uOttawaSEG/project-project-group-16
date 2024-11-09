package com.example.seg2105_project;// Step 1: Import necessary classes

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "EAMS.db";
    private static final int DATABASE_VERSION = 4;


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


    }


    public boolean addUser(String firstName, String lastName, String email, String password,
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
        long result = db.insert("Users", null, values);


        // Return true if the data was successfully inserted, false otherwise
        return result != -1;
    }


    public boolean addEvent(String title,String description, String date, String start_time, String end_time, String event_address, int organizer_id,boolean isManualApproval){


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
        values.put("isManualApproval", isManualApproval);  // Store the approval mode (0 or 1)


        long result=db.insert("Events",null,values);

        //return 1 if the insertion has been done
        return result !=-1;
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
    public Cursor getAttendeeForEvent(String eventId){
        SQLiteDatabase db=this.getReadableDatabase();
        return db.rawQuery(
                "SELECT U.first_name, U.last_name, U.email, U.phone_number " +
                        "FROM Users U " +
                        "JOIN Events E ON E.organizer_id = U.user_id " +
                        "WHERE E.event_id = ? AND U.user_role = 'Attendee' AND U.registration_status = 'pending'",
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




}

