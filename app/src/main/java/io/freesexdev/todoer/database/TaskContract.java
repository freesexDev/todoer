package io.freesexdev.todoer.database;

import android.provider.BaseColumns;

public class TaskContract {

    public static final String DB_NAME = "io.freesexdev.todoer.tasks.db";
    public static final String BIRTHDAY_DB_NAME = "io.freesexdev.todoer.birthdays.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "tasks";
    public static final String BirthdayTable = "birthdays";

    public class Columns {
        public static final String TASK = "task";
        public static final String _ID = BaseColumns._ID;
        public static final String BIRTHDAY_NAME = "name";
        public static final String AGE = "age";
        public static final String DATE = "date";
    }

}
