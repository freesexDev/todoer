package io.freesexdev.todoer.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.freesexdev.todoer.R;
import io.freesexdev.todoer.adapter.TabAdapter;

public class MainFragment extends Fragment {

    private ViewPager pager;
    private TabLayout tablayout;

    public MainFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main, container, false);

        pager = (ViewPager) v.findViewById(R.id.viewPager);
        TabAdapter adapter = new TabAdapter(getFragmentManager());
        pager.setAdapter(adapter);
        tablayout = (TabLayout) v.findViewById(R.id.tabLayout);
        tablayout.setupWithViewPager(pager);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
