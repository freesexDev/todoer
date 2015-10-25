package io.freesexdev.todoer.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.yandex.metrica.YandexMetrica;

import io.freesexdev.todoer.database.TaskContract;
import io.freesexdev.todoer.database.TaskDBHelper;

import static io.freesexdev.todoer.R.id;
import static io.freesexdev.todoer.R.layout;
import static io.freesexdev.todoer.R.string;
import static io.freesexdev.todoer.R.style;

public class HomeFragment extends ListFragment {

    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private FloatingActionButton fab;
    public TaskDBHelper helper;
    private int LAYOUT;
    private int LIST_ITEM_LAYOUT;

    public HomeFragment() {
        LAYOUT = layout.fragment_home;
        LIST_ITEM_LAYOUT = layout.list_item;
    }

    public final HomeFragment updateUI() {
        HomeFragment fragment = new HomeFragment();
        updateUi();
        return fragment;
    }

    public static HomeFragment getInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private TextView emptyTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.w("HomeFragment", "create");
        try {
            View view = inflater.inflate(LAYOUT, container, false);
            emptyTextView = (TextView) view.findViewById(id.emptyTextView);
            ListView list = (ListView) view.findViewById(android.R.id.list);
            updateUi();
            initDb();
            initSharedPreferences();
            initFab(view, list);
            YandexMetrica.reportEvent("OnCreateView HomeFragment");
            return view;
        } catch (Throwable t) {
            YandexMetrica.reportError("Ошибка в OnCreateView", t);
        }
        return null;
    }

    public void initFab(View view, ListView listView) {
        try {
            fab = (FloatingActionButton) view.findViewById(id.floatbutton);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YandexMetrica.reportEvent("нажатие на FAB");
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), style.AppTheme_Dialog));
                    builder.setTitle(string.add_task);
                    builder.setMessage(string.add_desc);
                    final EditText inputField = new EditText(getActivity());
                    builder.setView(inputField);
                    builder.setPositiveButton(string.add, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String task = inputField.getText().toString();
                            if (task.equals(null)) {
                                Toast.makeText(getActivity(), string.emptyString, Toast.LENGTH_SHORT).show();
                            } else {
                                helper = new TaskDBHelper(getActivity());
                                SQLiteDatabase db = helper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.clear();
                                values.put(TaskContract.Columns.TASK, task);
                                db.insertWithOnConflict(TaskContract.TABLE, null, values,
                                        SQLiteDatabase.CONFLICT_IGNORE);
                                updateUi();
                                YandexMetrica.reportEvent("Добавлено напоминание:" + task);
                            }
                        }
                    });
                    builder.setNegativeButton(string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            YandexMetrica.reportEvent("Добавление отменено");
                        }
                    });
                    builder.create().show();
                }
            });
            fab.attachToListView(listView);
        } catch (Throwable throwable) {
            YandexMetrica.reportError("Ошибка в Fab, HomeFragment", throwable);
        }
    }

    @Override
    public void onResume() {
        initDb();
        updateUi();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        initDb();
        updateUi();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        try {
            super.onViewCreated(view, savedInstanceState);
            initDb();
            updateUi();
        } catch (Throwable t) {
            YandexMetrica.reportError("Ошибка в OnViewCreated", t);
        }
    }

    public void initDb() {
        try {
            SQLiteDatabase sqlDB = new TaskDBHelper(getActivity()).getWritableDatabase();
            Cursor cursor = sqlDB.query(TaskContract.TABLE,
                    new String[]{TaskContract.Columns.TASK},
                    null, null, null, null, null);
            cursor.moveToFirst();
        } catch (Throwable t) {
            YandexMetrica.reportError("Ошибка в DataBase", t);
        }
    }

    public final void updateUi() {
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
                new int[]{id.label},
                0
        );
        setListAdapter(listAdapter);
        if (listAdapter.isEmpty()) {
            emptyTextView.setVisibility(View.VISIBLE);
            YandexMetrica.reportEvent("Адаптер пуст");
        } else {
            emptyTextView.setVisibility(View.INVISIBLE);
            YandexMetrica.reportEvent("Адаптер с сожержимым");
        }
        YandexMetrica.reportEvent("Обновление списка");
    }

    public void reloadActivity() {
        Intent intent = getActivity().getIntent();
        getActivity().overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        getActivity().finish();
        getActivity().overridePendingTransition(0, 0);
        startActivity(intent);
    }

    public void initSharedPreferences() {
        settings = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        editor = settings.edit();
    }
}
