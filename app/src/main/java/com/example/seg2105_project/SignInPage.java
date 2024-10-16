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

import android.text.TextUtils;
import android.util.Patterns;

public class SignInPage extends AppCompatActivity {

    private Button signInAttendeeButton;
    private Button signInOrganizerButton;

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



        signInAttendeeButton = findViewById(R.id.signInAttendeeButton);

        signInAttendeeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(SignInPage.this, SigninAttendee.class);
                intent.putExtra("UserType","attendee");
                startActivity(intent);
            }
        });




        signInOrganizerButton = findViewById(R.id.signInOrganizerButton);

        signInOrganizerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(SignInPage.this, SignInOrganizer.class);
                intent.putExtra("UserType","organizer");
                startActivity(intent);
            }
        });

    }
}