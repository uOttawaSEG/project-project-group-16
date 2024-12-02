package com.example.seg2105_project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashSet;
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

        attendeeID = getIntent().getIntExtra("user_id", -1);


        eventListView = findViewById(R.id.listOfTheCorrespondingEvents);
        eventList = new ArrayList<>();
        noEventText = findViewById(R.id.noEventText);
        keyword = findViewById(R.id.keyword);
        searchKeywordButton = findViewById(R.id.searchKeywordButton);

        dbHelper = new DatabaseHelper(this);

        searchKeywordButton.setOnClickListener(view -> {
            keywordString = keyword.getText().toString().trim();
            loadCorrespondingEvents();
        });

        loadCorrespondingEvents();
    }

    private void loadCorrespondingEvents() {
        eventList.clear();

        HashSet<Integer> registeredEventIds = dbHelper.getRegisteredEventIds(attendeeID);
        Cursor cursor = dbHelper.getUpcomingEvents();

        if (cursor.moveToFirst()) {
            do {
                int event_id = cursor.getInt(cursor.getColumnIndexOrThrow("event_id"));

                // Exclut les événements enregistrés
                if (registeredEventIds.contains(event_id)) {
                    continue;
                }
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                if (keywordString != null && (title.contains(keywordString) || description.contains(keywordString))){
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                    String start_time = cursor.getString(cursor.getColumnIndexOrThrow("start_time"));
                    String end_time = cursor.getString(cursor.getColumnIndexOrThrow("end_time"));
                    String event_address = cursor.getString(cursor.getColumnIndexOrThrow("event_address"));
                    boolean isManualApproval = cursor.getInt(cursor.getColumnIndexOrThrow("isManualApproval")) == 1;

                    Event event = new Event(event_id, title, description, date, start_time, end_time, event_address, isManualApproval, new ArrayList<>());
                    eventList.add(event);
                }
            } while (cursor.moveToNext());
        } else {
            noEventText.setVisibility(View.VISIBLE);
        }

        cursor.close();

        SearchEventAdapter adapter = new SearchEventAdapter(this, eventList);
        eventListView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            boolean refreshList = data.getBooleanExtra("refresh_list", false);
            if (refreshList) {
                loadCorrespondingEvents(); // Recharge les événements
            }
        }
    }
}
