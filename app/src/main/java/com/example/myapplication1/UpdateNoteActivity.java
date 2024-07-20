package com.example.myapplication1;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class UpdateNoteActivity extends AppCompatActivity {

    EditText titleInput, dateInput, timeInput;
    Button updateButton, deleteButton;

    String id, user_id, title, date, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences preferences = getSharedPreferences("userId", MODE_PRIVATE);
        user_id = preferences.getString("id", "");

        titleInput = findViewById(R.id.TitleEditText);
        dateInput = findViewById(R.id.DateEditText);
        timeInput = findViewById(R.id.TimeEditText);
        updateButton = findViewById(R.id.UpdateButton);
        deleteButton = findViewById(R.id.DeleteButton);

        getAndSetIntentDate();

        Toolbar toolbar = findViewById(R.id.toolBar);
        //toolbar.setTitle("Update a note");
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

    public void onClickUpdateButton(View view) {
        MyDatabaseHelper myDB = new MyDatabaseHelper(this);
        myDB.updateData(id, titleInput.getText().toString().trim(), dateInput.getText().toString().trim(), timeInput.getText().toString().trim());
        Intent intent = new Intent(this, MainAndBottomNavigation.class);
        startActivity(intent);
    }

    public void onClickDeleteButton(View view) {
        confirmDialog();
    }

    void getAndSetIntentDate() {
        if(getIntent().hasExtra("id") && getIntent().hasExtra("title") && getIntent().hasExtra("date") && getIntent().hasExtra("time")) {
            //Get
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            date = getIntent().getStringExtra("date");
            time = getIntent().getStringExtra("time");

            //Set
            titleInput.setText(title);
            dateInput.setText(date);
            timeInput.setText(time);
        }
        else Toast.makeText(this, "No date", Toast.LENGTH_LONG).show();
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + title + "?");
        builder.setMessage("Are you sure you want to delete " + title + "?");
        builder.setPositiveButton("Yes", (new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper db = new MyDatabaseHelper(UpdateNoteActivity.this);
                db.deleteDataOneRow(id);
                finish();
            }
        }));
        builder.setNegativeButton("No", (new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }));
        builder.create().show();
    }

    public void deleteAllButton(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete all?");
        builder.setMessage("Are you sure you want to delete all?");
        builder.setPositiveButton("Yes", (new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper db = new MyDatabaseHelper(UpdateNoteActivity.this);
                db.deleteAllDataWhereData("Notes", "user_id", user_id);
                finish();
            }
        }));
        builder.setNegativeButton("No", (new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }));
        builder.create().show();
    }
}