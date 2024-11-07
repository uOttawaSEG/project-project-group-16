package com.example.seg2105_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class CreateEvents extends AppCompatActivity {

    private DatabaseHelper db;
    private Button submitEventButton;
    private EditText title, description, date, start_time, end_time, event_address;
    private String organizerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_events);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        submitEventButton= findViewById(R.id.submitEventButton);
        title=findViewById(R.id.titleField);
        description=findViewById(R.id.descriptionField);
        date=findViewById(R.id.dateField);
        start_time=findViewById(R.id.startTimeField);
        end_time=findViewById(R.id.endTimeField);
        event_address=findViewById(R.id.eventAddressField);


        db=new DatabaseHelper(this);


        submitEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerEvent();
            }
        });
    }

    public void registerEvent(){

        //input values
        String titleString=title.getText().toString().trim();;
        String descriptionString=description.getText().toString().trim();
        String dateString=date.getText().toString().trim();
        String startTimeString=start_time.getText().toString().trim();
        String endTimeString=end_time.getText().toString().trim();
        String eventAddressString=event_address.getText().toString().trim();



        //field validation




        //field validation done

        SharedPreferences sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        String currentEmail=sharedPreferences.getString("email", null);
        int organizerId= db.getUserId(currentEmail);

        boolean insertSuccess=db.addEvent(
                titleString,
                descriptionString,
                dateString,
                startTimeString,
                endTimeString,
                eventAddressString,
                organizerId

        );
        if(insertSuccess){

            Toast.makeText(CreateEvents.this, "Event Creation Successful", Toast.LENGTH_LONG).show();
            Intent intent= new Intent(CreateEvents.this, WelcomePage.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(CreateEvents.this, "Event Creation Failed. Try Again.", Toast.LENGTH_LONG).show();
        }

    }
}