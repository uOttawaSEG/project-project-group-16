package com.example.seg2105_project;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class SigninAttendee extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText emailAddress;
    private EditText phoneNumber;
    private EditText address;
    private EditText password;

    private Button submitButton;

    private ArrayList userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signin_attendee);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        firstName = findViewById(R.id.firstNameFieldAttendee);
        lastName = findViewById(R.id.lastNameFieldAttendee);
        emailAddress = findViewById(R.id.emailAddressFieldAttendee);
        phoneNumber = findViewById(R.id.phoneNumberAttendee);
        address = findViewById(R.id.addressFieldAttendee);
        password = findViewById(R.id.confirmPasswordAttendee);


        submitButton = findViewById(R.id.submitAttendeeButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                userData = new ArrayList(6);
                userData.add(emailAddress);
                userData.add(password);
                userData.add(firstName);
                userData.add(lastName);
                userData.add(phoneNumber);
                userData.add(address);
            }
        });

    }
}