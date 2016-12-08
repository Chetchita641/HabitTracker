package com.chrismacholtz.habittracker.data;

import android.provider.BaseColumns;

/**
 * Created by SWS Customer on 12/7/2016.
 */ //Contract class for all the constants used in the database
final public class HabitContract {
    public HabitContract() {}

    public final static class HabitEntry implements BaseColumns {
        public static final String TABLE_NAME = "exercise";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_START = "start";
        public static final String COLUMN_END = "end";
        public static final String COLUMN_DURATION = "duration";
    }
}
