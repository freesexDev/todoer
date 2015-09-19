package io.freesexdev.todoer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;


public class MainActivity extends AppCompatActivity {

    public TaskDBHelper helper;
    public EditText inputField;
    private View coordinatorLayout;
    private String TAG = "TodoLogs";
    private int rootLayout = R.id.layout_fragment;

    @Override
    protected void onRestart() {
        super.onRestart();
        Snackbar
                .make(coordinatorLayout, R.string.added, Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeFragment(new io.freesexdev.todoer.HomeFragment(), getFragmentManager());
        Log.i(TAG, "onCreate");
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(R.string.home);
        coordinatorLayout = findViewById(R.id.coordinator);

        SQLiteDatabase sqlDB = new TaskDBHelper(this).getWritableDatabase();
        Cursor cursor = sqlDB.query(TaskContract.TABLE,
                new String[]{TaskContract.Columns.TASK},
                null, null, null, null, null);

        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            Log.d("MainActivity cursor",
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    TaskContract.Columns.TASK)));
        }

        new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.header_drawer)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {

                        if (iDrawerItem.getIdentifier() == 1) {
                            selectItem(0);
                            toolbar.setTitle(R.string.home);
                        }
                        if (iDrawerItem.getIdentifier() == 2) {
                            selectItem(1);
                            toolbar.setTitle(R.string.all);
                        }
                    }
                })
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.home)
                                .withIdentifier(1)
                                .setEnabled(true),
                        new PrimaryDrawerItem().withName(R.string.all)
                                .withIdentifier(2)
                )

                .build();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void changeFragment(Fragment fragment, FragmentManager fragmentManager) {

        fragmentManager.popBackStack();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        fragmentTransaction.replace(rootLayout, fragment);
        fragmentTransaction.commit();

    }

    public void selectItem(int position) {

        switch (position) {
            case 0:
                changeFragment(new io.freesexdev.todoer.HomeFragment(), getFragmentManager());
                break;

            case 1:
                changeFragment(new io.freesexdev.todoer.TodoListFragment(), getFragmentManager());
                break;
        }

    }

    public void fabClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme_Dialog));
        builder.setTitle(R.string.add_task);
        builder.setMessage(R.string.add_desc);
        inputField = new EditText(this);
        builder.setView(inputField);
        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String task = inputField.getText().toString();
                Log.d("MainActivity", task);


                helper = new TaskDBHelper(MainActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.clear();
                values.put(TaskContract.Columns.TASK, task);

                db.insertWithOnConflict(TaskContract.TABLE, null, values,
                        SQLiteDatabase.CONFLICT_IGNORE);

                recreate();

            }
        });

        builder.setNegativeButton(R.string.cancel, null);

        builder.create().show();

    }


}
