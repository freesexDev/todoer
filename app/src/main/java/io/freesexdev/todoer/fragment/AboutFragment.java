package io.freesexdev.todoer.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import io.freesexdev.todoer.R;

public class AboutFragment extends Fragment {

    private int layout = R.layout.fragment_about;

    public AboutFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(layout, container, false);
        ImageView i = (ImageView) view.findViewById(R.id.logo);
        i.setImageResource(R.drawable.web_hi_res_512);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
