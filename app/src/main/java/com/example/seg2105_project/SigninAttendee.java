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

public class SigninAttendee extends AppCompatActivity {

    private ArrayList<String> userData;
    private Button submitAttendeeButton;
    private EditText firstName;
    private EditText lastName;
    private EditText emailAddress;
    private EditText phoneNumber;
    private EditText address;
    private EditText password;

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

        submitAttendeeButton = findViewById(R.id.submitAttendeeButton);
        firstName=findViewById(R.id.firstNameFieldAttendee);
        lastName=findViewById(R.id.lastNameFieldAttendee);
        emailAddress=findViewById(R.id.emailAddressFieldAttendee);
        phoneNumber=findViewById(R.id.phoneNumberAttendee);
        address=findViewById(R.id.addressFieldAttendee);
        password=findViewById(R.id.confirmPasswordAttendee);


        submitAttendeeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
               registerAttendee();
            }
        });


    }
    public void registerAttendee(){

        userData=new ArrayList<>(7);
        userData.add(firstName.getText().toString());
        userData.add(lastName.getText().toString());
        userData.add(emailAddress.getText().toString());
        userData.add(phoneNumber.getText().toString());
        userData.add(address.getText().toString());
        userData.add(password.getText().toString());
        Toast.makeText(SigninAttendee.this,"You are signed in ",Toast.LENGTH_LONG).show();
        Intent intent =new Intent(SigninAttendee.this,LogInPage.class);
        intent.putExtra("UserType","Attendee");
        intent.putExtra("Email",emailAddress.getText().toString());
        intent.putExtra("passWord",password.getText().toString());
        startActivity(intent);





    }
}