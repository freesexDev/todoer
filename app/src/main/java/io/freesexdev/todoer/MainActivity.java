package io.freesexdev.todoer;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import io.freesexdev.todoer.database.TaskContract;
import io.freesexdev.todoer.database.TaskDBHelper;
import io.freesexdev.todoer.fragment.AboutFragment;
import io.freesexdev.todoer.fragment.MainFragment;


public class MainActivity extends AppCompatActivity {

    private ViewPager pager;
    private FloatingActionButton fab;
    private TabLayout tablayout;
    private Drawer result;
    public TaskDBHelper helper;
    public EditText inputField;
    private View coordinatorLayout;
    private String TAG = "TodoLogs";
    private int rootLayout = R.id.layout_fragment;
    private Toolbar toolbar;

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

        changeFragment(new MainFragment(), getSupportFragmentManager());

        coordinatorLayout = findViewById(R.id.coordinator);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabClick();
            }
        });

        initToolbar();

        result = new DrawerBuilder()
                .withActivity(this)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.header_drawer)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int i, IDrawerItem iDrawerItem) {
                        if (iDrawerItem.getIdentifier() == 0) {
                            selectItem(0);
                            toolbar.setTitle(R.string.home);
                            fab.setVisibility(View.VISIBLE);
                        }

                        if (iDrawerItem.getIdentifier() == 1) {
                            fab.setVisibility(View.INVISIBLE);
                            selectItem(1);
                            toolbar.setTitle(R.string.about);
                        }

                        if (iDrawerItem.getIdentifier() == 2) {
                            Intent settingsActivity = new Intent(getApplicationContext(), SettingsActivity.class);
                            startActivity(settingsActivity);
                        }
                        return false;
                    }
                })
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.home)
                                .withIdentifier(0)
                                .withIcon(R.drawable.ic_home_grey600_24dp),
                        new PrimaryDrawerItem().withName(R.string.about)
                                .withIcon(R.drawable.ic_information_outline_grey600_24dp)
                                .withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.action_settings)
                                .withIcon(R.drawable.ic_settings_grey600_24dp)
                                .withIdentifier(2)
                )

                .build();

        result.setSelection(0);

        initDb();

    }


    public void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(R.string.home);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.openDrawer();
            }
        });
    }

    public void initDb() {
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
    }


    @Override
    public void onBackPressed() {
        result.closeDrawer();
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
                changeFragment(new MainFragment(), getSupportFragmentManager());
                break;

            case 1:
                changeFragment(new AboutFragment(), getSupportFragmentManager());
                break;
        }

    }

    public void fabClick() {
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

    public void deleteTask(View view) {
        View v = (View) view.getParent();
        TextView taskTextView = (TextView) v.findViewById(R.id.label);
        String task = taskTextView.getText().toString();

        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                TaskContract.TABLE,
                TaskContract.Columns.TASK,
                task);


        helper = new TaskDBHelper(this);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);
        recreate();


    }


}
