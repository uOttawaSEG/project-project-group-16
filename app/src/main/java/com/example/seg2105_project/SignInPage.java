package com.example.seg2105_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignInPage extends AppCompatActivity {


    private Button signInasAnOrganizerButton;
    private Button signInasAnattendeeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //sign in as an Organizer Button

        signInasAnOrganizerButton = findViewById(R.id.signInasOrganizerButton);

        signInasAnOrganizerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(SignInPage.this, SignInOrganizer.class);
                startActivity(intent);
            }
        });

        //sign in as an Attendee Button

        signInasAnattendeeButton = findViewById(R.id.signInasAnattendeeButton);

        signInasAnattendeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(SignInPage.this, SigninAttendee.class);
                startActivity(intent);
            }
        });


    }
}