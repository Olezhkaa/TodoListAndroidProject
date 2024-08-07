package com.example.myapplication1.AppFirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication1.Models.Note;
import com.example.myapplication1.R;
import com.example.myapplication1.databinding.ActivityUpdateNoteFirebaseBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class UpdateNoteFirebase extends AppCompatActivity {

    private ActivityUpdateNoteFirebaseBinding binding;

    EditText titleEditText, dateEditText, timeEditText;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference notes;

    String keyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateNoteFirebaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        titleEditText = findViewById(R.id.titleEditText);
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://todolistandroidproject-default-rtdb.europe-west1.firebasedatabase.app/");
        notes = db.getReference("Notes");

        getAndSetIntentDate();
        getKeyNoteData();




    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void getAndSetIntentDate() {
        if(getIntent().hasExtra("title") && getIntent().hasExtra("date") && getIntent().hasExtra("time")) {
            //Get
            String title = getIntent().getStringExtra("title");
            String date = getIntent().getStringExtra("date");
            String time = getIntent().getStringExtra("time");

            //Set
            if (title != null && date != null && time != null)
            {
                titleEditText.setText(title);
                dateEditText.setText(date);
                timeEditText.setText(time);
            }
        }
        else Toast.makeText(this, "No date", Toast.LENGTH_LONG).show();
    }

    public void onClickUpdateButton(View view) {
        if(titleEditText.getText().toString().trim().isEmpty() || dateEditText.getText().toString().trim().isEmpty() || timeEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(UpdateNoteFirebase.this, "Fill in all the fields!", Toast.LENGTH_LONG).show();
        } else {

            Note note = new Note(titleEditText.getText().toString().trim(), dateEditText.getText().toString().trim(), timeEditText.getText().toString().trim());
            notes.child(keyData).setValue(note);
            Toast.makeText(UpdateNoteFirebase.this, "Note Updated", Toast.LENGTH_LONG).show();
            startActivity(new Intent(UpdateNoteFirebase.this, MainMenuNavigation.class));
        }

    }

    public void onClickDeleteButton(View view) {
        try {
            notes.child(keyData).removeValue();
            Toast.makeText(UpdateNoteFirebase.this, "Note Deleted", Toast.LENGTH_LONG).show();
            startActivity(new Intent(UpdateNoteFirebase.this, MainMenuNavigation.class));
        }
        catch (Exception e) { Toast.makeText(UpdateNoteFirebase.this, "Error: " + e, Toast.LENGTH_LONG).show(); }
    }

    void getKeyNoteData()
    {
        notes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Note note = dataSnapshot.getValue(Note.class);
                    if(note.getTitle().equals(titleEditText.getText().toString().trim()) && note.getDate().equals(dateEditText.getText().toString().trim()) && note.getTime().equals(timeEditText.getText().toString().trim())) {
                        keyData = dataSnapshot.getKey();
                        //Toast.makeText(UpdateNoteFirebase.this, keyData, Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}