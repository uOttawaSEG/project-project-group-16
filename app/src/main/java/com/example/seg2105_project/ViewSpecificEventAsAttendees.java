package com.example.seg2105_project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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
    private int userId;
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

        userId = getIntent().getIntExtra("user_id", -1);
        event_id = getIntent().getIntExtra("event_id", -1);



        dbHelper = new DatabaseHelper(this);

        eventTitle = findViewById(R.id.eventTitle);
        eventDescription = findViewById(R.id.eventDescription);
        eventDate = findViewById(R.id.eventDate);
        eventStartTime = findViewById(R.id.eventStartTime);
        eventEndTime = findViewById(R.id.eventEndTime);
        eventAddress = findViewById(R.id.eventAddress);
        registerButton = findViewById(R.id.registerButton);

        loadEventDetails();

        registerButton.setOnClickListener(v -> {
            boolean isRegistered = dbHelper.registerAttendeetoEvent(userId, event_id);

            if (isRegistered) {
                Toast.makeText(this, "Successfully registered for the event!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("refresh_list", true);
                intent.putExtra("registered_event_id", event_id); // Passe l'ID de l'événement enregistré
                setResult(RESULT_OK, intent);
                finish(); // Ferme l'activité actuelle
            } else {
                Toast.makeText(this, "You are already registered for this event.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadEventDetails() {
        Cursor cursor = dbHelper.getEvent(event_id);

        if (cursor.moveToFirst()) {
            eventTitle.setText(cursor.getString(cursor.getColumnIndexOrThrow("title")));
            eventDescription.setText(cursor.getString(cursor.getColumnIndexOrThrow("description")));
            eventDate.setText(cursor.getString(cursor.getColumnIndexOrThrow("date")));
            eventStartTime.setText(cursor.getString(cursor.getColumnIndexOrThrow("start_time")));
            eventEndTime.setText(cursor.getString(cursor.getColumnIndexOrThrow("end_time")));
            eventAddress.setText(cursor.getString(cursor.getColumnIndexOrThrow("event_address")));
        } else {
            Toast.makeText(this, "Event details not found.", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }


}
