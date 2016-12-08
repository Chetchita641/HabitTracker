package com.chrismacholtz.habittracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SWS Customer on 12/7/2016.
 */ //Database helper
public class HabitDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "habitTracker.db";

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + HabitContract.HabitEntry.TABLE_NAME + "("
            + HabitContract.HabitEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + HabitContract.HabitEntry.COLUMN_DATE + " TEXT NOT NULL, "
            + HabitContract.HabitEntry.COLUMN_START + " TEXT NOT NULL, "
            + HabitContract.HabitEntry.COLUMN_END + " TEXT NOT NULL, "
            + HabitContract.HabitEntry.COLUMN_DURATION + " TEXT NOT NULL);";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + HabitContract.HabitEntry.TABLE_NAME;

    public HabitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { db.execSQL(SQL_CREATE_ENTRIES); }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
