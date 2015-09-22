package io.freesexdev.todoer;

import android.provider.BaseColumns;

public class TaskContract {

    public static final String DB_NAME = "io.freesexdev.todoer.tasks.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "tasks";

    public class Columns {
        public static final String TASK = "task";
        public static final String _ID = BaseColumns._ID;
    }

}
