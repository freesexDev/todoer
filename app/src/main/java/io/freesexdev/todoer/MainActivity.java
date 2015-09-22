package io.freesexdev.todoer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    private PreferenceManager pm;
    private SharedPreferences.Editor editor;
    public TaskDBHelper helper;
    public EditText inputField;
    private View coordinatorLayout;
    private String TAG = "TodoLogs";
    private int rootLayout = R.id.layout_fragment;
    public TextView textEmpty;
    private SharedPreferences s;
    String spName = "visited";
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textEmpty = (TextView) findViewById(R.id.text_empt);
        boolean exists = (new File("//data//data//\"+this.getPackageName()+\"//shared_prefs//feedbackpref.xml").exists());

        fab = (FloatingActionButton) findViewById(R.id.fab);

        if (exists) {
            textEmpty.setVisibility(View.VISIBLE);
        }

        changeFragment(new io.freesexdev.todoer.HomeFragment(), getFragmentManager());
        Log.i(TAG, "onCreate");
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(R.string.home);
        coordinatorLayout = findViewById(R.id.coordinator);

        new Drawer()
                .withActivity(this)
                .withActionBarDrawerToggle(true)
                .withToolbar(toolbar)
                .withHeader(R.layout.header_drawer)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {

                        if (iDrawerItem.getIdentifier() == 1) {
                            selectItem(0);
                            toolbar.setTitle(R.string.home);
                            fab.setVisibility(View.VISIBLE);
                        }

                        if (iDrawerItem.getIdentifier() == 2) {
                            selectItem(1);
                            toolbar.setTitle(R.string.all);
                            fab.setVisibility(View.INVISIBLE);
                        }

                        if (iDrawerItem.getIdentifier() == 4) {
                            Intent settingsActivity = new Intent(getApplicationContext(), SettingsActivity.class);
                            startActivity(settingsActivity);
                        }

                        if (iDrawerItem.getIdentifier() == 3) {
                            selectItem(2);
                            toolbar.setTitle(R.string.about);
                            fab.setVisibility(View.INVISIBLE);
                        }
                    }
                })
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.home)
                                .withIdentifier(1)
                                .withIcon(GoogleMaterial.Icon.gmd_home)
                                .setEnabled(true),
                        new PrimaryDrawerItem().withName(R.string.all)
                                .withIdentifier(2)
                                .withIcon(GoogleMaterial.Icon.gmd_receipt),
                        new PrimaryDrawerItem().withName(R.string.about)
                                .withIcon(GoogleMaterial.Icon.gmd_info)
                                .withIdentifier(3),
                        new SecondaryDrawerItem().withName(R.string.action_settings)
                                .withIcon(GoogleMaterial.Icon.gmd_settings)
                                .withIdentifier(4)
                )

                .build();

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
    protected void onRestart() {
        super.onRestart();
        reload();
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

            case 2:
                changeFragment(new AboutFragment(), getFragmentManager());
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
                long time = System.currentTimeMillis();
                Log.d("MainActivity", task);


                helper = new TaskDBHelper(MainActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.clear();
                values.put(TaskContract.Columns.TASK, task);
                db.insertWithOnConflict(TaskContract.TABLE, null, values,
                        SQLiteDatabase.CONFLICT_IGNORE);

                reload();

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
        reload();

    }

    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }


    public void visitChecker() {

    }


}
