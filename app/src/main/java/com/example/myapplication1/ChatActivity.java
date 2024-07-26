package com.example.myapplication1;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ChatActivity extends AppCompatActivity {

    Toolbar toolbar;

    public static int SIGN_IN_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolBar);
        String username = getIntent().getStringExtra("messageUsername");
        setSupportActionBar(toolbar);
        toolbar.setTitle(username);
        //Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //signIn user
        /*if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_CODE);
        }
        else Toast.makeText(this, "The user is not logged in", Toast.LENGTH_LONG).show();

        displayAllMessages();*/
    }

    private void displayAllMessages() {

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}