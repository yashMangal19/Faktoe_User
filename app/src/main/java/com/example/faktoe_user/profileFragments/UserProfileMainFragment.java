package com.example.faktoe_user.profileFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.faktoe_user.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfileMainFragment} factory method to
 * create an instance of this fragment.
 */
public class UserProfileMainFragment extends Fragment {
    View view;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_user_profile_main, container, false);
        return view;
    }
}