package io.freesexdev.todoer;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class TodoListFragment extends ListFragment {

    private LayoutInflater inflater;

    public TodoListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View wewe = inflater.inflate(R.layout.fragment_todos, container, false);

        return wewe;
    }
}
