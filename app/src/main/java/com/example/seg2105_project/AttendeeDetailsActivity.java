package com.example.seg2105_project;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.TextView;
import android.widget.Button;

public class AttendeeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_attendee_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView fullNameTextView = findViewById(R.id.fullName);
        TextView emailTextView = findViewById(R.id.email);
        TextView phoneNumberTextView = findViewById(R.id.phoneNumber);
        Button backButton = findViewById(R.id.backButton);

        // Get attendee details from Intent
        String firstName = getIntent().getStringExtra("firstName");
        String lastName = getIntent().getStringExtra("lastName");
        String email = getIntent().getStringExtra("email");
        String phoneNumber = getIntent().getStringExtra("phoneNumber");

        //field validation
        // Field validation with defaults
        firstName = (firstName != null) ? firstName : "N/A";
        lastName = (lastName != null) ? lastName : "N/A";
        email = (email != null) ? email : "N/A";
        phoneNumber = (phoneNumber != null) ? phoneNumber : "N/A";

        fullNameTextView.setText("Name: " + firstName + " " + lastName);
        emailTextView.setText("Email: " + email);
        phoneNumberTextView.setText("Phone Number: " + phoneNumber);

        backButton.setOnClickListener(v -> {
            finish(); // Close this activity and return to the previous one
        });
    }
}