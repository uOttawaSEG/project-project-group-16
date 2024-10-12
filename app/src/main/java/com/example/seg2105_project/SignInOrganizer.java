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

public class SignInOrganizer extends AppCompatActivity {

    private Button submitOrganizerButton;
    private ArrayList<String> userData;
    private EditText firstName;
    private EditText lastName;
    private EditText emailAddress;
    private EditText phoneNumber;
    private EditText address;
    private EditText password;
    private EditText organizationName;


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

        submitOrganizerButton = findViewById(R.id.submitOrganizerButton);
        firstName=findViewById(R.id.firstNameFieldOrganizer);
        lastName=findViewById(R.id.lastNameFieldOrganizer);
        emailAddress=findViewById(R.id.emailAddressFieldOrganizer);
        phoneNumber=findViewById(R.id.phoneNumberOrganizer);
        address=findViewById(R.id.addressFieldOrganizer);
        password=findViewById(R.id.confirmPasswordOrganizer);
        organizationName=findViewById(R.id.organizationName);



        submitOrganizerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                registerOrganizer();
            }
        });

    }

    public void registerOrganizer(){
        userData=new ArrayList<>(7);
        userData.add(firstName.getText().toString());
        userData.add(lastName.getText().toString());
        userData.add(emailAddress.getText().toString());
        userData.add(phoneNumber.getText().toString());
        userData.add(address.getText().toString());
        userData.add(password.getText().toString());
        userData.add(organizationName.getText().toString());
        Toast.makeText(SignInOrganizer.this,"You are signed in ",Toast.LENGTH_LONG).show();
        Intent intent=new Intent(SignInOrganizer.this,LogInPage.class);
        intent.putExtra("UserType","Organizer");
        intent.putExtra("Email",emailAddress.getText().toString());
        intent.putExtra("passWord",password.getText().toString());
        startActivity(intent);
    }
}

