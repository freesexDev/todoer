package io.freesexdev.todoer.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import io.freesexdev.todoer.fragment.HomeFragment;
import io.freesexdev.todoer.fragment.TodoListFragment;

public class TabAdapter extends FragmentPagerAdapter {

    String[] tabs;

    public TabAdapter(FragmentManager fm) {
        super(fm);
        tabs = new String[]{
                "Задачи",
                "Не готово"
        };

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return HomeFragment.getInstance();

            case 1:
                return TodoListFragment.getInstance();

            case 2:
                return TodoListFragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabs.length;
    }
}
