package com.example.myapplication1.AppFirebase.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication1.R;
import com.example.myapplication1.databinding.FragmentFriendsFirebaseBinding;
import com.example.myapplication1.databinding.FragmentHomeFirebaseBinding;
import com.example.myapplication1.databinding.FragmentProfileFirebaseBinding;

public class ProfileFragmentFirebase extends Fragment {

    private FragmentProfileFirebaseBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileFirebaseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textProfile;

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}