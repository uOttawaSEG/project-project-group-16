package com.example.seg2105_project;

import android.content.Intent;
import android.database.Cursor;
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

public class RejectedRegistrationRequestOverview extends AppCompatActivity {

    private TextView rejectedRegistrationRequestsList;
    private Button returnToWelcomePageButton;

    private Button approveButton;
    private DatabaseHelper dbHelper;
    private String currentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rejected_registration_request_overview);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rejectedRegistrationRequestsList = findViewById(R.id.rejectedRegistrationRequestsList);
        returnToWelcomePageButton = findViewById(R.id.returnToWelcomePageButton);
        approveButton=findViewById(R.id.approveButton);


        // Set a loading message while requests are being loaded
        rejectedRegistrationRequestsList.setText("Loading rejected registration requests...");
        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Load and display rejected registration requests
        loadRejectedRequests();

        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dbHelper.approveRegistrationRequest(currentEmail)){
                    Toast.makeText(RejectedRegistrationRequestOverview.this,"Request approved",Toast.LENGTH_SHORT).show();
                    loadRejectedRequests();
                }
                else{
                    Toast.makeText(RejectedRegistrationRequestOverview.this,"Failed to approve",Toast.LENGTH_SHORT).show();
                }
            }
        });

        String userTypeString=getIntent().getStringExtra("UserType");

        returnToWelcomePageButton.setOnClickListener(view -> {
            Intent intent = new Intent(RejectedRegistrationRequestOverview.this, WelcomePage.class);
            intent.putExtra("UserType", userTypeString);
            startActivity(intent);
        });

    }


    // Method to load and display pending registration requests
    private void loadRejectedRequests() {
        Cursor cursor = dbHelper.getRejectedRegistrationRequests();
        StringBuilder requests = new StringBuilder();

        if (cursor.moveToFirst()) {
            do {
                String fullName = cursor.getString(cursor.getColumnIndexOrThrow("first_name")) + " " +
                        cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String role = cursor.getString(cursor.getColumnIndexOrThrow("user_role"));

                // user we want to approve/reject
                currentEmail= email;

                // Append user information to the StringBuilder
                requests.append("Name: ").append(fullName).append("\n");
                requests.append("Email: ").append(email).append("\n");
                requests.append("Role: ").append(role).append("\n\n");


            } while (cursor.moveToNext());

            //activate buttons if a request is pending
            approveButton.setEnabled(true);

        } else {
            // No pending requests
            requests.append(" There are no pending registration requests.");
            approveButton.setEnabled(false);
        }

        // Display the requests in the TextView
        rejectedRegistrationRequestsList.setText(requests.toString());

        // Close the cursor when done
        cursor.close();
    }

}