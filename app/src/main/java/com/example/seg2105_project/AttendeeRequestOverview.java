package com.example.seg2105_project;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.TextView;
import android.database.Cursor;
import android.widget.Toast;
import android.content.Intent;
import android.widget.LinearLayout;

public class AttendeeRequestOverview extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private String eventId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_attendee_request_overview);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        //UI element

        dbHelper=new DatabaseHelper(this);
        eventId=getIntent().getStringExtra("eventId");
        if(eventId!=null){
            loadAttendeeRequests(eventId);
        }
        else{
            Toast.makeText(this,"No event Id provided",Toast.LENGTH_SHORT).show();
        }

    }
    //method to view or load attendee request for an event
    private void loadAttendeeRequests(String eventId){
        LinearLayout attendeeRequestsContainer = findViewById(R.id.attendeeRequestsContainer); // Updated reference
        Cursor cursor = dbHelper.getAttendeeForEvent(eventId);

        attendeeRequestsContainer.removeAllViews(); // Clear previous entries

        if (cursor == null) {
            Toast.makeText(this, "Error loading attendee requests.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cursor.moveToFirst()) {
            do {
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phone_number"));

                // Create a new TextView for each attendee
                TextView attendeeView = new TextView(this);
                attendeeView.setText("Name: " + firstName + " " + lastName + "\n" +
                        "Email: " + email + "\n" +
                        "Phone: " + phoneNumber);
                attendeeView.setPadding(16, 16, 16, 16);
                attendeeView.setTextSize(16);

                // Add a click listener for more details
                attendeeView.setOnClickListener(view -> {
                    Intent intent = new Intent(this, AttendeeDetailsActivity.class);
                    intent.putExtra("firstName", firstName);
                    intent.putExtra("lastName", lastName);
                    intent.putExtra("email", email);
                    intent.putExtra("phoneNumber", phoneNumber);
                    startActivity(intent);
                });

                // Add the TextView to the LinearLayout
                attendeeRequestsContainer.addView(attendeeView);

            } while (cursor.moveToNext());
        } else {
            //field validation
            // Display a message if no attendees are found
            TextView noAttendeesView = new TextView(this);
            noAttendeesView.setText("No attendees have requested registration for this event.");
            noAttendeesView.setPadding(16, 16, 16, 16);
            noAttendeesView.setTextSize(16);
            attendeeRequestsContainer.addView(noAttendeesView);
        }

        cursor.close();
    }
}