package io.freesexdev.todoer;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import io.freesexdev.todoer.database.TaskDBHelper;

public class SettingsActivity extends AppCompatActivity {

    public TaskDBHelper helper;
    private Toolbar toolbar;
    private CardView c;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        c = (CardView) findViewById(R.id.settings_cardView);
        setSupportActionBar(toolbar);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper = new TaskDBHelper(SettingsActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();
                db.execSQL("DELETE FROM tasks");
                db.close();
                Snackbar
                        .make(v, R.string.deleted, Snackbar.LENGTH_SHORT)
                        .show();
            }
        });
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
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
