package com.example.myapplication1;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity {

    MyDatabaseHelper myDB;
    ArrayList<String> user_id, user_username, user_email, user_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        myDB = new MyDatabaseHelper(this);
        user_id = new ArrayList<>();
        user_username = new ArrayList<>();
        user_email = new ArrayList<>();
        user_password = new ArrayList<>();
        storeDataInArrays();
    }

    void storeDataInArrays() {
        String TABLE_NAME = "Users";
        Cursor cursor = myDB.readAllDate(TABLE_NAME);
        if(cursor.getCount() == 0) {
            makeText(this, "No data!", LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()) {
                user_id.add(cursor.getString(0));
                user_username.add(cursor.getString(1));
                user_email.add(cursor.getString(2));
                user_password.add(cursor.getString(3));
            }
        }
    }

    public void onClickSignUpButton(View view) {
        Toast toast = Toast.makeText(this, "You have successfully registered!", Toast.LENGTH_LONG);
        EditText UsernameEditText = findViewById(R.id.UsernameEditText);
        EditText EmailEditText = findViewById(R.id.EmailEditText);
        EditText PasswordEditText = findViewById(R.id.PasswordEditText);
        EditText ConfirmPasswordEditText = findViewById(R.id.ConfirmPasswordEditText);
        Boolean flagSignupUser = false;
        if (!UsernameEditText.getText().toString().trim().isEmpty() && !EmailEditText.getText().toString().trim().isEmpty() && !PasswordEditText.getText().toString().trim().isEmpty() && !ConfirmPasswordEditText.getText().toString().trim().isEmpty()) {
            if (PasswordEditText.getText().toString().trim().length() >= 8 && PasswordEditText.getText().toString().trim().length() <= 16) {
                if (PasswordEditText.getText().toString().trim().equals(ConfirmPasswordEditText.getText().toString().trim())) {
                    for(int i = 0; i<user_id.size(); i++) {
                        if (user_username.get(i).equals(UsernameEditText.getText().toString().trim()) || user_email.get(i).equals(EmailEditText.getText().toString().trim())) {
                            flagSignupUser = true;
                        }
                    }
                    if (!flagSignupUser) {
                        MyDatabaseHelper myDB = new MyDatabaseHelper(this);
                        myDB.addUser(UsernameEditText.getText().toString().trim(), EmailEditText.getText().toString().trim(), PasswordEditText.getText().toString().trim());
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                        toast.show();
                    }
                    else Toast.makeText(this, "This username or email has already been registered!", Toast.LENGTH_LONG).show();
                } else {
                    Toast toastConfirmPassword = Toast.makeText(this, "Passwords don't match!", Toast.LENGTH_LONG);
                    toastConfirmPassword.show();
                }
            } else {
                Toast toastPassword = Toast.makeText(this, "The password must be at least 8 characters and no more than 16!", Toast.LENGTH_LONG);
                toastPassword.show();
            }
        } else {
            Toast toastEmpty = Toast.makeText(this, "Fill in all the fields!", Toast.LENGTH_LONG);
            toastEmpty.show();
        }


    }

    public void onClickLogInButton(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}