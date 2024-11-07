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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    if (titleString.isEmpty()){
        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
    }

        if (titleString.isEmpty()){
            Toast.makeText(this, "Title cannot be empty .", Toast.LENGTH_SHORT).show();
            return;
        }

        if (descriptionString.isEmpty()){
            Toast.makeText(this, "Description cannot be empty .", Toast.LENGTH_SHORT).show();
            return;
        }


        if (dateString.isEmpty()){
            Toast.makeText(this, "Date cannot be empty .", Toast.LENGTH_SHORT).show();
            return;
        }

        if (startTimeString.isEmpty()){
            Toast.makeText(this, "Start time cannot be empty .", Toast.LENGTH_SHORT).show();
            return;
        }

        if (endTimeString.isEmpty()){
            Toast.makeText(this, "End time cannot be empty .", Toast.LENGTH_SHORT).show();
            return;
        }
        if (eventAddressString.isEmpty()){
            Toast.makeText(this, "Event Address cannot be empty.", Toast.LENGTH_SHORT).show();
            return;

        }

        // Date and Time Verification
        if(!isValidDate(dateString)){
            Toast.makeText(this, "Invalid time format", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isStartBeforeEnd(startTimeString,endTimeString)){
            Toast.makeText(this, "End date must be after start date", Toast.LENGTH_SHORT).show();
        }

        if (isValidTime(startTimeString)){
            Toast.makeText(this, "Invalid start time format", Toast.LENGTH_SHORT).show();
            return;
        }



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


    // Field Validation Methods


    private boolean isFutureDat(String date){
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try{
            Date selectedDate=dateFormat.parse(date);
            Date currentDate= new Date(); // date at the moment
            return !selectedDate.before(currentDate);

        } catch (ParseException e) {
            return false;
        }
    }


    private boolean isValidDate(String date){
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dateFormat.setLenient(false);
        try{
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }

    }

    private boolean isValidTime(String time){
    SimpleDateFormat timeFormat= new SimpleDateFormat("HH:mm", Locale.getDefault());
    timeFormat.setLenient(false);
    try{
        timeFormat.parse(time);
        return true;
    } catch (ParseException e) {
        return false;
    }

    }


    private boolean isStartBeforeEnd(String startTime, String endTime){
    SimpleDateFormat timeFormat= new SimpleDateFormat("HH:mm", Locale.getDefault());
    try{
        Date start=timeFormat.parse(startTime);
        Date end= timeFormat.parse(endTime);
        return start.before(end);
    } catch (ParseException e) {
        return false;
    }
    }
}