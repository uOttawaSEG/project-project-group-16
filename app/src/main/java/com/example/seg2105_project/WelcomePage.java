package com.example.seg2105_project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WelcomePage extends AppCompatActivity {
    private Button LogOffButton;
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
        logOffButton=findViewById(R.id.logOffButton);
        logOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                //Redirect to the LogIn page
                Intent intent = new Intent(WelcomePage.this, MainActivity.class);

            }
        }
    }
}