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

import java.util.ArrayList;
import android.text.TextUtils;
import android.util.Patterns;

public class SigninAttendee extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    //private ArrayList<String> userData;

    private Button submitAttendeeButton;
    private EditText firstName;
    private EditText lastName;
    private EditText emailAddress;
    private EditText phoneNumber;
    private EditText address;
    private EditText password;
    private EditText confirmPassword;
    private String registrationStatus;

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

        //Initialize fields
        submitAttendeeButton = findViewById(R.id.submitAttendeeButton);
        firstName = findViewById(R.id.firstNameFieldAttendee);
        lastName = findViewById(R.id.lastNameFieldAttendee);
        emailAddress = findViewById(R.id.emailAddressFieldAttendee);
        phoneNumber = findViewById(R.id.phoneNumberAttendee);
        address = findViewById(R.id.addressFieldAttendee);
        password = findViewById(R.id.createPasswordAttendee);
        confirmPassword = findViewById(R.id.confirmPasswordAttendee);

        dbHelper=new DatabaseHelper(this);

        submitAttendeeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
               registerAttendee();
            }
        });
    }

    public void registerAttendee(){
        //Receive input values
        String firstNameString = firstName.getText().toString().trim();
        String lastNameString = lastName.getText().toString().trim();
        String emailAddressString = emailAddress.getText().toString().trim();
        String phoneNumberString = phoneNumber.getText().toString().trim();
        String addressString = address.getText().toString().trim();
        String passwordString = password.getText().toString().trim();
        String confirmPasswordString = confirmPassword.getText().toString().trim();
        registrationStatus = "pending";

        //Field validation
        if (TextUtils.isEmpty(firstNameString) || firstNameString.length() < 2 || !firstNameString.matches("[a-zA-Z]+")) {
            firstName.setError("Enter a proper First Name");
            return;

        }

        if (TextUtils.isEmpty(lastNameString) || lastNameString.length() < 2 || !lastNameString.matches("[a-zA-Z ]+")) {
            lastName.setError("Enter a proper Last Name");
            return;

        }

        if (TextUtils.isEmpty(emailAddressString) || !Patterns.EMAIL_ADDRESS.matcher(emailAddressString).matches()) {
            emailAddress.setError("Enter a valid Email");
            return;

        }

        if (TextUtils.isEmpty(phoneNumberString)) {
            phoneNumber.setError("Phone Number is required");
            return;

        }
        if (phoneNumberString.length() !=10){
            phoneNumber.setError("Phone number must be 10 digits");
            return;

        }

        if (!Patterns.PHONE.matcher(phoneNumberString).matches()) {
            phoneNumber.setError("Enter a valid phone number");
            return;

        }

        if (TextUtils.isEmpty(addressString)) {
            address.setError("Address is required");
            return;

        }

        if (TextUtils.isEmpty(passwordString)) {
            password.setError("Password is required");
            return;

        }

        if (passwordString.length() < 5) {
            password.setError("Password must be at least 5 characters");
            return;

        }

        if (!passwordString.equals(confirmPasswordString)) {
            confirmPassword.setError("Passwords do not match");
            return;
        }

        // If all validations pass, proceed to register
         /* userData = new ArrayList<>(7);
        userData.add(firstNameString);
        userData.add(lastNameString);
        userData.add(emailAddressString);
        userData.add(phoneNumberString);
        userData.add(addressString);
        userData.add(passwordString);

          */

        boolean insertSuccess = dbHelper.addUser(
                firstNameString,
                lastNameString,
                emailAddressString,
                passwordString,
                phoneNumberString,
                addressString,
                registrationStatus,
                null,
                "Attendee"
        );

        /* Toast.makeText(SigninAttendee.this,"You are signed in as an Attendee",Toast.LENGTH_LONG).show();
        Intent intent =new Intent(SigninAttendee.this,LogInPage.class);
        intent.putExtra("UserType","Attendee");
        intent.putExtra("Email",emailAddressString);
        intent.putExtra("passWord",passwordString);
        startActivity(intent);

         */

        if (insertSuccess) {
            Toast.makeText(SigninAttendee.this, "Registration Successful", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(SigninAttendee.this, LogInPage.class);
            intent.putExtra("UserType", "Attendee");
            intent.putExtra("Email", emailAddressString);
            intent.putExtra("passWord", passwordString);
            intent.putExtra("registrationStatus", registrationStatus);
            startActivity(intent);
        } else {
            // Show error message if there was an issue with registration
            Toast.makeText(SigninAttendee.this, "Registration Failed. Try Again.", Toast.LENGTH_LONG).show();
        }

    }
}