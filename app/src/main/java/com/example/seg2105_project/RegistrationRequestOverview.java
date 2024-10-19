package com.example.seg2105_project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegistrationRequestOverview extends AppCompatActivity {

    private TextView registrationRequestsList;
    private Button returnToWelcomePageButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration_request_overview);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });


        registrationRequestsList = findViewById(R.id.registrationRequestsList);
        returnToWelcomePageButton = findViewById(R.id.returnToWelcomePageButton);

        // Set a loading message while requests are being loaded
        registrationRequestsList.setText("Loading registration requests...");
        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Load and display pending registration requests
        loadPendingRequests();


        String userTypeString=getIntent().getStringExtra("UserType");

        returnToWelcomePageButton.setOnClickListener(view -> {
            Intent intent = new Intent(RegistrationRequestOverview.this, WelcomePage.class);
            intent.putExtra("UserType", userTypeString);
            startActivity(intent);
        });


    }
    // Method to load and display pending registration requests
    private void loadPendingRequests() {
        Cursor cursor = dbHelper.getPendingRegistrationRequests();
        StringBuilder requests = new StringBuilder();

        if (cursor.moveToFirst()) {
            do {
                String fullName = cursor.getString(cursor.getColumnIndex("first_name")) + " " +
                        cursor.getString(cursor.getColumnIndex("last_name"));
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String role = cursor.getString(cursor.getColumnIndex("user_role"));

                // Append user information to the StringBuilder
                requests.append("Name: ").append(fullName).append("\n");
                requests.append("Email: ").append(email).append("\n");
                requests.append("Role: ").append(role).append("\n\n");
            } while (cursor.moveToNext());
        } else {
            // No pending requests
            requests.append(" There are no pending registration requests.");
        }

        // Display the requests in the TextView
        registrationRequestsList.setText(requests.toString());

        // Close the cursor when done
        cursor.close();
    }

}