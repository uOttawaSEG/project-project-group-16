package com.example.seg2105_project;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ViewRegistratedEvents extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private LinearLayout eventContainer;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_registrated_events);

        // Handle insets for a seamless UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize database helper and views
        dbHelper = new DatabaseHelper(this);
        scrollView = findViewById(R.id.scroll_view); // Reference ScrollView
        eventContainer = findViewById(R.id.event_container); // Reference LinearLayout inside ScrollView

        // Load and display registered events
        loadRegisteredEvents();
    }

    private void loadRegisteredEvents() {
        int attendeeId = 1; // Replace with dynamic attendee ID retrieval logic if necessary

        // Query the database for registered events
        Cursor cursor = dbHelper.getRegisteredEventsForAttendee(attendeeId);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Extract event details from the cursor
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String startTime = cursor.getString(cursor.getColumnIndexOrThrow("start_time"));
                String endTime = cursor.getString(cursor.getColumnIndexOrThrow("end_time"));
                String address = cursor.getString(cursor.getColumnIndexOrThrow("event_address"));

                // Create a TextView for each event and configure its display
                TextView eventView = new TextView(this);
                eventView.setText(String.format(
                        "Title: %s\nDescription: %s\nDate: %s\nTime: %s - %s\nAddress: %s",
                        title, description, date, startTime, endTime, address
                ));
                eventView.setPadding(16, 16, 16, 16);
                eventView.setTextSize(16);
                eventView.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);

                // Add click handling to display a Toast
                eventView.setOnClickListener(v -> {
                    Toast.makeText(this, "Selected Event: " + title, Toast.LENGTH_SHORT).show();
                });

                // Add the event view to the container
                eventContainer.addView(eventView);

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