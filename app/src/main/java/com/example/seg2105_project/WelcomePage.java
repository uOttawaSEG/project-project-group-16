package com.example.seg2105_project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;


public class WelcomePage extends AppCompatActivity {
    private TextView welcomeMessage;
    private Button logOffButton;
    private Button viewRegistrationRequestsButton;
    private Button viewRejectedRegistrationRequestsButton;
    private Button viewcreateEventButton;
    private Button viewUpcomingEvents;
    private Button viewPastEvents;
    private Button searchForEventsButton;
    private Button viewRegistredEventsButton;
    private DatabaseHelper db;

    private String userTypeString;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome_page);

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        welcomeMessage = findViewById(R.id.WelcomeMessage);
        logOffButton = findViewById(R.id.logOffButton);
        viewRegistrationRequestsButton = findViewById(R.id.viewRegistrationRequestsButton);
        viewRejectedRegistrationRequestsButton = findViewById(R.id.viewRejectedRegistrationRequestsButton);
        viewcreateEventButton = findViewById(R.id.createEventButton);
        viewUpcomingEvents = findViewById(R.id.viewUpcomingEvents);
        viewPastEvents = findViewById(R.id.viewPastEvents);
        searchForEventsButton = findViewById(R.id.searchForEventsButton);
        viewRegistredEventsButton=findViewById(R.id.viewregistredeventsBtn);

        // Retrieve the userData passed from another activity

        // Retrieve user data
       // userData = getIntent().getStringArrayListExtra("userData");
        userTypeString=getIntent().getStringExtra("UserType");

        // Ensure userData is not null before using it
        if (userTypeString!=null) {

            welcomeMessage.setText("Welcome " + userTypeString + "!");

            if (userTypeString.equals("Administrator")) {
                viewRegistrationRequestsButton.setVisibility(View.VISIBLE);
                viewRegistrationRequestsButton.setEnabled(true);
                viewRejectedRegistrationRequestsButton.setVisibility(View.VISIBLE);
                viewRejectedRegistrationRequestsButton.setEnabled(true);
                viewcreateEventButton.setVisibility(View.GONE);
            }

            if(userTypeString.equals("Organizer")){

                viewcreateEventButton.setVisibility(View.VISIBLE);
                viewcreateEventButton.setEnabled(true);

                viewUpcomingEvents.setVisibility(View.VISIBLE);
                viewUpcomingEvents.setEnabled(true);

                viewPastEvents.setVisibility(View.VISIBLE);
                viewPastEvents.setEnabled(true);
            }

            if(userTypeString.equals("Attendee")){

                searchForEventsButton.setVisibility(View.VISIBLE);
                searchForEventsButton.setEnabled(true);
                viewRegistredEventsButton.setVisibility(View.VISIBLE);
                viewRegistredEventsButton.setEnabled(true);

            }


        }


        // Handle log off button click
        logOffButton.setOnClickListener(view -> {
            Intent intent = new Intent(WelcomePage.this, MainActivity.class);
            Toast.makeText(WelcomePage.this,"You have logged off",Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();  // Close the current activity
        });


        // Handle viewRegistrationRequestsButton click
        viewRegistrationRequestsButton.setOnClickListener(view -> {
            Intent intent = new Intent(WelcomePage.this, RegistrationRequestOverview.class);
            intent.putExtra("UserType", userTypeString);
            startActivity(intent);
        });


        // Handle viewRejectedRegistrationRequestsButton click
        viewRejectedRegistrationRequestsButton.setOnClickListener(view -> {
            Intent intent = new Intent(WelcomePage.this, RejectedRegistrationRequestOverview.class);
            intent.putExtra("UserType", userTypeString);
            startActivity(intent);
        });


        //handle createEvents button
        viewcreateEventButton.setOnClickListener(view -> {
            Intent intent = new Intent(WelcomePage.this, CreateEvents.class);
            intent.putExtra("UserType", userTypeString);
            startActivity(intent);
        });

        // Handle viewUpcomingEvents button click
        viewUpcomingEvents.setOnClickListener(view -> {
            Intent intent = new Intent(WelcomePage.this, UpcomingEventsOverview.class);
            intent.putExtra("UserType", userTypeString);
            startActivity(intent);
        });

        // Handle viewPastEvents button click
        viewPastEvents.setOnClickListener(view -> {
            Intent intent = new Intent(WelcomePage.this, PastEventsOverview.class);
            intent.putExtra("UserType", userTypeString);
            startActivity(intent);
        });

        // Search for events (attendee)
        searchForEventsButton.setOnClickListener(view -> {
            Intent intent = new Intent(WelcomePage.this, SearchEventsAttendees.class);
            intent.putExtra("UserType", userTypeString);
            startActivity(intent);
        });

        // Handle registered Events (attendees)
        viewRegistredEventsButton.setOnClickListener(view -> {
            Intent intent = new Intent(WelcomePage.this, ViewRegistratedEvents.class);
            intent.putExtra("UserType", userTypeString);
            startActivity(intent);
        });

    }
}

















