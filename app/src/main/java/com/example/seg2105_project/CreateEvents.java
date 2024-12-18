package com.example.seg2105_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateEvents extends AppCompatActivity {

    private DatabaseHelper db;
    private Button submitEventButton, deleteEventButton;
    private EditText title, description, date, start_time, end_time, event_address;
    private String organizerId;
    private String eventState;
    private Spinner approvalModeSpinner;
    private String userTypeString;

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
        approvalModeSpinner = findViewById(R.id.approvalModeSpinner);

        // Populate Spinner with options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.approval_modes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        approvalModeSpinner.setAdapter(adapter);

        userTypeString = getIntent().getStringExtra("UserType");

        db=new DatabaseHelper(this);

        submitEventButton.setOnClickListener(view -> registerEvent());
    }

    public void registerEvent(){

        //input values
        String titleString=title.getText().toString().trim();
        String descriptionString=description.getText().toString().trim();
        String dateString=date.getText().toString().trim();
        String startTimeString=start_time.getText().toString().trim();
        String endTimeString=end_time.getText().toString().trim();
        String eventAddressString=event_address.getText().toString().trim();
        // Get selected approval mode
        String selectedMode = approvalModeSpinner.getSelectedItem().toString();
        Log.d("Spinner Debug", "Selected mode from spinner: " + selectedMode);
        boolean isManualApproval = selectedMode.equals("Manual Approval");
        Log.d("Spinner Debug", "isManualApproval resolved to: " + isManualApproval);
        //field validation

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

        if (!isFutureDate(dateString)){
            Toast.makeText(this, "Please select a valid date. ", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isValidDate(dateString)){
            Toast.makeText(this, "Invalid Date format", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isStartBeforeEnd(startTimeString,endTimeString)){
            Toast.makeText(this, "End time must be after start time", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidTime(startTimeString)){
            Toast.makeText(this, "Invalid start time format", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!is30minIncrement(startTimeString)){
            Toast.makeText(this, "The start time must be selected in 30-minute increments", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!is30minIncrement(endTimeString)){
            Toast.makeText(this, "The end time must be selected in 30-minute increments", Toast.LENGTH_SHORT).show();
            return;
        }




        // Retrieve current user email and fetch organizerId
        SharedPreferences sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        String currentEmail=sharedPreferences.getString("email", null);
//        int organizerId= db.getUserId(currentEmail);
//
//        // Check for event conflicts before registering
//        if (db.checkEventConflict(organizerId, startTimeString, endTimeString, dateString)) {
//            Toast.makeText(this, "You have a conflict with another event. Please select a different time.", Toast.LENGTH_SHORT).show();
//            return;
//        }
int organizerId = 1;
        Log.d("Step 1", "isManualApproval passed: " + isManualApproval);


        long event_id=db.addEvent(
                titleString,
                descriptionString,
                dateString,
                startTimeString,
                endTimeString,
                eventAddressString,
                organizerId,
                isManualApproval//true  Default to manual approval mode
        );
        if(event_id != -1){

            Toast.makeText(CreateEvents.this, "Event Creation Successful", Toast.LENGTH_LONG).show();
            // Verify event details
            Cursor cursor = db.getEvent((int) event_id); // Fetch event details
            if (cursor != null && cursor.moveToFirst()) {
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                int manualApprovalValue = cursor.getInt(cursor.getColumnIndexOrThrow("isManualApproval"));
                Log.d("Event Verification", "Event Title: " + title + ", isManualApproval: " + isManualApproval);
            } else {
                Log.e("Event Verification", "Event not found in database.");
            }
            if (cursor != null) {
                cursor.close();
            }
            Intent intent= new Intent(CreateEvents.this, WelcomePage.class);
            intent.putExtra("UserType", userTypeString);
            startActivity(intent);
        }
        else{
            Toast.makeText(CreateEvents.this, "Event Creation Failed. Try Again.", Toast.LENGTH_LONG).show();
        }

    }


    // Field Validation Methods


    private boolean isFutureDate(String date){
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


    private boolean is30minIncrement(String time) {
        SimpleDateFormat timeFormat= new SimpleDateFormat("HH:mm", Locale.getDefault());
        timeFormat.setLenient(false);

        try{
            Date parsedTime = timeFormat.parse(time);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parsedTime);

            int minutes = calendar.get(Calendar.MINUTE);

            return (minutes == 0 || minutes == 30);
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