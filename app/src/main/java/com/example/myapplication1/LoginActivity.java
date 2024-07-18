package com.example.myapplication1;

import static android.widget.Toast.*;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SharedMemory;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.MessageFormat;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    EditText usernameEditText;
    EditText passwordEditText;

    MyDatabaseHelper myDB;
    ArrayList<String> user_id, user_username, user_email, user_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Remember log in account
        SharedPreferences preferences = getSharedPreferences("checkBox", MODE_PRIVATE);
        String checkBox = preferences.getString("remember", "");
        if (checkBox.equals("true"))
        {
            Intent intent = new Intent(this, MainAndBottomNavigation.class);
            startActivity(intent);
        }

        myDB = new MyDatabaseHelper(this);
        user_id = new ArrayList<>();
        user_username = new ArrayList<>();
        user_email = new ArrayList<>();
        user_password = new ArrayList<>();
        storeDataInArrays();

        CheckBox rememberCheckBox = findViewById(R.id.rememberCheckBox);
        rememberCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()) {
                    SharedPreferences preferences = getSharedPreferences("checkBox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "true");
                    editor.apply();
                }
                else {
                    SharedPreferences preferences = getSharedPreferences("checkBox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                }
            }
        });

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

    public void onClickLoginButton(View view) {
        usernameEditText = findViewById(R.id.UsernameEditText);
        passwordEditText = findViewById(R.id.PasswordEditText);
        Boolean flagIncorrectUsernameOrPassword = false;
        if(!usernameEditText.getText().toString().trim().isEmpty() && !passwordEditText.getText().toString().trim().isEmpty()) {
            for(int i = 0; i < user_id.size(); i++)
            {
                if(usernameEditText.getText().toString().trim().equals(user_username.get(i)) && passwordEditText.getText().toString().trim().equals(user_password.get(i)))
                {
                    SharedPreferences preferences = getSharedPreferences("userId", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("id", user_id.get(i));
                    editor.apply();

                    Intent intent = new Intent(this, MainAndBottomNavigation.class);
                    startActivity(intent);

                    makeText(this, MessageFormat.format("Welcome, {0}!", usernameEditText.getText()), LENGTH_LONG).show();
                    flagIncorrectUsernameOrPassword = true;

                    usernameEditText.setText(null);
                    passwordEditText.setText(null);
                }
            }
            if(!flagIncorrectUsernameOrPassword) makeText(this, "Incorrect username or password!", LENGTH_LONG).show();
        }
        else {
            makeText(this, "Fill in all the fields!", LENGTH_LONG).show();
        }
    }



    public void onClickSignUpButton(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}