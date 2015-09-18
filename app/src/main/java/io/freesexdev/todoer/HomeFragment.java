package io.freesexdev.todoer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.freesexdev.todoer.R;


public class HomeFragment extends Fragment {

    private int layout = R.layout.fragment_home;
    private LayoutInflater inflater;

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(layout, container, false);

        return view;
    }
}
