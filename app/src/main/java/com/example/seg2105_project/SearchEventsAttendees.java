package com.example.seg2105_project;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.content.SharedPreferences;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class SearchEventsAttendees extends AppCompatActivity {

    private ListView eventListView;
    private List<Event> eventList;
    private DatabaseHelper dbHelper;
    private TextView noEventText;
    private EditText keyword;
    private String keywordString;
    private Button searchKeywordButton;
    private int attendeeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_events_attendees);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        eventListView = findViewById(R.id.listOfTheCorrespondingEvents);
        eventList = new ArrayList<>();
        noEventText = findViewById(R.id.noEventText);
        keyword = findViewById(R.id.keyword);
        searchKeywordButton = findViewById(R.id.searchKeywordButton);


        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        searchKeywordButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                keywordString = keyword.getText().toString().trim();
                eventList.clear();
                // Load corresponding events into eventList
                loadCorrespondingEvents();
            }
        });

    }


    private void loadCorrespondingEvents() {
        //int attendeeId = getAttendeeId();
        //Log.d("EventSearch", "Attendee ID: " + attendeeId);
        //Log.d("EventSearch", "Keyword: " + keywordString);
        //try {
            // add the corresponding future events to the eventList
            Cursor cursor = dbHelper.getUpcomingEvents();

            if (cursor.moveToFirst()) {
                do {
                    int event_idIndex = cursor.getColumnIndex("event_id");
                    long event_idLong = cursor.getLong(event_idIndex);
                    int event_id = (int) event_idLong;
                    // Log event ID
                    Log.d("EventSearch", "Checking event ID: " + event_id);
                    // Check if the event is registered
                    //boolean isRegistered = dbHelper.isEventRegistered(event_id, attendeeId);
                    //if (isRegistered) {
                        //continue; // Skip this event
                    //}


                    String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                    //Log.d("EventSearch", "Event title: " + title);
                    //Log.d("EventSearch", "Event description: " + description);
                    // Vérifie si le keyword se trouve dans le titre ou la description de l'événement, si oui l'événement est ajouté à la liste
                    // vérifie que le keyword n'est pas vide
                    if (keywordString != null && (title.contains(keywordString) || description.contains(keywordString))) {
                        noEventText.setVisibility(View.INVISIBLE);  // attend qu'au moins un événement soit trouvé pour rendre invisible le texte indiquant qu'aucun événement ne correspond à la recherche
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

                        Event event = new Event(event_id, title, description, date, start_time, end_time, event_address, isManualApproval, attendees);
                        eventList.add(event);
                    }


                } while (cursor.moveToNext());
            } else {


                noEventText.setVisibility(View.VISIBLE);
            }

            // Close the cursor when done
            cursor.close();

            // Create the adapter, and set it with the events
            SearchEventAdapter adapter = new SearchEventAdapter(this, eventList);
            eventListView.setAdapter(adapter);
        //} catch (Exception e) {


            //{
                //Log.e("EventSearch", "Error loading events: " + e.getMessage());
                //e.printStackTrace();
            //}


        }


    }

    //private int getAttendeeId() {
        // Example: retrieve from Intent
        //return getIntent().getIntExtra("attendee_id", -1);
    //}







