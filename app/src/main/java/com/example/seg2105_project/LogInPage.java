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

public class LogInPage extends AppCompatActivity {

    private Button submitLogInButton;

    private EditText emailAddress;
    private EditText password;

    private final String adminEmail="admin@uottawa.ca";
    private final String adminpassword="12345";

    private boolean loggedIn;

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

        submitLogInButton = findViewById(R.id.submitLogInButton);

        submitLogInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                emailAddress = findViewById(R.id.emailAddressField);
                password = findViewById(R.id.logInPassword);

                String emailAddressString = emailAddress.getText().toString();
                String passwordString = password.getText().toString();

                loggedIn=false;
                while(!loggedIn) {
                    if (emailAddressString.equals(adminEmail) && passwordString.equals(adminpassword)) {
                        Intent intent = new Intent(LogInPage.this, WelcomePage.class);
                        intent.putExtra("UserType", "Administrator");
                        loggedIn=true;
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(LogInPage.this, WelcomePage.class);
                        String userType = getIntent().getStringExtra("UserType");
                        intent.putExtra("UserType", userType);
                        String emailUser = getIntent().getStringExtra("Email");
                        String passWordUser = getIntent().getStringExtra("passWord");
                        if (emailAddressString.equals(emailUser) && passWordUser.equals(passwordString)) {
                            Toast.makeText(LogInPage.this, "Logged in Successfully", Toast.LENGTH_LONG).show();
                            loggedIn=true;
                            startActivity(intent);
                        } else {
                            Toast.makeText(LogInPage.this, "failed to Log", Toast.LENGTH_LONG).show();
                        }


                    }
                }
            }
        });


    }
}
