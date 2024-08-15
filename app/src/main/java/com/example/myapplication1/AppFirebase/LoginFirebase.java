package com.example.myapplication1.AppFirebase;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
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

import com.example.myapplication1.R;
import com.example.myapplication1.databinding.ActivityLoginFirebaseBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFirebase extends AppCompatActivity {

    ActivityLoginFirebaseBinding binding;

    private EditText emailEditText, passwordEditText;
    Button signInButton;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginFirebaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signInButton = findViewById(R.id.signInButton);

        auth = FirebaseAuth.getInstance();

        TextPaint paint = binding.titleTextView.getPaint();
        float width = paint.measureText("Tianjin, China");

        Shader textShader = new LinearGradient(0, 0, width, binding.titleTextView.getTextSize(),
                new int[]{
                        Color.parseColor("#7490BB"),
                        Color.parseColor("#2D538C"),
                }, null, Shader.TileMode.CLAMP);
        binding.titleTextView.getPaint().setShader(textShader);
    }

    public void onClickSignIn(View view) {
        if(emailEditText.getText().toString().trim().isEmpty() || passwordEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(LoginFirebase.this, "Fields cannot be empty", Toast.LENGTH_LONG).show();
        }
        else {
            auth.signInWithEmailAndPassword(emailEditText.getText().toString().trim(), passwordEditText.getText().toString().trim())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            startActivity(new Intent(LoginFirebase.this, MainMenuNavigation.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginFirebase.this, "Error: " + e, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(LoginFirebase.this, LoginFirebase.class));
                        }
                    });
        }
    }

    public void onClickGoSingUp(View view) {
        startActivity(new Intent(LoginFirebase.this, SignupFirebase.class));
    }

}