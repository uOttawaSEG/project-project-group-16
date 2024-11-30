package com.example.seg2105_project;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.widget.TextView;
import android.database.Cursor;
import android.widget.Toast;
import android.content.Intent;
import android.widget.LinearLayout;
import  android.widget.Button;

public class AttendeeRequestOverview extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private int eventId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_attendee_request_overview);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        //UI element

        dbHelper=new DatabaseHelper(this);
        eventId = getIntent().getIntExtra("event_id", -1);
        if(eventId != -1){
            String eventIdString = String.valueOf(eventId);
            loadAttendeeRequests(eventIdString);
        }
        else{
            Toast.makeText(this,"No event Id provided",Toast.LENGTH_SHORT).show();
        }

    }
    //method to view or load attendee request for an event
    private void loadAttendeeRequests(String eventId){
        LinearLayout attendeeRequestsContainer = findViewById(R.id.attendeeRequestsContainer);
        attendeeRequestsContainer.removeAllViews(); // Clear previous entries

        Cursor cursor = dbHelper.getAttendeeForEvent(eventId);
        // Add debug logs to verify database results
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Log.d("Database Debug", "Attendee ID: " + cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                Log.d("Database Debug", "First Name: " + cursor.getString(cursor.getColumnIndexOrThrow("first_name")));
                Log.d("Database Debug", "Registration Status: " + cursor.getString(cursor.getColumnIndexOrThrow("registration_status")));
            } while (cursor.moveToNext());
        } else {
            Log.d("Database Debug", "No attendees found for event ID: " + eventId);
        }
        if (cursor == null) {
            Toast.makeText(this, "Error loading attendee requests.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add "Approve All" button at the top
        Button approveAllButton = new Button(this);
        approveAllButton.setText("Approve All");
        approveAllButton.setOnClickListener(v -> {
            boolean success = dbHelper.approveAllRegistrationsForEvent(Integer.parseInt(eventId));
            if (success) {
                Toast.makeText(this, "All registrations approved!", Toast.LENGTH_SHORT).show();
                loadAttendeeRequests(eventId); // Refresh the list
            } else {
                Toast.makeText(this, "No pending registrations to approve.", Toast.LENGTH_SHORT).show();
            }
        });
        attendeeRequestsContainer.addView(approveAllButton);

        if (cursor.moveToFirst()) {
            do {
                int attendeeId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phone_number"));
                String registrationStatus=cursor.getString(cursor.getColumnIndexOrThrow("registration_status"));

                // Create a layout for each attendee
                LinearLayout attendeeLayout = new LinearLayout(this);
                attendeeLayout.setOrientation(LinearLayout.VERTICAL);
                attendeeLayout.setPadding(16, 16, 16, 16);

                // Add attendee details
                TextView attendeeView = new TextView(this);
                attendeeView.setText("Name: " + firstName + " " + lastName + "\n" +
                        "Email: " + email + "\n" +
                        "Phone: " + phoneNumber);
                attendeeView.setTextSize(16);
                attendeeLayout.addView(attendeeView);


                //Add registration status indicator
                TextView statusView=new TextView(this);
                statusView.setText("Status : "+registrationStatus);//Display status
                statusView.setTextSize(14);
                statusView.setPadding(0,8,0,8);
                attendeeLayout.addView(statusView);



                // Add "View Info" button
                Button btnViewInfo = new Button(this);
                btnViewInfo.setText("View Info");
                btnViewInfo.setOnClickListener(v -> {
                    Intent intent = new Intent(this, AttendeeDetailsActivity.class);
                    intent.putExtra("firstName", firstName);
                    intent.putExtra("lastName", lastName);
                    intent.putExtra("email", email);
                    intent.putExtra("phoneNumber", phoneNumber);
                    startActivity(intent);
                });
                attendeeLayout.addView(btnViewInfo);

                // Add Approve Button
                Button btnApprove = new Button(this);
                btnApprove.setText("Approve");
                btnApprove.setOnClickListener(v -> {
                    Log.d("Approve Debug", "Attempting to approve attendee with ID: " + attendeeId + ", Event ID: " + eventId);

                    boolean success = dbHelper.updateRegistrationStatus(attendeeId, Integer.parseInt(eventId), "approved");
                    if (success) {
                        Log.d("Approve Debug", "Registration status successfully updated to 'approved'.");
                        attendeeRequestsContainer.removeView(attendeeLayout); // Remove from UI
                        Toast.makeText(this, "Registration approved!", Toast.LENGTH_SHORT).show();
                        loadAttendeeRequests(eventId);//refresh list
                    }
                   // else {
                        //Log.e("Approve Debug", "Failed to update registration status. Verify attendee and event IDs.");
                        //Toast.makeText(this, "Failed to approve registration.", Toast.LENGTH_SHORT).show();
                   // }

                });
                attendeeLayout.addView(btnApprove);

                // Add Reject Button
                Button btnReject = new Button(this);
                btnReject.setText("Reject");
                btnReject.setOnClickListener(v -> {
                    boolean success = dbHelper.updateRegistrationStatus(attendeeId, Integer.parseInt(eventId), "rejected");
                    if (success) {
                        attendeeRequestsContainer.removeView(attendeeLayout); // Remove from UI
                        Toast.makeText(this, "Registration rejected!", Toast.LENGTH_SHORT).show();
                        loadAttendeeRequests(eventId);//refresh list
                    }
                });
                attendeeLayout.addView(btnReject);

                // Add the attendee layout to the main container
                attendeeRequestsContainer.addView(attendeeLayout);

            } while (cursor.moveToNext());
        } else {
            // Display a message if no attendees are found
            TextView noAttendeesView = new TextView(this);
            noAttendeesView.setText("No attendees have requested registration for this event.");
            noAttendeesView.setPadding(16, 16, 16, 16);
            noAttendeesView.setTextSize(16);
            attendeeRequestsContainer.addView(noAttendeesView);
        }

        cursor.close();
    }
}