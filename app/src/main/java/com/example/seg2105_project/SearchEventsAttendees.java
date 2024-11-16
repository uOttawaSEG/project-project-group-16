package com.example.seg2105_project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
        keywordString = keyword.getText().toString().trim();
        searchKeywordButton = findViewById(R.id.searchKeywordButton);


        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        searchKeywordButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Load corresponding events into eventList
                loadCorrespondingEvents();
            }
        });

    }


    private void loadCorrespondingEvents(){
        // add the corresponding future events to the eventList
        Cursor cursor = dbHelper.getUpcomingEvents();

        if (cursor.moveToFirst()) {
            do {
                int event_idIndex = cursor.getColumnIndex("event_id");
                long event_idLong = cursor.getLong(event_idIndex);
                int event_id = (int) event_idLong;
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                // Vérifie si le keyword se trouve dans le titre ou la description de l'événement, si oui l'événement est ajouté à la liste
                if (title.contains(keywordString) || description.contains(keywordString)){
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

                    Event event = new Event(event_id, title, description, date, start_time, end_time, event_address,isManualApproval, attendees);
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


    }

}