package com.example.myapplication1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication1.R;

import java.util.ArrayList;

public class CustomAdapterHome extends RecyclerView.Adapter<CustomAdapterHome.MyViewHolder> {

    private Context context;
    private ArrayList note_id, note_title, note_time;

    public CustomAdapterHome(Context context, ArrayList note_title, ArrayList note_time) {
        this.context = context;
        ArrayList noteID = new ArrayList();
        for(int i = 1; i<=note_title.size(); i++) {
            noteID.add(String.valueOf(i));
        }
        this.note_id = noteID;
        this.note_title = note_title;
        this.note_time = note_time;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView noteIdTextView, noteTitleTextView, noteTimeTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            noteIdTextView = itemView.findViewById(R.id.noteIdTextView);
            noteTitleTextView = itemView.findViewById(R.id.noteTitleTextView);
            noteTimeTextView = itemView.findViewById(R.id.noteTimeTextView);
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
        holder.noteIdTextView.setText(String.valueOf(note_id.get(position)));
        holder.noteTitleTextView.setText(String.valueOf(note_title.get(position)));
        holder.noteTimeTextView.setText(String.valueOf(note_time.get(position)));

    }

    @Override
    public int getItemCount() {
        return note_id.size();
    }
}
