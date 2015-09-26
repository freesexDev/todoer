package io.freesexdev.todoer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.freesexdev.todoer.R;


public class TodoListFragment extends Fragment {

    public static TodoListFragment getInstance() {
        Bundle args = new Bundle();

        TodoListFragment fragment = new TodoListFragment();
        fragment.setArguments(args);

        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_todos, container, false);

        return view;
    }
}
