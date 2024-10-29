package com.example.seg2105_project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RejectedRegistrationRequestOverview extends AppCompatActivity {

    private TextView rejectedRegistrationRequestsList;
    private Button returnToWelcomePageButton;
    private Button approveButton;
    private DatabaseHelper databaseHelper;
    private String currentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rejected_registration_request_overview);
        if (!rejectedRegistrationRequestsList.getText().toString().isEmpty()) {
              approveButton.setEnabled(true); // Temporary test
        } else {
            approveButton.setEnabled(false);
        }

        // Apply window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        // Initialize UI components
        rejectedRegistrationRequestsList = findViewById(R.id.rejectedRegistrationRequestsList);
        returnToWelcomePageButton = findViewById(R.id.returnToWelcomePageButton);
        approveButton = findViewById(R.id.approveButton);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Load rejected requests
        loadRejectedRequests();

        // Get user type from intent
        String userTypeString = getIntent().getStringExtra("UserType");

        // Set up return button to go back to the Welcome Page
        returnToWelcomePageButton.setOnClickListener(view -> {
            Intent intent = new Intent(RejectedRegistrationRequestOverview.this, WelcomePage.class);
            intent.putExtra("UserType", userTypeString);
            startActivity(intent);
        });

        // Initially disable approve button until a request is selected
        approveButton.setEnabled(false);

        // Set up click listener for approve button
        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (databaseHelper.approveRegistrationRequest(currentEmail)) {
                    Toast.makeText(RejectedRegistrationRequestOverview.this, "Request approved", Toast.LENGTH_SHORT).show();
                    loadRejectedRequests(); // Reload the list after approving a request
                } else {
                    Toast.makeText(RejectedRegistrationRequestOverview.this, "Failed to approve", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to load and display rejected registration requests
    private void loadRejectedRequests() {
        Cursor cursor = databaseHelper.getRejectedRegistrationRequests(); // Custom method to fetch rejected requests
        StringBuilder requests = new StringBuilder();

        if (cursor.moveToFirst()) {
            do {
                String fullName = cursor.getString(cursor.getColumnIndexOrThrow("first_name")) + " " +
                        cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String role = cursor.getString(cursor.getColumnIndexOrThrow("user_role"));

                currentEmail = email; // Store the email of the current user

                requests.append("Name: ").append(fullName).append("\n");
                requests.append("Email: ").append(email).append("\n");
                requests.append("Role: ").append(role).append("\n\n");

                // Log the data to confirm it's being retrieved
                Log.d("RejectedRequests", "Loaded request for: " + email);

            } while (cursor.moveToNext());

            // Enable approve button if there are rejected requests
            approveButton.setEnabled(true);
        } else {
            // No rejected requests found
            requests.append("There are no rejected registration requests.");
            approveButton.setEnabled(false);
            Log.d("RejectedRequests", "No rejected registration requests found.");
        }

        rejectedRegistrationRequestsList.setText(requests.toString()); // Display requests

        cursor.close(); // Close cursor when done
    }
}

