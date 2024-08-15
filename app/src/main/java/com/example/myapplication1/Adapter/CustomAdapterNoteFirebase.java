package com.example.myapplication1.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication1.Models.Note;
import com.example.myapplication1.Models.User;
import com.example.myapplication1.R;
import com.example.myapplication1.AppFirebase.UpdateNoteFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomAdapterNoteFirebase extends RecyclerView.Adapter<CustomAdapterNoteFirebase.MyViewHolder> {

    Activity activity;
    Context context;
    ArrayList<Note> noteList;

    public CustomAdapterNoteFirebase(Activity activity, Context context, ArrayList<Note> noteList) {
        this.activity = activity;
        this.context = context;
        this.noteList = noteList;
    }


    @NonNull
    @Override
    public CustomAdapterNoteFirebase.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row_home, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterNoteFirebase.MyViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.titleTextView.setText(note.getTitle());
        holder.dateTextView.setText(note.getDate());
        holder.timeTextView.setText(note.getTime());

        styleColorText(holder.titleTextView);
        styleColorText(holder.dateTextView);
        styleColorText(holder.timeTextView);

        holder.rowHomeConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase db = FirebaseDatabase.getInstance("https://todolistandroidproject-default-rtdb.europe-west1.firebasedatabase.app/");
                DatabaseReference users = db.getReference("Users");
                users.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if(dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                User user = dataSnapshot.getValue(User.class);
                                String accessRights = user.getAccessRights();
                                if(accessRights.equals("User") && !FirebaseAuth.getInstance().getCurrentUser().getUid().equals(note.getIdUser())) { }
                                else {
                                    Intent intent = new Intent(context, UpdateNoteFirebase.class);
                                    intent.putExtra("userUidUpdateNote", String.valueOf(note.getIdUser()));
                                    intent.putExtra("title", String.valueOf(note.getTitle()));
                                    intent.putExtra("date", String.valueOf(note.getDate()));
                                    intent.putExtra("time", String.valueOf(note.getTime()));
                                    activity.startActivityForResult(intent, 1);
                                }

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });



    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView, dateTextView, timeTextView;
        ConstraintLayout rowHomeConstraintLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.noteTitleTextView);
            dateTextView = itemView.findViewById(R.id.noteDateTextView);
            timeTextView = itemView.findViewById(R.id.noteTimeTextView);
            rowHomeConstraintLayout = itemView.findViewById(R.id.rowHomeConstraintLayout);

        }
    }

    public void styleColorText(TextView textView) {
        TextPaint paint = textView.getPaint();
        float width = paint.measureText("Tianjin, China");

        Shader textShader = new LinearGradient(0, 0, width, textView.getTextSize(),
                new int[]{
                        Color.parseColor("#7490BB"),
                        Color.parseColor("#2D538C"),
                }, null, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(textShader);
    }
}
