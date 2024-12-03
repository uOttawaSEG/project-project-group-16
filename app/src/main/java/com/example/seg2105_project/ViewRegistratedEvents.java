package com.example.seg2105_project;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ViewRegistratedEvents extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private LinearLayout eventContainer;
    private ScrollView scrollView;
    private int attendeeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_registrated_events);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize database helper and views
        dbHelper = new DatabaseHelper(this);
        scrollView = findViewById(R.id.scroll_view); // Reference ScrollView
        eventContainer = findViewById(R.id.event_container); // Reference LinearLayout inside ScrollView
        attendeeId = getIntent().getIntExtra("user_id", -1);; // Replace with dynamic attendee ID retrieval logic if necessary
        // Load and display registered events
        loadRegisteredEvents();
    }

    private void loadRegisteredEvents() {


        // Query the database for registered events
        Cursor cursor = dbHelper.getRegisteredEventsForAttendee(attendeeId);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Extract eventId from the cursor
                int eventId = cursor.getInt(cursor.getColumnIndexOrThrow("event_id"));
                // Extract event details from the cursor
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String startTime = cursor.getString(cursor.getColumnIndexOrThrow("start_time"));
                String endTime = cursor.getString(cursor.getColumnIndexOrThrow("end_time"));
                String address = cursor.getString(cursor.getColumnIndexOrThrow("event_address"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("registration_status"));

                // Create a container (eventLayout) for event details and the cancel button
                LinearLayout eventLayout = new LinearLayout(this);
                eventLayout.setOrientation(LinearLayout.VERTICAL);
                eventLayout.setPadding(16, 16, 16, 16);
                eventLayout.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);

                // Create a TextView for each event and configure its display
                TextView eventView = new TextView(this);
                eventView.setText(String.format(
                        "Title: %s\nDescription: %s\nDate: %s\nTime: %s - %s\nAddress: %s\nStatus: %s",
                        title, description, date, startTime, endTime, address,status
                ));
                eventView.setPadding(16, 16, 16, 16);
                eventView.setTextSize(16);
                eventView.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);

                // Add click handling to display a Toast
                eventView.setOnClickListener(v -> {
                    Toast.makeText(this, "Selected Event: " + title, Toast.LENGTH_SHORT).show();
                });

                // Create a Cancel Button
                Button cancelButton = new Button(this);
                cancelButton.setText("Cancel Registration");

                // Set click listener for the cancel button
                cancelButton.setOnClickListener(v -> {
                    boolean isCancelled = dbHelper.cancelRegistration(attendeeId, eventId);

                    if (isCancelled) {
                        Toast.makeText(this, "Registration cancelled successfully.", Toast.LENGTH_SHORT).show();
                        eventContainer.removeView(eventLayout); // Remove the entire event layout from the UI
                    } else {
                        Toast.makeText(this, "Failed to cancel registration.", Toast.LENGTH_SHORT).show();
                    }
                });

                // Add the TextView and Button to the eventLayout
                eventLayout.addView(eventView);
                eventLayout.addView(cancelButton);

                // Add the eventLayout to the eventContainer
                eventContainer.addView(eventLayout);

            } while (cursor.moveToNext());

            cursor.close();
        } else {
            // Handle case where no events are found
            TextView noEventsView = new TextView(this);
            noEventsView.setText("No registered events found.");
            noEventsView.setPadding(16, 16, 16, 16);
            noEventsView.setTextSize(16);
            eventContainer.addView(noEventsView);
        }
    }
}
