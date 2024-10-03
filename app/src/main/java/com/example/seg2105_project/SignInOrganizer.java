package com.example.seg2105_project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class SignInOrganizer extends AppCompatActivity {

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
        setContentView(R.layout.activity_sign_in_organizer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        firstName = findViewById(R.id.firstNameFieldOrganizer);
        lastName = findViewById(R.id.lastNameFieldOrganizer);
        emailAddress = findViewById(R.id.emailAddressFieldOrganizer);
        phoneNumber = findViewById(R.id.phoneNumberOrganizer);
        address = findViewById(R.id.addressFieldOrganizer);
        password = findViewById(R.id.confirmPasswordOrganizer);


        submitButton = findViewById(R.id.submitOrganizerButton);

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