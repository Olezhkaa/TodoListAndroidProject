package com.example.myapplication1.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication1.AppFirebase.ChatActivityFirebase;
import com.example.myapplication1.AppFirebase.ui.HomeFragmentFirebase;
import com.example.myapplication1.Models.Note;
import com.example.myapplication1.Models.User;
import com.example.myapplication1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class CustomAdapterFriendsFirebase extends RecyclerView.Adapter<CustomAdapterFriendsFirebase.MyViewHolder>{

    Context context;
    Activity activity;

    ArrayList<User> userList;

    public CustomAdapterFriendsFirebase(Activity activity, Context context, ArrayList<User> userList) {
        this.activity = activity;
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public CustomAdapterFriendsFirebase.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row_friends, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterFriendsFirebase.MyViewHolder holder, int position) {
        User user = userList.get(position);
        holder.usernameTextView.setText(user.getUsername());
        holder.emailTextView.setText(user.getEmail());

        holder.rowFriendsConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = String.valueOf(user.getEmail());
                FirebaseDatabase.getInstance("https://todolistandroidproject-default-rtdb.europe-west1.firebasedatabase.app/")
                        .getReference("Users").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    User user = dataSnapshot.getValue(User.class);
                                    if(user.getEmail().equals(email)) {
                                        FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.frameLayout, new HomeFragmentFirebase(dataSnapshot.getKey(), true));
                                        fragmentTransaction.commit();
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

            }

        });
        holder.messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivityFirebase.class);
                intent.putExtra("messageUsername", String.valueOf(user.getUsername()));
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView usernameTextView, emailTextView;
        ConstraintLayout rowFriendsConstraintLayout;
        ImageButton messageButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            rowFriendsConstraintLayout = itemView.findViewById(R.id.rowFriendsConstraintLayout);
            messageButton = itemView.findViewById(R.id.messageButton);
        }
    }
}
