package com.chrismacholtz.habittracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chrismacholtz.habittracker.data.HabitContract;
import com.chrismacholtz.habittracker.data.HabitDbHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private HabitDbHelper mDbHelper;
    private boolean mButtonToggle = false;
    private long mStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //For a progress spinner to show the user that the app is keeping time
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        //Set up ClickListeners for two buttons to start/stop the timer, and another button to delete
        // the database
        Button submitButton = (Button) findViewById(R.id.start_button);
        Button clearButton = (Button) findViewById(R.id.clear_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonToggle();
                updateUI();
            }
        });
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAll();
                updateUI();
            }
        });

        updateUI();
    }

    //A simple method to update the user interface so I can see if the database is working correctly
    public void updateUI() {
        Cursor cursor = read();
        try {
            //Some code borrowed from the Pets app
            TextView displayView = (TextView) findViewById(R.id.output_text_view);
            displayView.setText("The table contains  " + cursor.getCount() + " items\n\n");

            int idColumnIndex = cursor.getColumnIndex(HabitContract.HabitEntry._ID);
            int dateColumnIndex = cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_DATE);
            int startColumnIndex = cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_START);
            int endColumnIndex = cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_END);
            int durationColumnIndex = cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_DURATION);
            while (cursor.moveToNext()) {
                String currentDate = cursor.getString(dateColumnIndex);
                String currentStart = cursor.getString(startColumnIndex);
                String currentEnd = cursor.getString(endColumnIndex);
                String currentDuration = cursor.getString(durationColumnIndex);

                displayView.append("Date: " + currentDate +
                        "\nStart Time: " + currentStart +
                        "\nFinish Time: " + currentEnd +
                        "\nDuration: " + currentDuration + "\n");
            }
        } finally {
            // Clean up after your done. Your mother doesn't live in this code.
            cursor.close();
        }
    }

    //Method for inserting new information. The device's current time and date is logged, as well
    // as the start and stop times. Finally, a quick calculation is made to find out how long the
    // user has exercised.
    public void insert() {
        Date rightNow = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm aaa");
        long endTime = rightNow.getTime();
        long difference = endTime - mStartTime;

        //String versions of everything to put into the database
        String currentDate = dateFormat.format(rightNow);
        String startTimeString = timeFormat.format(mStartTime);
        String endTimeString = timeFormat.format(endTime);
        String durationString = (difference / 60000) + " min.";

        ContentValues values = new ContentValues();
        values.put(HabitContract.HabitEntry.COLUMN_DATE, currentDate);
        values.put(HabitContract.HabitEntry.COLUMN_START, startTimeString);
        values.put(HabitContract.HabitEntry.COLUMN_END, endTimeString);
        values.put(HabitContract.HabitEntry.COLUMN_DURATION, durationString);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.insert(HabitContract.HabitEntry.TABLE_NAME, null, values);
    }

    //A simple method to delete the entire database
    public void deleteAll() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(HabitContract.HabitEntry.TABLE_NAME, null, null);
    }

    private Cursor read() {
        //Open the database and access it
        mDbHelper = new HabitDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        //Column names for the cursor
        String[] projection = {
                HabitContract.HabitEntry._ID,
                HabitContract.HabitEntry.COLUMN_DATE,
                HabitContract.HabitEntry.COLUMN_START,
                HabitContract.HabitEntry.COLUMN_END,
                HabitContract.HabitEntry.COLUMN_DURATION
        };

        return db.query(HabitContract.HabitEntry.TABLE_NAME, projection, null, null, null, null, null);
    }

    //A method called by the ClickListeners to change the button from "START" TO "STOP". Also,
    // makes the progress spinner appear and disappear when the user is being timed.
    public void buttonToggle() {
        Button button = (Button) findViewById(R.id.start_button);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        Date rightNow = new Date();
        if (!mButtonToggle) {
            mStartTime = rightNow.getTime();
            mButtonToggle = true;
            button.setText("STOP");
            progressBar.setVisibility(View.VISIBLE);
        } else {
            mButtonToggle = false;
            button.setText("START");
            insert();
            progressBar.setVisibility(View.GONE);
        }
    }
}

