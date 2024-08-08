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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication1.Adapter.CustomAdapterNoteFirebase;
import com.example.myapplication1.AppFirebase.AddNoteFirebase;
import com.example.myapplication1.AppFirebase.LoginFirebase;
import com.example.myapplication1.Models.Note;
import com.example.myapplication1.Models.User;
import com.example.myapplication1.R;
import com.example.myapplication1.databinding.FragmentHomeFirebaseBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class HomeFragmentFirebase extends Fragment {

    private FragmentHomeFirebaseBinding binding;

    private FloatingActionButton addButton;
    FrameLayout frameLayout;

    RecyclerView recyclerView;
    FirebaseDatabase db;
    DatabaseReference notes;
    CustomAdapterNoteFirebase customAdapterNoteFirebase;
    ArrayList<Note> noteArrayList;

    TextView noDataTextView;
    ImageView emptyImageView;

    String userUID, date;

    String accessRights= "";


    public HomeFragmentFirebase() {
        userUID = getUserUID();
        date = todayDate();
    }
    public HomeFragmentFirebase(String date) {
        userUID = getUserUID();
        this.date = date;
    }
    public HomeFragmentFirebase(String userUID, boolean friend) {
        this.userUID = userUID;
        date = todayDate();
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeFirebaseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if(FirebaseAuth.getInstance().getCurrentUser() == null) { startActivity(new Intent(getActivity(), LoginFirebase.class)); }

        frameLayout = root.findViewById(R.id.frameLayout);

        noDataTextView = root.findViewById(R.id.noDataTextView);
        emptyImageView = root.findViewById(R.id.emptyImageView);

        recyclerView = root.findViewById(R.id.recyclerView);
        db = FirebaseDatabase.getInstance("https://todolistandroidproject-default-rtdb.europe-west1.firebasedatabase.app/");
        notes = db.getReference("Notes").child(userUID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        noteArrayList = new ArrayList<>();
        customAdapterNoteFirebase = new CustomAdapterNoteFirebase(getActivity(), getContext(), noteArrayList);
        recyclerView.setAdapter(customAdapterNoteFirebase);

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        notes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noteArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Note note = dataSnapshot.getValue(Note.class);
                    if(Objects.requireNonNull(note).getDate().equals(date) && Objects.requireNonNull(note).getIdUser().equals(userUID)) noteArrayList.add(note);
                }
                customAdapterNoteFirebase.notifyDataSetChanged();
                emptyRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        addButton = root.findViewById(R.id.addButton);
        visibleAddNoteButton();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddNoteFirebase.class);
                intent.putExtra("userIdAddNote", userUID);
                startActivity(intent);
            }
        });



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void emptyRecyclerView() {
        if(customAdapterNoteFirebase.getItemCount() == 0) {
            emptyImageView.setVisibility(View.VISIBLE);
            noDataTextView.setVisibility(View.VISIBLE);
        } else {
            emptyImageView.setVisibility(View.GONE);
            noDataTextView.setVisibility(View.GONE);
        }
    }

    public String todayDate() {
        Calendar calendar = Calendar.getInstance();
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        if(Integer.parseInt(day) < 10) day = "0" + day;
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        if (Integer.parseInt(month) < 10) month = "0" + month;
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String date = day + "." + month + "." + year;
        return date;
    }

    public String getUserUID() {
        String userUid = null;
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        return userUid;
    }

    public void visibleAddNoteButton() {
        DatabaseReference users = db.getReference("Users");
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if(dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            accessRights = user.getAccessRights();
                            if(!accessRights.equals("Admin") && !userUID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                addButton.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}