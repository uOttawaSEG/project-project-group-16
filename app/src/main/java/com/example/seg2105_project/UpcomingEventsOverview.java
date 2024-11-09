package com.example.seg2105_project;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class UpcomingEventsOverview extends AppCompatActivity {

    private ListView eventListView;
    private List<Event> eventList;
    private DatabaseHelper dbHelper;
    private TextView noEventText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_upcoming_events_overview);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        eventListView = findViewById(R.id.listOfTheUpcomingEvents);
        eventList = new ArrayList<>();
        noEventText = findViewById(R.id.noEventText);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);


        // add the upcoming events to the eventList
        Cursor cursor = dbHelper.getUpcomingEvents();

        if (cursor.moveToFirst()) {
            noEventText.setVisibility(View.INVISIBLE);
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String start_time = cursor.getString(cursor.getColumnIndexOrThrow("start_time"));
                String end_time = cursor.getString(cursor.getColumnIndexOrThrow("end_time"));
                String event_address = cursor.getString(cursor.getColumnIndexOrThrow("event_address"));
                String organizer_id = cursor.getString(cursor.getColumnIndexOrThrow("organizer_id"));
                List<String> attendees = new ArrayList<>();

                Event event = new Event(title, description, date, start_time, end_time, event_address, attendees);
                eventList.add(event);
            } while (cursor.moveToNext());


        } else {
            noEventText.setVisibility(View.VISIBLE);
        }

        // Close the cursor when done
        cursor.close();

        // Create the adapter, and set it with the events
        EventAdapter adapter = new EventAdapter(this, eventList);
        eventListView.setAdapter(adapter);


    }



}