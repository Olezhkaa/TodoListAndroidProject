package com.example.myapplication1.AppFirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication1.Models.Note;
import com.example.myapplication1.Models.User;
import com.example.myapplication1.R;
import com.example.myapplication1.databinding.ActivityAddNoteFirebaseBinding;
import com.example.myapplication1.databinding.ActivityMainMenuNavigationBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class AddNoteFirebase extends AppCompatActivity {

    private ActivityAddNoteFirebaseBinding binding;
    private EditText titleEditText, dateEditText, timeEditText;
    private Button addNoteButton;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference notes;

    String userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNoteFirebaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        titleEditText = findViewById(R.id.titleEditText);
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);

        userUID = getIntent().getStringExtra("userIdAddNote");

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://todolistandroidproject-default-rtdb.europe-west1.firebasedatabase.app/");
        notes = db.getReference("Notes").child(userUID);


        addNoteButton = findViewById(R.id.addNoteButton);
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(titleEditText.getText().toString().trim().isEmpty() || dateEditText.getText().toString().trim().isEmpty() || timeEditText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AddNoteFirebase.this, "Fill in all the fields!", Toast.LENGTH_LONG).show();
                } else {
                    Note note = new Note(userUID, titleEditText.getText().toString().trim(), dateEditText.getText().toString().trim(), timeEditText.getText().toString().trim());
                    notes.push().setValue(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AddNoteFirebase.this, "The entry was successfully added", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(AddNoteFirebase.this, MainMenuNavigation.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddNoteFirebase.this, "Error: " + e, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(AddNoteFirebase.this, AddNoteFirebase.class));
                        }
                    });
                }
            }
        });


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}