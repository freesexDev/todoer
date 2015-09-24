package io.freesexdev.todoer;

import android.app.ListFragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

/*TODO: check this method:
public void reload() {
    Intent intent = getIntent();
    overridePendingTransition(0, 0);
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    finish();
    overridePendingTransition(0, 0);
    startActivity(intent);}*/

public class HomeFragment extends ListFragment {

    public TaskDBHelper helper;
    private int layout = R.layout.fragment_home;
    private int listItemLayout = R.layout.list_item;
    private LayoutInflater inflater;


    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        new UpdateUi();
        SQLiteDatabase sqlDB = new TaskDBHelper(getActivity()).getWritableDatabase();
        Cursor cursor = sqlDB.query(TaskContract.TABLE,
                new String[]{TaskContract.Columns.TASK},
                null, null, null, null, null);

        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            Log.d("HomeFragment cursor",
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    TaskContract.Columns.TASK)));
        }


        View view = inflater.inflate(layout, container, false);
        return view;
    }

    @Override
    public void onResume() {
        new UpdateUi();
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    class UpdateUi {
        public UpdateUi() {
            helper = new TaskDBHelper(getActivity());
            SQLiteDatabase sqlDB = helper.getReadableDatabase();
            Cursor cursor = sqlDB.query(TaskContract.TABLE,
                    new String[]{TaskContract.Columns._ID, TaskContract.Columns.TASK},
                    null, null, null, null, null);

            ListAdapter listAdapter = new SimpleCursorAdapter(
                    getActivity(),
                    listItemLayout,
                    cursor,
                    new String[]{TaskContract.Columns.TASK},
                    new int[]{R.id.label},
                    0
            );
            setListAdapter(listAdapter);
        }

    }
}
