package com.example.myapplication1.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication1.Models.Note;
import com.example.myapplication1.R;
import com.example.myapplication1.AppFirebase.UpdateNoteFirebase;

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

        holder.rowHomeConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateNoteFirebase.class);
                intent.putExtra("title", String.valueOf(note.getTitle()));
                intent.putExtra("date", String.valueOf(note.getDate()));
                intent.putExtra("time", String.valueOf(note.getTime()));
                activity.startActivityForResult(intent, 1);
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
}
