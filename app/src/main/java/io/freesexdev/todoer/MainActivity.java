package io.freesexdev.todoer;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
    //private FloatingActionButton fab;
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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeFragment(new MainFragment(), getSupportFragmentManager());
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
                        }

                        if (iDrawerItem.getIdentifier() == 1) {
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
                                .withIcon(R.drawable.ic_format_list_bulleted_grey600_24dp),
                        new PrimaryDrawerItem().withName(R.string.about)
                                .withIcon(R.drawable.ic_information_grey600_24dp)
                                .withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.action_settings)
                                .withIcon(R.drawable.ic_settings_box_grey600_24dp)
                                .withIdentifier(2))
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
    }


    @Override
    public void onBackPressed() {
        if (result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
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
        reloadActivity();
    }

    public void reloadActivity() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }
}
