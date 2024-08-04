package com.example.myapplication1.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication1.Models.Message;
import com.example.myapplication1.Models.User;
import com.example.myapplication1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomAdapterMessageFirebase extends RecyclerView.Adapter<CustomAdapterMessageFirebase.MyViewHolder>{

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    Context context;
    Activity activity;

    ArrayList<Message> messageList;

    public CustomAdapterMessageFirebase(Context context, Activity activity, ArrayList<Message> messageList) {
        this.context = context;
        this.activity = activity;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public CustomAdapterMessageFirebase.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        if (viewType == MSG_TYPE_LEFT) {
            view = inflater.inflate(R.layout.list_item_message_left, parent, false);
        } else {
            view = inflater.inflate(R.layout.list_item_message_right, parent, false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterMessageFirebase.MyViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.messageUsername.setText(message.getUserName());
        holder.messageText.setText(message.getTextMessage());
        holder.messageTime.setText(message.getTimeMessage());

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView messageUsername, messageText, messageTime;
        ConstraintLayout rowMessageConstraintLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            messageUsername = itemView.findViewById(R.id.messageUsername);
            messageText = itemView.findViewById(R.id.messageText);
            messageTime = itemView.findViewById(R.id.messageTime);
            rowMessageConstraintLayout = itemView.findViewById(R.id.rowFriendsConstraintLayout);
        }
    }

    @Override
    public int getItemViewType(int position) {
        DatabaseReference users = FirebaseDatabase.getInstance("https://todolistandroidproject-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users");
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    if(dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        User user = dataSnapshot.getValue(User.class);
                        SharedPreferences.Editor prefEditor = activity.getSharedPreferences("username", Context.MODE_PRIVATE).edit();
                        prefEditor.putString("username", user.getUsername());
                        prefEditor.apply();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        String username = activity.getSharedPreferences("username", Context.MODE_PRIVATE).getString("username", "");
        if(messageList.get(position).getUserName().equals(username)) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
