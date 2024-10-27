package com.example.seg2105_project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class RejectedRegistrationRequestOverview extends AppCompatActivity {

    private TextView rejectedRegistrationRequestsList;
    private Button returnToWelcomePageButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rejected_registration_request_overview);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rejectedRegistrationRequestsList = findViewById(R.id.rejectedRegistrationRequestsList);
        returnToWelcomePageButton = findViewById(R.id.returnToWelcomePageButton);

        databaseHelper = new DatabaseHelper(this);

        loadRejectedRequests();

        String userTypeString=getIntent().getStringExtra("UserType");

        // Set a loading message while requests are being loaded
        //rejectedRegistrationRequestsList.setText("Loading rejected registration requests...");

        returnToWelcomePageButton.setOnClickListener(view -> {
            Intent intent = new Intent(RejectedRegistrationRequestOverview.this, WelcomePage.class);
            intent.putExtra("UserType", userTypeString);
            startActivity(intent);
        });

    }
    private void loadRejectedRequests() {
        Cursor cursor = databaseHelper.getRejectedRegistrationRequests();

        if (cursor != null && cursor.getCount() > 0) {
            StringBuilder rejectedRequests = new StringBuilder();
            while (cursor.moveToNext()) {
                // Ensure column indices are valid
                int firstNameIndex = cursor.getColumnIndex("first_name");
                int lastNameIndex = cursor.getColumnIndex("last_name");
                int emailIndex = cursor.getColumnIndex("email");
                int organizationIndex = cursor.getColumnIndex("organization_name");

                // If any column is missing, skip this row to avoid crashes
                if (firstNameIndex == -1 || lastNameIndex == -1 || emailIndex == -1 || organizationIndex == -1) {
                    continue;
                }

                String firstName = cursor.getString(firstNameIndex);
                String lastName = cursor.getString(lastNameIndex);
                String email = cursor.getString(emailIndex);
                String organization = cursor.getString(organizationIndex);

                rejectedRequests.append("Name: ").append(firstName).append(" ").append(lastName).append("\n")
                        .append("Email: ").append(email).append("\n")
                        .append("Organization: ").append(organization).append("\n\n");
            }
            rejectedRegistrationRequestsList.setText(rejectedRequests.toString());
        } else {
            rejectedRegistrationRequestsList.setText("No rejected registration requests found.");
        }

        if (cursor != null) {
            cursor.close();
        }
    }
}