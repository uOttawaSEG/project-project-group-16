package com.example.seg2105_project;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

        // Load upcoming events into eventList
        loadUpcomingEvents();


    }

    private void loadUpcomingEvents(){
        // add the upcoming events to the eventList
        Cursor cursor = dbHelper.getUpcomingEvents();

        if (cursor.moveToFirst()) {
            noEventText.setVisibility(View.INVISIBLE);
            do {
                int event_idIndex = cursor.getColumnIndex("event_id");
                long event_idLong = cursor.getLong(event_idIndex);
                int event_id = (int) event_idLong;
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String start_time = cursor.getString(cursor.getColumnIndexOrThrow("start_time"));
                String end_time = cursor.getString(cursor.getColumnIndexOrThrow("end_time"));
                String event_address = cursor.getString(cursor.getColumnIndexOrThrow("event_address"));
                String organizer_id = cursor.getString(cursor.getColumnIndexOrThrow("organizer_id"));
                List<Attendee> attendees = new ArrayList<>();
                Integer isManualApprovalInt = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("isManualApproval")));
                boolean isManualApproval;
                if (isManualApprovalInt == 1) isManualApproval = true;
                else isManualApproval = false;

                Event event = new Event(event_id, title, description, date, start_time, end_time, event_address,isManualApproval, attendees);
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

    public void deleteEvent(int event_id) {
        boolean isDeleted = dbHelper.deleteEventById(event_id);
        if (isDeleted) {
            Toast.makeText(this, "Event deleted successfully", Toast.LENGTH_SHORT).show();
            loadUpcomingEvents(); // Reload the event list to reflect changes
        } else {
            Toast.makeText(this, "Failed to delete the event", Toast.LENGTH_SHORT).show();
        }
    }


}