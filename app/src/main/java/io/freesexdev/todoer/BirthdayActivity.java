package io.freesexdev.todoer;

import android.app.SearchManager;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.OnClickWrapper;
import com.yandex.metrica.YandexMetrica;

import io.freesexdev.todoer.database.BirthdayDBHelper;
import io.freesexdev.todoer.database.TaskContract;
import io.freesexdev.todoer.fragment.BirthdayFragment;

public class BirthdayActivity extends AppCompatActivity {

    private String TAG = "BirthdayActivity";
    private TextView nameTextView;
    private TextView ageTextView;
    private TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday);
        Toolbar toolbar = (Toolbar) findViewById(R.id.birthday_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Intent main = new Intent(getApplication(), MainActivity.class);
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(main);
            }
        });
        setSupportActionBar(toolbar);
        changeFragment(new BirthdayFragment(), getSupportFragmentManager());
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void changeFragment(Fragment fragment, FragmentManager fragmentManager) {
        fragmentManager.popBackStack();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        fragmentTransaction.replace(R.id.birthday_container, fragment);
        fragmentTransaction.commit();
    }

    public void delete(final View v) {
        Log.i(TAG, "delete");
        final OnClickWrapper onClickWrapper = new OnClickWrapper("superactivitytoast", new SuperToast.OnClickListener() {
            @Override
            public void onClick(View view, Parcelable token) {
                deleteDay(v);
            }
        });
        SuperActivityToast toast = new SuperActivityToast(BirthdayActivity.this, SuperToast.Type.BUTTON);
        toast.setDuration(SuperToast.Duration.EXTRA_LONG);
        toast.setText("Удалить день рождения?");
        toast.setButtonIcon(android.R.color.transparent, "Да");
        toast.setOnClickWrapper(onClickWrapper);
        toast.show();

    }

    public void deleteDay(View view) {
        View parent = (View) view.getParent();
        initDeleteTextViews(parent);
        String name = nameTextView.getText().toString();
        String age = ageTextView.getText().toString();
        String dateT = date.getText().toString();
        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                TaskContract.BirthdayTable,
                TaskContract.Columns.BIRTHDAY_NAME,
                name);
        String sql1 = String.format("DELETE FROM %S WHERE %S = '%S'",
                TaskContract.BirthdayTable,
                TaskContract.Columns.AGE,
                age);
        String sql2 = String.format("DELETE FROM %S WHERE %S = '%S'",
                TaskContract.BirthdayTable,
                TaskContract.Columns.BIRTHDAY_NAME,
                dateT);
        BirthdayDBHelper helper = new BirthdayDBHelper(this);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);
        sqlDB.execSQL(sql1);
        sqlDB.execSQL(sql2);
        reloadActivity();
        YandexMetrica.reportEvent("Удаление дня рождения");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchManager manager = (SearchManager) BirthdayActivity.this.getSystemService(SEARCH_SERVICE);
        SearchView searchView = null;
        if (search != null) {
            searchView = (SearchView) search.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(manager.getSearchableInfo(BirthdayActivity.this.getComponentName()));
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void initDeleteTextViews(View v) {
        nameTextView = (TextView) v.findViewById(R.id.birthday_name);
        ageTextView = (TextView) v.findViewById(R.id.birthday_age);
        date = (TextView) v.findViewById(R.id.dateTextView);
    }

    public void reloadActivity() {
        Intent i = new Intent(this, BirthdayActivity.class);
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(i);
    }
}
