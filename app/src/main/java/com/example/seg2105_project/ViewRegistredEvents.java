package com.example.seg2105_project;

import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ViewRegistredEvents extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private RegisteredEventsAdapter adapter;
    private List<Event> registeredEvents;
    private int event_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_registred_events);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper= new DatabaseHelper(this);
        recyclerView=findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        registeredEvents=new ArrayList<>();


        // load registered events
        loadRegisteredEvents();

        adapter = new RegisteredEventsAdapter(this, registeredEvents);
        recyclerView.setAdapter(adapter);

    }

    private void loadRegisteredEvents() {
        // ID de l'utilisateur connecté (exemple : récupérez cet ID depuis les préférences ou la session utilisateur)
        int attendeeId = 1; // Remplacez par l'ID réel

        // Utiliser la méthode pour récupérer les événements enregistrés
        Cursor cursor = dbHelper.getRegisteredEventsForAttendee(attendeeId);

        while (cursor.moveToNext()) {
            // Récupérer les colonnes depuis le curseur
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
            String startTime = cursor.getString(cursor.getColumnIndexOrThrow("start_time"));
            String endTime = cursor.getString(cursor.getColumnIndexOrThrow("end_time"));
            String address = cursor.getString(cursor.getColumnIndexOrThrow("event_address"));

            List<Attendee> attendees = new ArrayList<>();
            Integer isManualApprovalInt = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("isManualApproval")));
            boolean isManualApproval;
            if (isManualApprovalInt == 1) isManualApproval = true;
            else isManualApproval = false;

            // Ajouter l'événement à une liste (exemple : `registeredEvents`)
            registeredEvents.add(new Event(event_id, title, description,date, startTime,endTime, address,isManualApproval, attendees));
        }

        cursor.close(); // Toujours fermer le curseur après utilisation
    }
}