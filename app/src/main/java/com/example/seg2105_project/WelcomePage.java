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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        welcomeMessage =findViewById(R.id.WelcomeMessage);
        logOffButton=findViewById(R.id.logOffButton);

        userData=getIntent().getStringArrayListExtra("userData");
        String firstName= userData.get(0);
        if(userData!= null && !userData.isEmpty()){
            welcomeMessage.setText("Hello, "+ firstName+" !");
        }
        logOffButton.setOnClickListener(view -> {
            Intent intent= new Intent(WelcomePage.this,MainActivity.class);
            startActivity(intent);
            finish();
        });












    }
}