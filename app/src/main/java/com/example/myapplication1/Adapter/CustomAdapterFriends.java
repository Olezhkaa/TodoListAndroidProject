package com.example.myapplication1.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication1.CalendarFragment;
import com.example.myapplication1.HomeFragment;
import com.example.myapplication1.MainAndBottomNavigation;
import com.example.myapplication1.R;

import java.util.ArrayList;
import java.util.Calendar;

public class CustomAdapterFriends extends RecyclerView.Adapter<CustomAdapterFriends.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList user_id, user_username, user_email;

    public CustomAdapterFriends(Context context, ArrayList user_id, ArrayList user_username, ArrayList user_email) {
        this.context = context;
        this.user_id = user_id;
        this.user_username = user_username;
        this.user_email = user_email;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView usernameTextView, emailTextView;
        ConstraintLayout rowFriendsConstraintLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            rowFriendsConstraintLayout = itemView.findViewById(R.id.rowFriendsConstraintLayout);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row_friends, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.usernameTextView.setText(String.valueOf(user_username.get(position)));
        holder.emailTextView.setText(String.valueOf(user_email.get(position)));
        holder.rowFriendsConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = String.valueOf(user_id.get(holder.getPosition()));
                int userIdInt = Integer.parseInt(userID);

                Calendar calendar = Calendar.getInstance();
                String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                if(Integer.parseInt(day) < 10) day = "0" + day;
                String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
                if (Integer.parseInt(month) < 10) month = "0" + month;
                String year = String.valueOf(calendar.get(Calendar.YEAR));
                String date = day + "." + month + "." + year;

                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, new HomeFragment(userIdInt, date));
                fragmentTransaction.commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return user_username.size();
    }


}
