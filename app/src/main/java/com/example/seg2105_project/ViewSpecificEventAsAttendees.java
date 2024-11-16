package com.example.seg2105_project;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ViewSpecificEventAsAttendees extends AppCompatActivity {

    private TextView eventTitle, eventDescription, eventDate, eventStartTime, eventEndTime, eventAddress;
    private Button registerButton;
    private DatabaseHelper dbHelper;
    private long event_idLong;
    private int event_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_specific_event_as_attendees);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        eventTitle = findViewById(R.id.eventTitle);
        eventDescription = findViewById(R.id.eventDescription);
        eventDate = findViewById(R.id.eventDate);
        eventStartTime = findViewById(R.id.eventStartTime);
        eventEndTime = findViewById(R.id.eventEndTime);
        eventAddress = findViewById(R.id.eventAddress);
        registerButton = findViewById(R.id.registerButton);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        event_idLong = getIntent().getIntExtra("event_id", -1);
        event_id = (int) event_idLong;

        String title;
        String description;
        String date;
        String start_time;
        String end_time;
        String event_address;

        // Get the event information from database
        Cursor cursor = dbHelper.getEvent(event_id);

        if (cursor.moveToFirst()) {
            title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
            start_time = cursor.getString(cursor.getColumnIndexOrThrow("start_time"));
            end_time = cursor.getString(cursor.getColumnIndexOrThrow("end_time"));
            event_address = cursor.getString(cursor.getColumnIndexOrThrow("event_address"));


            eventTitle.setText(title);
            eventDescription.setText(description);
            eventDate.setText(date);
            eventStartTime.setText(start_time);
            eventEndTime.setText(end_time);
            eventAddress.setText(event_address);
        } else {
            Toast.makeText(ViewSpecificEventAsAttendees.this, "Impossible to access the information of the event. Please try again.", Toast.LENGTH_LONG).show();
        }

    }
}