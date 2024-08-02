package com.example.myapplication1.AppFirebase;

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

import com.example.myapplication1.Models.Friend;
import com.example.myapplication1.Models.User;
import com.example.myapplication1.R;
import com.example.myapplication1.databinding.ActivityAddFriendFirebaseBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class AddFriendFirebase extends AppCompatActivity {

    private ActivityAddFriendFirebaseBinding binding;
    private EditText emailEditText;
    private Button addFriendButton;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference friends;
    DatabaseReference users;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddFriendFirebaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        emailEditText = binding.emailEditText;
        addFriendButton = binding.addFriendButton;

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://todolistandroidproject-default-rtdb.europe-west1.firebasedatabase.app/");
        friends = db.getReference("Friends").child(auth.getCurrentUser().getUid());
        users = db.getReference("Users");

        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString().trim();
                if(email.isEmpty()) {
                    Toast.makeText(AddFriendFirebase.this, "Fill in all the fields!", Toast.LENGTH_LONG).show();
                } else {
                    users.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean flagUserAvailability = false;
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                User user = dataSnapshot.getValue(User.class);
                                if(user.getEmail().equals(email)) {
                                    String friendsUid = dataSnapshot.getKey();
                                    Friend friend = new Friend(friendsUid);
                                    if(!auth.getCurrentUser().getUid().equals(friendsUid)) {
                                        flagUserAvailability = true;
                                        friends.push().setValue(friend).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(AddFriendFirebase.this, "Successfully added", Toast.LENGTH_LONG).show();
                                                onBackPressed();
                                            }
                                        });
                                    }

                                }
                                else
                                {
                                    flagUserAvailability = true;
                                    Toast.makeText(AddFriendFirebase.this, "You have entered your email", Toast.LENGTH_LONG).show();
                                }
                            }
                            if (!flagUserAvailability) Toast.makeText(AddFriendFirebase.this, "This user does not exist!", Toast.LENGTH_LONG).show();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(AddFriendFirebase.this, "This user does not exist!", Toast.LENGTH_LONG).show();

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