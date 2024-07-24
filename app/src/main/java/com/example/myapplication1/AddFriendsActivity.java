package com.example.myapplication1;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Objects;

public class AddFriendsActivity extends AppCompatActivity {

    int user_id, friend_id;
    EditText emailEditText;
    Button addFriendButton;

    MyDatabaseHelper myDB;

    ArrayList<String> user_id_TableUsers, user_email_TableUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_friends);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        myDB = new MyDatabaseHelper(this);
        user_id_TableUsers = new ArrayList<>();
        user_email_TableUsers = new ArrayList<>();
        storeDataInArrays();

        emailEditText = findViewById(R.id.emailEditText);
        addFriendButton = findViewById(R.id.addFriendButton);

        Toolbar hatToolbar = findViewById(R.id.hatToolbar);
        setSupportActionBar(hatToolbar);
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

    public void onClickAddFriendButton(View view) {
        boolean flag = false;
        if(!emailEditText.getText().toString().trim().isEmpty()) {
            for(int i = user_email_TableUsers.size()-1; i >= 0; i--)
            {
                if(emailEditText.getText().toString().trim().equals(user_email_TableUsers.get(i).toString().trim()))
                {
                    SharedPreferences preferences = getSharedPreferences("userId", MODE_PRIVATE);
                    user_id = Integer.parseInt(preferences.getString("id", ""));
                    //Toast.makeText(this, String.valueOf(userId), Toast.LENGTH_LONG).show();
                    friend_id = Integer.parseInt(user_id_TableUsers.get(i));
                    myDB.addFriends(user_id, friend_id);
                    flag = true;
                    onBackPressed();
                }
            }
            if (flag == false)
            {
                Toast.makeText(this, "Invalid data!", Toast.LENGTH_LONG).show();
            }
        }
        else Toast.makeText(this, "Fill in all the fields!", Toast.LENGTH_LONG).show();
    }

    void storeDataInArrays() {
        String TABLE_NAME = "Users";
        Cursor cursor = myDB.readAllDate(TABLE_NAME);
        if(cursor.getCount() == 0) {
            makeText(this, "No data!", LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()) {
                user_id_TableUsers.add(cursor.getString(0));
                user_email_TableUsers.add(cursor.getString(2));
            }
        }
    }
}