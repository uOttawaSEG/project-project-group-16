package com.example.seg2105_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.TextView;

public class WelcomePage extends AppCompatActivity {  // Removed 'abstract'
    private TextView welcomeMessage;
    private Button logOffButton;
    private ArrayList<String> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        // Handling system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        welcomeMessage = findViewById(R.id.WelcomeMessage);
        logOffButton = findViewById(R.id.logOffButton);

        // Retrieve the userData passed from another activity
        userData = getIntent().getStringArrayListExtra("userData");
        String firstName = userData.get(0);
        String role = userData.get(1);


        if (userData != null && !userData.isEmpty()) {
            // Assuming the first element is the first name

            welcomeMessage.setText("Hello, " + role + " "+ firstName + " !");
        }

        // Set up the log off click listener
        logOffButton.setOnClickListener(v -> {
            // Redirect to MainActivity (Login screen)
            Intent intent = new Intent(WelcomePage.this, MainActivity.class);
            startActivity(intent);  // Start the MainActivity
            finish();  // Finish current activity to prevent back navigation
        });
    }
}



