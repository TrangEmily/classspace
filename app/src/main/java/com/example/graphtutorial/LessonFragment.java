package com.example.graphtutorial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LessonFragment extends Fragment {
    private static final String USER_NAME = "userName";

    private String mUserName;

    public LessonFragment() {

    }

    public static LessonFragment createInstance(String userName) {
        LessonFragment fragment = new LessonFragment();

        // Add the provided username to the fragment's arguments
        Bundle args = new Bundle();
        args.putString(USER_NAME, userName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserName = getArguments().getString(USER_NAME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View homeView = inflater.inflate(R.layout.fragment_lesson, container, false);

        // If there is a username, replace the "Please sign in" with the username
        if (mUserName != null) {
            TextView userName = homeView.findViewById(R.id.home_page_username_game);
            userName.setText("Hi, ".concat(mUserName));
        }

        homeView.findViewById(R.id.mathImage).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DetailedLessonFragment nextFrag= new DetailedLessonFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        homeView.findViewById(R.id.socialImage).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DetailedLessonFragment nextFrag= new DetailedLessonFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        homeView.findViewById(R.id.englishImage).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DetailedLessonFragment nextFrag= new DetailedLessonFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        return homeView;
    }
}