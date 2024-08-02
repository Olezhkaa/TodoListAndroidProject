package com.example.myapplication1.AppFirebase.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication1.Adapter.CustomAdapterFriendsFirebase;
import com.example.myapplication1.Adapter.CustomAdapterNoteFirebase;
import com.example.myapplication1.AppFirebase.AddFriendFirebase;
import com.example.myapplication1.AppFirebase.AddNoteFirebase;
import com.example.myapplication1.AppFirebase.MainMenuNavigation;
import com.example.myapplication1.Models.Friend;
import com.example.myapplication1.Models.Note;
import com.example.myapplication1.Models.User;
import com.example.myapplication1.R;
import com.example.myapplication1.databinding.FragmentFriendsFirebaseBinding;
import com.example.myapplication1.databinding.FragmentHomeFirebaseBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class FriendsFragmentFirebase extends Fragment {

    private FragmentFriendsFirebaseBinding binding;

    RecyclerView recyclerView;
    FirebaseDatabase db;
    DatabaseReference users;
    CustomAdapterFriendsFirebase customAdapterFriendsFirebase;
    ArrayList<User> userArrayList;

    ArrayList<Friend> friendArrayList;
    DatabaseReference friends;

    FloatingActionButton addButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFriendsFirebaseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.recyclerView);
        db = FirebaseDatabase.getInstance("https://todolistandroidproject-default-rtdb.europe-west1.firebasedatabase.app/");
        users = db.getReference("Users");
        friends = db.getReference("Friends").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userArrayList = new ArrayList<>();
        customAdapterFriendsFirebase = new CustomAdapterFriendsFirebase(getActivity(), getContext(), userArrayList);
        recyclerView.setAdapter(customAdapterFriendsFirebase);

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        friends.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Friend friend = dataSnapshot.getValue(Friend.class);
                    users.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot : snapshot.getChildren())
                            {
                                if(friend.getFriendUID().equals(dataSnapshot.getKey()))
                                {
                                    User user = dataSnapshot.getValue(User.class);
                                    userArrayList.add(user);
                                    break;
                                }
                            }
                            customAdapterFriendsFirebase.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        addButton = root.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddFriendFirebase.class));
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}