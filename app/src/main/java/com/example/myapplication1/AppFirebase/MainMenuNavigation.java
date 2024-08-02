package com.example.myapplication1.AppFirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication1.AppFirebase.ui.CalendarFragmentFirebase;
import com.example.myapplication1.AppFirebase.ui.FriendsFragmentFirebase;
import com.example.myapplication1.AppFirebase.ui.HomeFragmentFirebase;
import com.example.myapplication1.AppFirebase.ui.ProfileFragmentFirebase;
import com.example.myapplication1.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication1.databinding.ActivityMainMenuNavigationBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainMenuNavigation extends AppCompatActivity {

    ActivityMainMenuNavigationBinding binding;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainMenuNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(FirebaseAuth.getInstance().getCurrentUser() == null) { startActivity(new Intent(this, LoginFirebase.class)); }

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        /*AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragmentFirebase, R.id.calendarFragmentFirebase, R.id.friendsFragmentFirebase, R.id.profileFragmentFirebase).build();
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);*/

        replaceFragment(new HomeFragmentFirebase());
        toolbar.setTitle("Today");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.homeFragmentFirebase) {
                toolbar.setTitle("Today");
                replaceFragment(new HomeFragmentFirebase());
            }
            if(item.getItemId() == R.id.calendarFragmentFirebase) {
                toolbar.setTitle("Calendar");
                replaceFragment(new CalendarFragmentFirebase());
            }
            if(item.getItemId() == R.id.friendsFragmentFirebase) {
                toolbar.setTitle("Friends");
                replaceFragment(new FriendsFragmentFirebase());
            }
            if(item.getItemId() == R.id.profileFragmentFirebase) {
                toolbar.setTitle("Profile");
                replaceFragment(new ProfileFragmentFirebase());
            }
            return true;
        });

    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void onClickLogOutButton(View view) {
        AuthUI.getInstance()
                .signOut(MainMenuNavigation.this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainMenuNavigation.this, "User Signed Out", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainMenuNavigation.this, LoginFirebase.class));
                    }
                });
    }
}