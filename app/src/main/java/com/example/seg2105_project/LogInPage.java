package com.example.seg2105_project;

import android.content.Intent;
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

import android.text.TextUtils;
import android.util.Patterns;

public class LogInPage extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private Button submitLogInButton;

    private EditText emailAddress;
    private EditText password;

    private final String adminEmail = "admin@uottawa.ca";
    private final String adminpassword = "12345";

    private boolean loggedIn;
    private String registrationStatus;
    private String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize fields
        emailAddress = findViewById(R.id.emailAddressField);
        password = findViewById(R.id.logInPassword);
        submitLogInButton = findViewById(R.id.submitLogInButton);

        dbHelper = new DatabaseHelper(this);

        registrationStatus = getIntent().getStringExtra("registrationStatus");

        submitLogInButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String emailAddressString = emailAddress.getText().toString().trim();
                String passwordString = password.getText().toString().trim();

                // Field Validation
                if (TextUtils.isEmpty(emailAddressString)) {
                    emailAddress.setError("Email is required");
                    return;  // Stops execution if email is empty
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(emailAddressString).matches()) {
                    emailAddress.setError("Enter a valid email");
                    return;  // Stops execution if email format is invalid
                }

                if (TextUtils.isEmpty(passwordString)) {
                    password.setError("Password is required");
                    return;  // Stops execution if password is empty
                }

                if (passwordString.length() < 5) {
                    password.setError("Password must be at least 5 characters");
                    return;  // Stops execution if password length is less than 5
                }

                loggedIn = false;  // Variable to check if the user is connected
                // Loop until the user logs in
               if (!loggedIn) {
                    //Check admin credentials
                    if (emailAddressString.equals(adminEmail) && passwordString.equals(adminpassword)) {
                        Intent intent = new Intent(LogInPage.this, WelcomePage.class);
                        intent.putExtra("UserType", "Administrator");
                        intent.putExtra("registration_status", "approved");
                        loggedIn = true;
                        startActivity(intent);
                        finish();
                    } else {
                        userRole = dbHelper.checkUser(emailAddressString, passwordString);
                        if (userRole !=null) {

                            registrationStatus=dbHelper.getRegistrationStatus(emailAddressString);
                            // User credentials are valid, proceed to welcome page
                            if (registrationStatus.equals("pending")) {
                                Toast.makeText(LogInPage.this, "Your registration request has not been processed yet, approval is pending.", Toast.LENGTH_LONG).show();
                            } else if (registrationStatus.equals("rejected")) {
                                Toast.makeText(LogInPage.this, "Your registration request has been rejected. Please contact the administrator: (123)456-7890", Toast.LENGTH_LONG).show();
                            } else if (registrationStatus.equals("approved")){

                                int userId = dbHelper.getUserId(emailAddressString);

                                if (userId == -1) {
                                    Toast.makeText(LogInPage.this, "Failed to retrieve User ID. Please try again.", Toast.LENGTH_LONG).show();
                                    return;
                                }

                                Intent intent = new Intent(LogInPage.this, WelcomePage.class);
                                intent.putExtra("registration_status", "approved");
                                intent.putExtra("UserType", userRole); // Set appropriate user type based on your login
                                intent.putExtra("user_id", userId);
                                loggedIn = true;
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LogInPage.this, "Failed to log in. Please check your credentials.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(LogInPage.this, "Failed to log in. Please check your credentials.", Toast.LENGTH_LONG).show();
                        }
                    }
                }




            }

        });
    }
}





