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

import com.example.myapplication1.R;
import com.example.myapplication1.UpdateNoteActivity;

import java.util.ArrayList;

public class CustomAdapterHome extends RecyclerView.Adapter<CustomAdapterHome.MyViewHolder> {

    private Context context;
    Activity activity;
    private ArrayList noteID, note_id, note_title, note_date, note_time;

    public CustomAdapterHome(Activity activity, Context context, ArrayList note_id, ArrayList note_title, ArrayList note_date, ArrayList note_time) {
        this.activity = activity;
        this.context = context;
        ArrayList noteID = new ArrayList();
        for(int i = 1; i<=note_title.size(); i++) {
            noteID.add(String.valueOf(i));
        }
        this.noteID = noteID;
        this.note_id = note_id;
        this.note_title = note_title;
        this.note_date = note_date;
        this.note_time = note_time;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView noteIdTextView, noteTitleTextView, noteDateTextView, noteTimeTextView;
        ConstraintLayout rowHomeConstraintLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            noteIdTextView = itemView.findViewById(R.id.noteIdTextView);
            noteTitleTextView = itemView.findViewById(R.id.noteTitleTextView);
            noteDateTextView = itemView.findViewById(R.id.noteDateTextView);
            noteTimeTextView = itemView.findViewById(R.id.noteTimeTextView);
            rowHomeConstraintLayout = itemView.findViewById(R.id.rowHomeConstraintLayout);
        }
    }
    @NonNull
    @Override
    public CustomAdapterHome.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row_home, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterHome.MyViewHolder holder, int position) {
        holder.noteIdTextView.setText(String.valueOf(noteID.get(position)));
        holder.noteTitleTextView.setText(String.valueOf(note_title.get(position)));
        holder.noteDateTextView.setText(String.valueOf(note_date.get(position)));
        holder.noteTimeTextView.setText(String.valueOf(note_time.get(position)));
        holder.rowHomeConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateNoteActivity.class);
                intent.putExtra("id", String.valueOf(note_id.get(holder.getPosition())));
                intent.putExtra("title", String.valueOf(note_title.get(holder.getPosition())));
                intent.putExtra("date", String.valueOf(note_date.get(holder.getPosition())));
                intent.putExtra("time", String.valueOf(note_time.get(holder.getPosition())));
                activity.startActivityForResult(intent, 1);

            }
        });

    }

    @Override
    public int getItemCount() {
        return note_title.size();
    }
}
