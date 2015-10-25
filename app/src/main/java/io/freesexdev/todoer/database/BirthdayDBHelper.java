package io.freesexdev.todoer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BirthdayDBHelper extends SQLiteOpenHelper {

    public BirthdayDBHelper(Context context) {
        super(context, TaskContract.BIRTHDAY_DB_NAME, null, TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String sqlQuery =
                String.format("CREATE TABLE %s (" +
                                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "%s TEXT, " + "%s TEXT, " + "%s TEXT)", TaskContract.BirthdayTable,
                        TaskContract.Columns.BIRTHDAY_NAME, TaskContract.Columns.AGE, TaskContract.Columns.DATE);

        Log.d("TaskDBHelper", "Query to form table: " + sqlQuery);
        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TaskContract.BirthdayTable);
        onCreate(sqlDB);
    }
}
