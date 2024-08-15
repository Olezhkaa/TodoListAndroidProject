package com.example.myapplication1.AppFirebase;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication1.Adapter.CustomAdapterMessageFirebase;
import com.example.myapplication1.Models.Message;
import com.example.myapplication1.Models.Note;
import com.example.myapplication1.Models.User;
import com.example.myapplication1.R;
import com.example.myapplication1.databinding.ActivityChatFirebaseBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ChatActivityFirebase extends AppCompatActivity {

    ActivityChatFirebaseBinding binding;

    Toolbar toolbar;
    FloatingActionButton sendButton;
    EditText messageEditText;

    RecyclerView recyclerView;
    FirebaseDatabase db;
    DatabaseReference chat;
    CustomAdapterMessageFirebase customAdapterMessageFirebase;
    ArrayList<Message> messageArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatFirebaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = findViewById(R.id.toolBar);
        String username = getIntent().getStringExtra("messageUsername");
        String[] usernameArr = username.split(" ");
        if(usernameArr.length != 1) {
            username = usernameArr[0] + "\n" +  usernameArr[1];
        }
        else {
            binding.linearLayout.setGravity(Gravity.CENTER);
            binding.profileImageView.setPadding(0, 0, 0, 20);
        }
        setSupportActionBar(toolbar);

        TextPaint paint = binding.titleTextView.getPaint();
        float width = paint.measureText("Tianjin, China");
        Shader textShader = new LinearGradient(0, 0, width, binding.titleTextView.getTextSize(),
                new int[]{
                        Color.parseColor("#7490BB"),
                        Color.parseColor("#2D538C"),
                }, null, Shader.TileMode.CLAMP);
        binding.titleTextView.getPaint().setShader(textShader);

        binding.titleTextView.setText(username);

        recyclerView = binding.recyclerView;
        db = FirebaseDatabase.getInstance("https://todolistandroidproject-default-rtdb.europe-west1.firebasedatabase.app/");
        chat = db.getReference("Chat");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        displayAllMessages();

        messageArrayList = new ArrayList<>();
        customAdapterMessageFirebase = new CustomAdapterMessageFirebase(this, this, messageArrayList);
        recyclerView.setAdapter(customAdapterMessageFirebase);

        messageEditText = binding.messageEditText;
        binding.textInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                messageArrayList.clear();
                if(!messageEditText.getText().toString().trim().isEmpty()) {
                    String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String friendUid = getIntent().getStringExtra("messageFriendUid");
                    DatabaseReference users = db.getReference("Users");
                    users.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                User user = dataSnapshot.getValue(User.class);
                                assert user != null;
                                if(dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    String username = user.getUsername();
                                    Message message = new Message(username, messageEditText.getText().toString().trim());
                                    chat.child(userUid).child(friendUid).push().setValue(message);
                                    chat.child(friendUid).child(userUid).push().setValue(message);
                                    messageEditText.setText("");
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
        });
        /*sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageArrayList.clear();
                if(!messageEditText.getText().toString().trim().isEmpty()) {
                    String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String friendUid = getIntent().getStringExtra("messageFriendUid");
                    DatabaseReference users = db.getReference("Users");
                    users.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                User user = dataSnapshot.getValue(User.class);
                                assert user != null;
                                if(dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    String username = user.getUsername();
                                    Message message = new Message(username, messageEditText.getText().toString().trim());
                                    chat.child(userUid).child(friendUid).push().setValue(message);
                                    chat.child(friendUid).child(userUid).push().setValue(message);
                                    messageEditText.setText("");
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
        });*/
    }

    private void displayAllMessages() {
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String friendUid = getIntent().getStringExtra("messageFriendUid");
        chat.child(userUid).child(friendUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    messageArrayList.add(message);
                }
                customAdapterMessageFirebase.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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