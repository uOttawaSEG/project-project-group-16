package com.example.seg2105_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class WelcomePage extends AppCompatActivity {
    private TextView welcomeMessage;
    private Button logOffButton;
    private ArrayList<String> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome_page);

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find views
        welcomeMessage = findViewById(R.id.WelcomeMessage);
        logOffButton = findViewById(R.id.logOffButton);

        // Retrieve user data
       // userData = getIntent().getStringArrayListExtra("userData");
        String userTypeString=getIntent().getStringExtra("UserType");

        // Ensure userData is not null before using it
        if (userTypeString!=null) {

            welcomeMessage.setText("Welcome " + userTypeString + "!");
        }

        // Handle log off button click
        logOffButton.setOnClickListener(view -> {
            Intent intent = new Intent(WelcomePage.this, MainActivity.class);
            Toast.makeText(WelcomePage.this,"You have logged-off",Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();  // Close the current activity
        });
    }
}
















