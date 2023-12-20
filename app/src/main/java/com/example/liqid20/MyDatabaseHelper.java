package com.example.liqid20;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

class MyDatabaseHelper extends SQLiteOpenHelper {

    private final Context context;
    private static final String DATABASE_NAME = "Dashboard.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "dashboard";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SPEED = "speed";
    private static final String COLUMN_TRAVEL = "travel";
    private static final String COLUMN_WAIT = "wait";
    private static final String COLUMN_FORCE = "force";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SPEED + " REAL," + COLUMN_TRAVEL + " REAL," + COLUMN_WAIT + " REAL," + COLUMN_FORCE + " REAL);";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void addList(float speed, float travel, float wait, float force) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_SPEED, speed);
        cv.put(COLUMN_TRAVEL, travel);
        cv.put(COLUMN_WAIT, wait);
        cv.put(COLUMN_FORCE, force);
        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1) {
            Toast.makeText(context, "Failed to insert", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

}
