package com.example.myapplication1;

import static androidx.navigation.Navigation.findNavController;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.fragment.app.FragmentContainerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

import javax.security.auth.Destroyable;

public class MainAndBottomNavigation extends AppCompatActivity {


    public Toolbar hatToolBar;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_and_buttom_navigation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        hatToolBar = findViewById(R.id.hatToolBar);
        hatToolBar.setTitle("Toolbar");
        setSupportActionBar(hatToolBar);

        //Show back button toolBar
        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        replaceFragment(new HomeFragment(todayDate()));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.home) {
                hatToolBar.setTitle("Today");
                replaceFragment(new HomeFragment(todayDate()));
            }
            if(item.getItemId() == R.id.calendar) {
                hatToolBar.setTitle("Calendar");
                replaceFragment(new CalendarFragment());
            }
            if(item.getItemId() == R.id.friends) {
                hatToolBar.setTitle("Friends");
                replaceFragment(new FriendsFragment());
            }
            if(item.getItemId() == R.id.profile) {
                hatToolBar.setTitle("Profile");
                replaceFragment(new ProfileFragment());
            }
            return true;
        });

    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        onDestroy();
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        moveTaskToBack(true);

        super.onDestroy();

        System.runFinalizersOnExit(true);
        System.exit(0);
    }

    public void onClickLogOutButton(View view) {
        SharedPreferences preferences = getSharedPreferences("checkBox", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("remember", "false");
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public String todayDate() {
        Calendar calendar = Calendar.getInstance();
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        if(Integer.parseInt(day) < 10) day = "0" + day;
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        if (Integer.parseInt(month) < 10) month = "0" + month;
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String date = day + "." + month + "." + year;
        Toast.makeText(this, date, Toast.LENGTH_SHORT).show();
        return date;
    }



    //back button
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        if (item.getItemId()==android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

}