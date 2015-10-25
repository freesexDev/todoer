package io.freesexdev.todoer.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.melnykov.fab.FloatingActionButton;
import com.yandex.metrica.YandexMetrica;

import io.freesexdev.todoer.BirthdayActivity;
import io.freesexdev.todoer.R;
import io.freesexdev.todoer.database.BirthdayDBHelper;
import io.freesexdev.todoer.database.TaskContract;

public class BirthdayFragment extends ListFragment {

    private View layout;
    private String[] items;
    private int LIST_ITEM_LAYOUT = R.layout.birthday_list_item;
    private FloatingActionButton fab;
    private BirthdayDBHelper helper;
    private ListView listView;
    final int REQUEST_CODE_BIRTHDAY = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_birthday, container, false);
        items = new String[]{
                TaskContract.Columns.BIRTHDAY_NAME,
                TaskContract.Columns.AGE,
                TaskContract.Columns.DATE
        };
        listView = (ListView) layout.findViewById(android.R.id.list);
        initFab();
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(100);
                return false;
            }
        });
        initDb();
        updateUi(LIST_ITEM_LAYOUT, items);
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUi(LIST_ITEM_LAYOUT, items);
    }

    public void initFab() {
        try {
            final View linear = getLayoutInflater(getArguments()).inflate(R.layout.dialog_birthday, null, true);
            final DatePicker picker = (DatePicker) linear.findViewById(R.id.dPicker);
            final EditText name = (EditText) linear.findViewById(R.id.textInput);
            picker.setCalendarViewShown(false);
            fab = (FloatingActionButton) layout.findViewById(R.id.birthday_fab);
            fab.attachToListView(listView);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme_Dialog));
                    builder.setTitle(R.string.add_task);
                    builder.setMessage(R.string.add_desc);
                    builder.setView(linear);
                    builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int year = 2015 - picker.getYear();
                            int normalYear = picker.getYear();
                            int month = picker.getMonth();
                            int day = picker.getDayOfMonth();
                            String m;
                            String d;
                            if (picker.getMonth() < 10) {
                                m = "0" + Integer.toString(month);
                            } else {
                                m = Integer.toString(month);
                            }
                            if (picker.getDayOfMonth() < 10) {
                                d = "0" + Integer.toString(day);
                            } else {
                                d = Integer.toString(day);
                            }
                            String y = Integer.toString(year);
                            String birthdayName = name.getText().toString();
                            String date = "Дата рождения: " + d + "." + m + "." + normalYear;
                            Log.d("BirthdayFragment", "onClick() returned: " + birthdayName + " " + normalYear);
                            addBirthday(birthdayName, y, date);
                            updateUi(LIST_ITEM_LAYOUT, items);
                            reloadActivity();
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updateUi(LIST_ITEM_LAYOUT, items);
                            reloadActivity();
                        }
                    });
                    builder.create().show();
                }
            });
        } catch (Throwable throwable) {
            YandexMetrica.reportError("Ошибка при создании FloatingActionButton", throwable);
        }
    }

    public void initDb() {
        try {
            SQLiteDatabase sqlDB = new BirthdayDBHelper(getActivity()).getWritableDatabase();
            Cursor cursor = sqlDB.query(TaskContract.BirthdayTable,
                    new String[]{TaskContract.Columns.BIRTHDAY_NAME, TaskContract.Columns.AGE, TaskContract.Columns.DATE},
                    null, null, null, null, null);
            cursor.moveToFirst();
        } catch (Throwable t) {
            YandexMetrica.reportError("Дни рождения, Ошибка в DataBase", t);
        }
    }

    public void addBirthday(String name, String age, String date) {
        helper = new BirthdayDBHelper(getActivity());
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.clear();
        values.put(TaskContract.Columns.BIRTHDAY_NAME, name);
        values.put(TaskContract.Columns.AGE, age);
        values.put(TaskContract.Columns.DATE, date);
        db.insertWithOnConflict(TaskContract.BirthdayTable, null, values,
                SQLiteDatabase.CONFLICT_IGNORE);
    }

    public final void updateUi(int itemLayout, String[] titles) {
        helper = new BirthdayDBHelper(getActivity());
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(TaskContract.BirthdayTable,
                new String[]{
                        TaskContract.Columns._ID,
                        TaskContract.Columns.BIRTHDAY_NAME,
                        TaskContract.Columns.AGE,
                        TaskContract.Columns.DATE
                },
                null, null, null, null, null);
        ListAdapter listAdapter = new SimpleCursorAdapter(
                getActivity(),
                itemLayout,
                cursor,
                titles,
                new int[]{R.id.birthday_name, R.id.birthday_age, R.id.dateTextView},
                0);
        setListAdapter(listAdapter);
        YandexMetrica.reportEvent("Обновление списка");
    }

    public final void deleteBirthday(View l) {

    }

    public void reloadActivity() {
        Intent i = new Intent(getActivity(), BirthdayActivity.class);
        Intent intent = getActivity().getIntent();
        getActivity().overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        getActivity().finish();
        getActivity().overridePendingTransition(0, 0);
        startActivity(i);
    }

}
