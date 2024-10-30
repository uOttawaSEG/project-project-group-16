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

public class RegistrationRequestOverview extends AppCompatActivity {

    private TextView registrationRequestsList;
    private Button returnToWelcomePageButton;
    private Button approveButton;
    private Button rejectButton;
    private DatabaseHelper dbHelper;
    private String currentEmail;

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
        approveButton=findViewById(R.id.approvebutton);
        rejectButton=findViewById(R.id.rejectbutton);



        // Set a loading message while requests are being loaded
        registrationRequestsList.setText("Loading registration requests...");
        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Load and display pending registration requests
        loadPendingRequests();

        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dbHelper.approveRegistrationRequest(currentEmail)){
                    Toast.makeText(RegistrationRequestOverview.this,"Request approved",Toast.LENGTH_SHORT).show();
                    loadPendingRequests();
                }
                else{
                    Toast.makeText(RegistrationRequestOverview.this,"Failed to approve",Toast.LENGTH_SHORT).show();
                }
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dbHelper.rejectRegistrationRequest(currentEmail)){
                    Toast.makeText(RegistrationRequestOverview.this,"Request rejected",Toast.LENGTH_SHORT).show();
                    loadPendingRequests();


                }
                else{
                    Toast.makeText(RegistrationRequestOverview.this,"Failed to reject",Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                String fullName = cursor.getString(cursor.getColumnIndexOrThrow("first_name")) + " " +
                        cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String role = cursor.getString(cursor.getColumnIndexOrThrow("user_role"));
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phone_number"));
                String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                String organization_name = cursor.getString(cursor.getColumnIndexOrThrow("organization_name"));

                // user we want to approve/reject
                currentEmail= email;

                // Append user information to the StringBuilder
                requests.append("Name: ").append(fullName).append("\n");
                requests.append("Email: ").append(email).append("\n");
                requests.append("Role: ").append(role).append("\n");
                requests.append("Phone number: ").append(phoneNumber).append("\n");
                requests.append("Address: ").append(address).append("\n");
                if (organization_name != null)  {   // if organizer
                    requests.append("Organization name: ").append(organization_name).append("\n");
                }
                requests.append("\n");

            } while (cursor.moveToNext());

            //activate buttons if a request is pending
            approveButton.setEnabled(true);
            rejectButton.setEnabled(true);

        } else {
            // No pending requests
            requests.append(" There are no pending registration requests.");
            approveButton.setEnabled(false);
            rejectButton.setEnabled(false);
        }

        // Display the requests in the TextView
        registrationRequestsList.setText(requests.toString());

        // Close the cursor when done
        cursor.close();
    }

}