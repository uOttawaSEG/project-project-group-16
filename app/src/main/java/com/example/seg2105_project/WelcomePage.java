package com.example.seg2105_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.TextView;


public class WelcomePage extends AppCompatActivity {
    private TextView welcomeMessage;
    private Button logOffButton;
    private ArrayList<String> userData;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //initialize UI
        welcomeMessage=findViewById(R.id.WelcomeMessage);
        logOffButton=findViewById(R.id.logOffButton);
        // Retrieve the userData passed from Signin Attendee
        userData = getIntent().getStringArrayListExtra("userData");
        if (userData != null && !userData.isEmpty()) {

            // Use the data as needed (e.g., display the first name)
            String firstName = userData.get(0);  // Assuming the first element is firstName
            welcomeMessage.setText("Hello , " + firstName + "!");
        }
            // set up the log off click listener
            logOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Redirect to the LogIn of the MainActivity page
                Intent intent = new Intent(WelcomePage.this, MainActivity.class);
                startActivity(intent);  // Start the MainActivity
                finish();  // Finish the current activity so the user cannot go back
            }
            });
        }
    }
