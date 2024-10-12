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

<<<<<<< HEAD
=======
        submitLogInButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                // Récupération des champs email et mot de passe
                emailAddress = findViewById(R.id.emailAddressField);
                password = findViewById(R.id.logInPassword);

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

                loggedIn = false;  // Variable pour savoir si l'utilisateur est connecté

                // Boucle jusqu'à la validation de la connexion
                while (!loggedIn) {
                    // Vérification des identifiants d'administrateur
                    if (emailAddressString.equals(adminEmail) && passwordString.equals(adminpassword)) {
                        Intent intent = new Intent(LogInPage.this, WelcomePage.class);
                        intent.putExtra("UserType", "Administrator");
                        loggedIn = true;
                        startActivity(intent);
                    } else {
                        // Récupération des informations utilisateur transmises par l'intent
                        Intent intent = new Intent(LogInPage.this, WelcomePage.class);
                        String userType = getIntent().getStringExtra("UserType");
                        intent.putExtra("UserType", userType);
                        String emailUser = getIntent().getStringExtra("Email");
                        String passWordUser = getIntent().getStringExtra("passWord");

                        // Vérification des identifiants de l'utilisateur régulier
                        if (emailAddressString.equals(emailUser) && passWordUser.equals(passwordString)) {
                            Toast.makeText(LogInPage.this, "Logged in Successfully", Toast.LENGTH_LONG).show();
                            loggedIn = true;
                            startActivity(intent);
                        } else {
                            Toast.makeText(LogInPage.this, "Failed to log in", Toast.LENGTH_LONG).show();
                            break;  // Sortie de la boucle si les identifiants sont incorrects
                        }
                    }
                }
            }
        });


        /* submitLogInButton = findViewById(R.id.submitLogInButton);

>>>>>>> f32651b5a3892961a413a245ce974e6ab8e28e57

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
                        Toast.makeText(LogInPage.this, "Logged in Successfully", Toast.LENGTH_LONG).show();
                        intent.putExtra("UserType", "Administrator");
                        loggedIn=true;
                        startActivity(intent);
                    } else {
                        Intent intent2 = new Intent(LogInPage.this, WelcomePage.class);
                        String userType = getIntent().getStringExtra("UserType");
                        intent2.putExtra("UserType", userType);
                        String emailUser = getIntent().getStringExtra("Email");
                        String passWordUser = getIntent().getStringExtra("passWord");
                        if (emailAddressString.equals(emailUser) && passWordUser.equals(passwordString)) {
                            Toast.makeText(LogInPage.this, "Logged in Successfully", Toast.LENGTH_LONG).show();
                            loggedIn=true;
                            startActivity(intent2);
                        } else {
                            Toast.makeText(LogInPage.this, "failed to Log", Toast.LENGTH_LONG).show();
                        }


                    }
                }
            }
        });



    }
}
