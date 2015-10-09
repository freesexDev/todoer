package io.freesexdev.todoer.fragment;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

import com.melnykov.fab.FloatingActionButton;
import com.yandex.metrica.YandexMetrica;

import io.freesexdev.todoer.R;
import io.freesexdev.todoer.database.TaskContract;
import io.freesexdev.todoer.database.TaskDBHelper;

public class HomeFragment extends ListFragment {

    String API_KEY = "ecac9d66-1f28-47da-a8e5-6b04d89da791";
    private FloatingActionButton fab;
    public TaskDBHelper helper;
    private int LAYOUT = R.layout.fragment_home;
    private int LIST_ITEM_LAYOUT = R.layout.list_item;

    public static HomeFragment getInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        UpdateUi();
        initDb();
        YandexMetrica.activate(getActivity(), API_KEY);
        YandexMetrica.reportEvent("OnCreateView HomeFragment");
        View view = inflater.inflate(LAYOUT, container, false);
        fab = (FloatingActionButton) view.findViewById(R.id.floatbutton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme_Dialog));
                builder.setTitle(R.string.add_task);
                builder.setMessage(R.string.add_desc);
                final EditText inputField = new EditText(getActivity());
                builder.setView(inputField);
                builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String task = inputField.getText().toString();
                        Log.d("MainActivity", task);
                        helper = new TaskDBHelper(getActivity());
                        SQLiteDatabase db = helper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.clear();
                        values.put(TaskContract.Columns.TASK, task);
                        db.insertWithOnConflict(TaskContract.TABLE, null, values,
                                SQLiteDatabase.CONFLICT_IGNORE);
                        reloadActivity();
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.create().show();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        UpdateUi();
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void initDb() {
        SQLiteDatabase sqlDB = new TaskDBHelper(getActivity()).getWritableDatabase();
        Cursor cursor = sqlDB.query(TaskContract.TABLE,
                new String[]{TaskContract.Columns.TASK},
                null, null, null, null, null);
        cursor.moveToFirst();
    }

    public void UpdateUi() {
        helper = new TaskDBHelper(getActivity());
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(TaskContract.TABLE,
                new String[]{TaskContract.Columns._ID, TaskContract.Columns.TASK},
                null, null, null, null, null);
        ListAdapter listAdapter = new SimpleCursorAdapter(
                getActivity(),
                LIST_ITEM_LAYOUT,
                cursor,
                new String[]{TaskContract.Columns.TASK},
                new int[]{R.id.label},
                0
        );
        setListAdapter(listAdapter);
    }

    public void reloadActivity() {
        Intent intent = getActivity().getIntent();
        getActivity().overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        getActivity().finish();
        getActivity().overridePendingTransition(0, 0);
        startActivity(intent);
    }

}
