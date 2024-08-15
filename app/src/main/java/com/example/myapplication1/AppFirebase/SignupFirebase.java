package com.example.myapplication1.AppFirebase;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication1.Models.User;
import com.example.myapplication1.R;
import com.example.myapplication1.databinding.ActivitySignupFirebaseBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Objects;

public class SignupFirebase extends AppCompatActivity {

    ActivitySignupFirebaseBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    Button signUpButton, signInButton;
    EditText usernameEditText, emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupFirebaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://todolistandroidproject-default-rtdb.europe-west1.firebasedatabase.app/");
        users = db.getReference("Users");

        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        TextPaint paint = binding.titleTextView.getPaint();
        float width = paint.measureText("Tianjin, China");

        Shader textShader = new LinearGradient(0, 0, width, binding.titleTextView.getTextSize(),
                new int[]{
                        Color.parseColor("#7490BB"),
                        Color.parseColor("#2D538C"),
                }, null, Shader.TileMode.CLAMP);
        binding.titleTextView.getPaint().setShader(textShader);

        signUpButton = findViewById(R.id.signUpButton);

        try {
            signUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (usernameEditText.getText().toString().trim().isEmpty() || emailEditText.getText().toString().trim().isEmpty() || passwordEditText.getText().toString().trim().isEmpty()) {
                        Toast.makeText(SignupFirebase.this, "Fields cannot be empty", Toast.LENGTH_LONG).show();
                    }
                    else {
                        if (passwordEditText.getText().toString().trim().length() >= 8 && passwordEditText.getText().toString().trim().length() <= 16) {
                            auth.createUserWithEmailAndPassword(emailEditText.getText().toString().strip(), passwordEditText.getText().toString().strip())
                                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            User user = new User(usernameEditText.getText().toString().trim(), emailEditText.getText().toString().trim(), passwordEditText.getText().toString().trim());

                                            users.child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).setValue(user)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(SignupFirebase.this, MessageFormat.format("Welcome, {0}!", usernameEditText.getText()), Toast.LENGTH_LONG).show();
                                                            startActivity(new Intent(SignupFirebase.this, MainMenuNavigation.class));
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(SignupFirebase.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                        }
                                    });
                        } else Toast.makeText(SignupFirebase.this,"The password must be at least 8 characters and no more than 16", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(SignupFirebase.this, "Error " + e, Toast.LENGTH_LONG).show();
            startActivity(new Intent(SignupFirebase.this, SignupFirebase.class));
        }

        binding.logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupFirebase.this, LoginFirebase.class));
            }
        });

    }


}