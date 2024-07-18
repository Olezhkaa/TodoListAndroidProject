package com.example.myapplication1;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication1.Adapter.CustomAdapterHome;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;

    MyDatabaseHelper myDB;
    ArrayList<String> note_id, user_id, note_title, note_date, note_time;

    CustomAdapterHome customAdapterHome;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        FloatingActionButton addButton = (FloatingActionButton) view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddNoteActivity.class);
                startActivity(intent);
            }
        });

        myDB = new MyDatabaseHelper(getActivity());
        note_id = new ArrayList<>();
        user_id = new ArrayList<>();
        note_title = new ArrayList<>();
        note_date = new ArrayList<>();
        note_time = new ArrayList<>();
        storeDataInArrays();

        customAdapterHome = new CustomAdapterHome(getActivity(), note_title, note_time);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(customAdapterHome);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        return view;
    }

    void storeDataInArrays() {
        String TABLE_NAME = "Notes";
        String COLUMN = "User_id";
        SharedPreferences preferences = getActivity().getSharedPreferences("userId", Context.MODE_PRIVATE);
        String COLUMN_DATA = preferences.getString("id", "");

        Cursor cursor = myDB.readDateByColumnData(TABLE_NAME, COLUMN, COLUMN_DATA);
        if(cursor.getCount() == 0) {
            makeText(getActivity(), "No data!", LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()) {
                note_id.add(cursor.getString(0));
                user_id.add(cursor.getString(1));
                note_title.add(cursor.getString(2));
                note_date.add(cursor.getString(3));
                note_time.add(cursor.getString(4));
            }
        }
    }


}