package com.example.seg2105_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RejectedRegistrationRequestOverview extends AppCompatActivity {

    private TextView rejectedRegistrationRequestsList;
    private Button returnToWelcomePageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration_request_overview);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rejectedRegistrationRequestsList = findViewById(R.id.rejectedRegistrationRequestsList);
        returnToWelcomePageButton = findViewById(R.id.returnToWelcomePageButton);

        String userTypeString=getIntent().getStringExtra("UserType");

        returnToWelcomePageButton.setOnClickListener(view -> {
            Intent intent = new Intent(RejectedRegistrationRequestOverview.this, WelcomePage.class);
            intent.putExtra("UserType", userTypeString);
            startActivity(intent);
        });

    }
}