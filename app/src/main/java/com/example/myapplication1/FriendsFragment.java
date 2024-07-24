package com.example.myapplication1;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication1.Adapter.CustomAdapterFriends;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class FriendsFragment extends Fragment {

    RecyclerView recyclerView;

    FloatingActionButton addButton;

    MyDatabaseHelper myDB;
    ArrayList<String> user_id, friend_id, user_username, user_email;

    String user_id_active;

    ArrayList<String> user_id_in_adapter, user_username_in_adapter, user_email_in_adapter;

    CustomAdapterFriends customAdapterFriends;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddFriendsActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences preferences = getActivity().getSharedPreferences("userId", Context.MODE_PRIVATE);
        user_id_active = preferences.getString("id", "");

        myDB = new MyDatabaseHelper(getActivity());
        user_id = new ArrayList<>();
        user_username = new ArrayList<>();
        user_email = new ArrayList<>();
        friend_id = new ArrayList<>();
        storeDataInArraysTableFriends();
        storeDataInArraysTableUsers();


        user_id_in_adapter = new ArrayList<>();
        user_username_in_adapter = new ArrayList<>();
        user_email_in_adapter = new ArrayList<>();

        for(int i = friend_id.size()-1; i >= 0; i--) {
            for(int j = user_id.size()-1; j >= 0; j--) {
                if(friend_id.get(i).trim().equals(user_id.get(j).trim()) && !user_id.get(j).trim().equals(user_id_active)) {
                    user_id_in_adapter.add(user_id.get(j));
                    user_username_in_adapter.add(user_username.get(j));
                    user_email_in_adapter.add(user_email.get(j));
                    break;
                }
            }
        }

        customAdapterFriends = new CustomAdapterFriends(getContext(), user_id_in_adapter, user_username_in_adapter, user_email_in_adapter);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(customAdapterFriends);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        return view;
    }

    void storeDataInArraysTableFriends() {
        String TABLE_NAME = "Friends";
        String COLUMN = "User_id";
        String COLUMN_DATA = user_id_active;
        Cursor cursor = myDB.readDateByColumnData(TABLE_NAME, COLUMN, COLUMN_DATA);
        if(cursor.getCount() == 0) {
            makeText(getActivity(), "No data!", LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()) {
                friend_id.add(cursor.getString(2));
            }
        }
    }

    void storeDataInArraysTableUsers() {
        String TABLE_NAME = "Users";
        Cursor cursor = myDB.readAllDate(TABLE_NAME);
        if(cursor.getCount() == 0) {
            makeText(getActivity(), "No data!", LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()) {
                user_id.add(cursor.getString(0));
                user_username.add(cursor.getString(1));
                user_email.add(cursor.getString(2));
            }
        }
    }

}