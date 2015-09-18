package io.freesexdev.todoer;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    private EditText name, description;
    public String na, de;
    public static final String APP_PREFERENCES = "todolist";
    private SharedPreferences database;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        //Get SharedPreferences
        database = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        editor = database.edit();
        //Work with EditTexts, and Strings
        name = (EditText) findViewById(R.id.name_editText);
        description = (EditText) findViewById(R.id.description_editText);
        na = null;

    }

    public void addRemind(View v) {
        na = name.getText().toString();
        de = description.getText().toString();

        editor.putString("name", na);
        editor.putString("description", de);
        editor.apply();
        editor.commit();
    }

    public void cancelRemind(View v) {
        finish();
    }
}
