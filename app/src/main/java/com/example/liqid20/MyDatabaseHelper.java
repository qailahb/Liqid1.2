package com.example.liqid20;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

class MyDatabaseHelper extends SQLiteOpenHelper {

    private final Context context;
    private static final String DATABASE_NAME = "Dashboard.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_DATASET = "dataset";
    private static final String COLUMN_LIST = "list";
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
        String sqlDatasetCreate = "CREATE TABLE dataset (id integer primary key autoincrement, list string, speed real, travel real, wait real, force real);";
        db.execSQL(sqlDatasetCreate);

        String sqlListNamesCreate = "CREATE TABLE list_names (id integer primary key autoincrement, name string);";
        db.execSQL(sqlListNamesCreate);

        String sqlDefaultListNames = "insert into list_names (name) values ('QFLOW-VI-LOT'), ('QFLOW-VI-LOT1'), ('QFLOW-VI-LOT2'), ('QFLOW-VI-LOT3'), ('QFLOW-VI-LOT4');";
        db.execSQL(sqlDefaultListNames);

//        String query = "CREATE TABLE " + TABLE_DATASET + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                COLUMN_LIST + " STRING, " +
//                COLUMN_SPEED + " REAL," + COLUMN_TRAVEL + " REAL," + COLUMN_WAIT + " REAL," + COLUMN_FORCE + " REAL);";
//        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS dataset;");
        db.execSQL("DROP TABLE IF EXISTS list_names;");
        onCreate(db);
    }

    void addList(String list, float speed, float travel, float wait, float force) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_LIST, list);
        cv.put(COLUMN_SPEED, speed);
        cv.put(COLUMN_TRAVEL, travel);
        cv.put(COLUMN_WAIT, wait);
        cv.put(COLUMN_FORCE, force);

        long result = db.insert(TABLE_DATASET, null, cv);
        if(result == -1) {
            Toast.makeText(context, "Failed to insert", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    void addListName(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", name);
        long result = db.insert("list_names", null, cv);
        if(result == -1) {
            Toast.makeText(context, "Failed to insert", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT).show();
        }
    }


    Cursor readData(String list) {
        String query = String.format("Select id, speed, travel, wait, force from dataset where list = '%s'", list);
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor getDataFromTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_DATASET, null);
    }

    public String[] getListNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM list_names", null);
        ArrayList<String> listNames = new ArrayList<String>();
        while (cursor.moveToNext()) {
            listNames.add(cursor.getString(0));
        }
        String[] result = new String[listNames.size()];
        listNames.toArray(result);
        return result;
    }

    public void deleteTable(String listName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("list_names", "name = ?", new String[]{listName});
    }

    public void updateListName(String oldListName, String newListName) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", newListName);

        int rowsAffected = db.update("list_names", values, "name = ?", new String[]{oldListName});

        Log.d("UpdateListName", "Rows affected: " + rowsAffected);

        if (rowsAffected > 0) {
            Toast.makeText(context, "List name updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to update list name", Toast.LENGTH_SHORT).show();
        }
    }

}
