package com.example.seg2105_project;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.TextView;

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

        // Get attendee details from Intent
        String firstName = getIntent().getStringExtra("firstName");
        String lastName = getIntent().getStringExtra("lastName");
        String email = getIntent().getStringExtra("email");
        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        //field validation
        if (firstName == null) firstName = "N/A";
        if (lastName == null) lastName = "N/A";
        if (email == null) email = "N/A";
        if (phoneNumber == null) phoneNumber = "N/A";

        fullNameTextView.setText("Name: " + firstName + " " + lastName);
        emailTextView.setText("Email: " + email);
        phoneNumberTextView.setText("Phone Number: " + phoneNumber);
    }
}