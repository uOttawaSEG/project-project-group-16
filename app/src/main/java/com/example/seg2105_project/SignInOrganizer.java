package com.example.seg2105_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.ArrayList;


public class SignInOrganizer extends AppCompatActivity {

    private ArrayList<String> userData;
    private Button submitOrganizerButton;
    private EditText firstName, lastName, emailAddress, phoneNumber, address, password, confirmPassword, organizationName;

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

        //initialize fields
        firstName = findViewById(R.id.firstNameFieldOrganizer);
        lastName = findViewById(R.id.lastNameFieldOrganizer);
        emailAddress = findViewById(R.id.emailAddressFieldOrganizer);
        phoneNumber = findViewById(R.id.phoneNumberOrganizer);
        address = findViewById(R.id.addressFieldOrganizer);
        password = findViewById(R.id.createPasswordOrganizer);
        confirmPassword = findViewById(R.id.confirmPasswordOrganizer);
        organizationName = findViewById(R.id.organizationName);
        submitOrganizerButton = findViewById(R.id.submitOrganizerButton);

        submitOrganizerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                registerOrganizer();
            }
        });
    }

    public void registerOrganizer(){

        //Receive input values

        String firstNameString = firstName.getText().toString().trim();
        String lastNameString = lastName.getText().toString().trim();
        String emailAddressString = emailAddress.getText().toString().trim();
        String phoneNumberString = phoneNumber.getText().toString().trim();String addressString = address.getText().toString().trim();
        String passwordString = password.getText().toString().trim();
        String confirmPasswordString = confirmPassword.getText().toString().trim();
        String organizationNameString = organizationName.getText().toString().trim();

        // Field Validation
        if (TextUtils.isEmpty(firstNameString) || firstNameString.length() < 2 || !firstNameString.matches("[a-zA-Z]+")) {
            firstName.setError("Enter a proper First Name");

        }

        if (TextUtils.isEmpty(lastNameString) || lastNameString.length() < 2 || !lastNameString.matches("[a-zA-Z ]+")) {
            lastName.setError("Enter a proper Last Name");

        }

        if (TextUtils.isEmpty(emailAddressString) || !Patterns.EMAIL_ADDRESS.matcher(emailAddressString).matches()) {
            emailAddress.setError("Enter a valid email");

        }

        if (TextUtils.isEmpty(phoneNumberString)) {
            phoneNumber.setError("Phone Number is required");

        }
        if (phoneNumberString.length() !=10){
            phoneNumber.setError("Phone number must be 10 digits");

        }

        if (!Patterns.PHONE.matcher(phoneNumberString).matches()) {
            phoneNumber.setError("Enter a valid phone number");

        }

        if (TextUtils.isEmpty(addressString)) {
            address.setError("Address is required");

        }

        if (TextUtils.isEmpty(passwordString)) {
            password.setError("Password is required");

        }

        if (passwordString.length() < 5) {
            password.setError("Password must be at least 5 characters");

        }

        if (!passwordString.equals(confirmPasswordString)) {
            confirmPassword.setError("Passwords do not match");
            return;
        }

        if (TextUtils.isEmpty(organizationNameString)) {
            organizationName.setError("Organization Name is required");
            return;
        }


        // If all validations pass, proceed to register
        userData = new ArrayList<>(8);
        userData.add(firstNameString);
        userData.add(lastNameString);
        userData.add(emailAddressString);
        userData.add(phoneNumberString);
        userData.add(addressString);
        userData.add(passwordString);
        userData.add(organizationNameString);

        // Proceed after successful validation
        Toast.makeText(SignInOrganizer.this, "You are signed in as an Organizer", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(SignInOrganizer.this, LogInPage.class);
        intent.putExtra("UserType","Organizer");
        intent.putExtra("Email",emailAddressString);
        intent.putExtra("passWord",passwordString);
        startActivity(intent);
    }
}