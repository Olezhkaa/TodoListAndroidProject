package com.example.myapplication1;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class AddNoteActivity extends AppCompatActivity {

    EditText titleEditText;
    EditText dateEditText;
    EditText timeEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolBar);
        //toolbar.setTitle("Add a note");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickAddButton(View view) {
        titleEditText = findViewById(R.id.TitleEditText);
        dateEditText = findViewById(R.id.DateEditText);
        timeEditText = findViewById(R.id.TimeEditText);
        if(!titleEditText.getText().toString().trim().isEmpty() && !dateEditText.getText().toString().trim().isEmpty() && !timeEditText.getText().toString().trim().isEmpty()) {
            SharedPreferences preferences = getSharedPreferences("userId", MODE_PRIVATE);
            int userId = Integer.parseInt(preferences.getString("id", ""));
            //Toast.makeText(this, String.valueOf(userId), Toast.LENGTH_LONG).show();

            MyDatabaseHelper myDB = new MyDatabaseHelper(this);
            myDB.addNotes(userId, titleEditText.getText().toString().trim(), dateEditText.getText().toString().trim(), timeEditText.getText().toString().trim());
            finish();

        }
        else Toast.makeText(this, "Fill in all the fields!", Toast.LENGTH_LONG).show();
    }

}