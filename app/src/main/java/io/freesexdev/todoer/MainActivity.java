package io.freesexdev.todoer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;


public class MainActivity extends AppCompatActivity {

    private View coordinatorLayout;
    private String TAG = "TodoLogs";
    private int rootLayout = R.id.layout_fragment;

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
        Intent i = new Intent(this, AddActivity.class);
        startActivity(i);
    }

    public void changeListFragment(ListFragment fragment, FragmentManager fragmentManager) {

        fragmentManager.popBackStack();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        fragmentTransaction.replace(rootLayout, fragment);
        fragmentTransaction.commit();

    }


}
