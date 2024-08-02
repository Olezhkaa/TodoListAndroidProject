package com.example.myapplication1.AppFirebase.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.myapplication1.R;
import com.example.myapplication1.databinding.FragmentCalendarFirebaseBinding;
import com.example.myapplication1.databinding.FragmentHomeFirebaseBinding;

public class CalendarFragmentFirebase extends Fragment {

    private FragmentCalendarFirebaseBinding binding;

    CalendarView calendarView;
    String mDay, nMonth, nYear;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCalendarFirebaseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        calendarView = root.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                mDay = String.valueOf(dayOfMonth);
                if(dayOfMonth < 10) mDay = "0" + mDay;
                nMonth = String.valueOf(month+1);
                if(month < 10) nMonth = "0" + nMonth;
                nYear = String.valueOf(year);

                String selectedDate = mDay + "." + nMonth + "." + nYear;

                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, new HomeFragmentFirebase(selectedDate));
                fragmentTransaction.commit();
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