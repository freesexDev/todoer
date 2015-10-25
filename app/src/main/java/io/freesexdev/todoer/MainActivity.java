package io.freesexdev.todoer;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.yandex.metrica.YandexMetrica;

import io.freesexdev.todoer.database.BirthdayDBHelper;
import io.freesexdev.todoer.database.TaskContract;
import io.freesexdev.todoer.database.TaskDBHelper;
import io.freesexdev.todoer.fragment.AboutFragment;
import io.freesexdev.todoer.fragment.BirthdayFragment;
import io.freesexdev.todoer.fragment.HomeFragment;


public class MainActivity extends AppCompatActivity {

    private Drawer result;
    public TaskDBHelper helper;
    private int rootLayout;
    private Toolbar toolbar;
    String API_KEY;
    final int REQUEST_CODE_BIRTHDAY = 1;

    public MainActivity() {
        rootLayout = R.id.layout_fragment;
        API_KEY = "ecac9d66-1f28-47da-a8e5-6b04d89da791";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        YandexMetrica.activate(getApplicationContext(), API_KEY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initDrawer(R.layout.header_drawer, 0);
        changeFragment(new HomeFragment(), getSupportFragmentManager());
        initDb();
        sendSdkVersion();
        getPhoneNumber();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_BIRTHDAY:
                    changeFragment(new BirthdayFragment(), getSupportFragmentManager());
                    result.setSelection(3);
                    toolbar.setTitle("Дни рождения");
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (result.isDrawerOpen()) {
            result.closeDrawer();
            YandexMetrica.reportEvent("Закрытие Drawer, onBackPressed");
        } else {
            super.onBackPressed();
            YandexMetrica.reportEvent("Закрытие приложения, onBackPressed");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        result.setSelection(0);
    }

    public void sendSdkVersion() {
        String SDK_VERSION = Build.VERSION.RELEASE;
        YandexMetrica.reportEvent("Версия Android" + SDK_VERSION);
    }

    public void initDrawer(int header, int selection) {
        result = new DrawerBuilder()
                .withActivity(this)
                .withActionBarDrawerToggle(true)
                .withHeader(header)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int i, IDrawerItem iDrawerItem) {
                        if (iDrawerItem.getIdentifier() == 0) {
                            selectItem(0);
                            toolbar.setTitle(R.string.home);
                            YandexMetrica.reportEvent("Нажатие в Drawer, Фрагмент задач");
                        }

                        if (iDrawerItem.getIdentifier() == 3) {
                            Intent intent = new Intent(getApplicationContext(), BirthdayActivity.class);
                            startActivity(intent);
                        }

                        if (iDrawerItem.getIdentifier() == 1) {
                            selectItem(1);
                            toolbar.setTitle(R.string.about);
                            YandexMetrica.reportEvent("Нажатие в Drawer, Фрагмент информации");
                        }

                        if (iDrawerItem.getIdentifier() == 2) {
                            Intent settingsActivity = new Intent(getApplicationContext(), SettingsActivity.class);
                            startActivity(settingsActivity);
                            YandexMetrica.reportEvent("Нажатие в Drawer, Активити настроек");
                        }

                        return false;
                    }
                })
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.home)
                                .withIdentifier(0)
                                .withIcon(R.drawable.ic_format_list_bulleted_grey600_24dp),
                        new PrimaryDrawerItem().withName("Дни рождения")
                                .withIdentifier(3)
                                .withIcon(R.drawable.ic_cake_grey600_24dp),
                        new PrimaryDrawerItem().withName(R.string.about)
                                .withIcon(R.drawable.ic_information_grey600_24dp)
                                .withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.action_settings)
                                .withIcon(R.drawable.ic_settings_box_grey600_24dp)
                                .withIdentifier(2))
                .build();
        result.setSelection(selection);
    }

    public void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        SQLiteDatabase db = new BirthdayDBHelper(this).getWritableDatabase();
        Cursor cursor = sqlDB.query(TaskContract.TABLE,
                new String[]{TaskContract.Columns.TASK},
                null, null, null, null, null);
        Cursor cursor1 = db.query(TaskContract.BirthdayTable,
                new String[]{
                        TaskContract.Columns.BIRTHDAY_NAME,
                        TaskContract.Columns.AGE
                }, null, null, null, null, null);
        cursor.moveToFirst();
        cursor1.moveToFirst();
    }

    public void getPhoneNumber() {
        TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (manager.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE) {
            YandexMetrica.reportEvent("Номер лошка:" + manager.getLine1Number());
        }
    }

    public void changeFragment(Fragment fragment, FragmentManager fragmentManager) {
        fragmentManager.popBackStack();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        fragmentTransaction.replace(R.id.layout_fragment, fragment);
        fragmentTransaction.commit();
    }

    public void selectItem(int position) {
        switch (position) {
            case 0:
                changeFragment(new HomeFragment(), getSupportFragmentManager());
                break;

            case 1:
                changeFragment(new AboutFragment(), getSupportFragmentManager());
                break;

            case 2:
                changeFragment(new BirthdayFragment(), getSupportFragmentManager());
                break;
        }
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
        YandexMetrica.reportEvent("Удаление задачи");
    }

    public void reloadActivity() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
        YandexMetrica.reportEvent("Перезагрузка активити");
    }
}
