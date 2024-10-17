package com.example.seg2105_project;// Step 1: Import necessary classes

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "EAMS.db";
    private static final int DATABASE_VERSION = 1;


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
                + "organization_name TEXT, "
                + "user_role TEXT CHECK(user_role IN ('Attendee', 'Organizer', 'Administrator')) NOT NULL" // user_role: defines if the user is an Attendee, Organizer, or Admin.
                + ");";


        db.execSQL(createUsersTable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // To remove old data
        db.execSQL("DROP TABLE IF EXISTS Users");

        // To recreate the table with any updates
        onCreate(db);
    }


    public boolean addUser(String firstName, String lastName, String email, String password,
                           String phone, String address, String organizationName, String userRole) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("first_name", firstName);
        values.put("last_name", lastName);
        values.put("email", email);
        values.put("password", password);
        values.put("phone_number", phone);
        values.put("address", address);
        values.put("organization_name", organizationName); // null for Attendees
        values.put("user_role", userRole);

        // Insert the data into the Users table
        long result = db.insert("Users", null, values);

        // Return true if the data was successfully inserted, false otherwise
        return result != -1;
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
}
