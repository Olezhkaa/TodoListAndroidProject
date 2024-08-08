package com.example.myapplication1.AppFirebase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication1.AppFirebase.ui.CalendarFragmentFirebase;
import com.example.myapplication1.AppFirebase.ui.FriendsFragmentFirebase;
import com.example.myapplication1.AppFirebase.ui.HomeFragmentFirebase;
import com.example.myapplication1.AppFirebase.ui.ProfileFragmentFirebase;
import com.example.myapplication1.Models.User;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URI;

public class MainMenuNavigation extends AppCompatActivity {

    ActivityMainMenuNavigationBinding binding;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;

    FirebaseDatabase db;
    DatabaseReference usersOnline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainMenuNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() == null) { startActivity(new Intent(this, LoginFirebase.class)); }

        db = FirebaseDatabase.getInstance("https://todolistandroidproject-default-rtdb.europe-west1.firebasedatabase.app/");
        usersOnline = db.getReference("UsersOnline");

        if(auth.getCurrentUser() != null) {
            usersOnline.child(auth.getCurrentUser().getUid()).setValue(auth.getCurrentUser().getEmail());
        }

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);



    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 71 && data != null && data.getData() != null && resultCode == RESULT_OK) {
            Log.d("MyLog", "Image URI: " + data.getData());
            Uri filePath = data.getData();
            replaceFragment(new ProfileFragmentFirebase(this, this, filePath));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

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
                replaceFragment(new ProfileFragmentFirebase(this, this));
            }
            return true;
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}