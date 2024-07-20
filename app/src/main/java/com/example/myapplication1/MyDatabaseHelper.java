package com.example.myapplication1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Project.db";
    private static final Integer DATABASE_VERSION = 1;

    private static final String TABLE_NAME_USERS = "Users";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    private static final String TABLE_NAME_NOTES = "Notes";
    private static final String COLUMN_USER_ID = "User_id";
    private static final String COLUMN_TITLE = "Title";
    private static final String COLUMN_DATE = "Date";
    private static final String COLUMN_TIME = "Time";



    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryCreateUsers = "CREATE TABLE " + TABLE_NAME_USERS + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_USERNAME + " TEXT, " +
                        COLUMN_EMAIL + " TEXT, " +
                        COLUMN_PASSWORD + " TEXT);";

        String queryCreateNotes = "CREATE TABLE " + TABLE_NAME_NOTES + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_ID + " INTEGER, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_TIME + " TEXT);";

        db.execSQL(queryCreateUsers);
        db.execSQL(queryCreateNotes);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_NOTES);
        onCreate(db);
    }

    void addUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USERNAME, username);
        cv.put(COLUMN_EMAIL, email);
        cv.put(COLUMN_PASSWORD, password);
        long result = db.insert(TABLE_NAME_USERS, null, cv);
        if(result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Added successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    void addNotes(int userID, String title, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USER_ID, userID);
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_TIME, time);
        long result = db.insert(TABLE_NAME_NOTES, null, cv);
        if(result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Added successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllDate(String TableName) {
        String query = "SELECT * FROM " + TableName;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    Cursor readDateByColumnData(String tableName, String column, String columnData) {
        String query = "SELECT * FROM " + tableName + " WHERE " + column + " = " + columnData;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void updateData(String rowId, String title, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase() ;
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_TIME, time);

        long result = db.update(TABLE_NAME_NOTES, cv, "_id=?", new String[]{rowId});
        if(result == -1) {
            Toast.makeText(context, "Failed to Updated", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(context, "Successful Updated", Toast.LENGTH_LONG).show();
        }

    }

    void deleteDataOneRow(String rowId) {
        SQLiteDatabase db = this.getWritableDatabase() ;
        long result = db.delete(TABLE_NAME_NOTES, "_id=?", new String[]{rowId});
        if(result == -1) {
            Toast.makeText(context, "Failed to Deleted", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(context, "Successful Deleted", Toast.LENGTH_LONG).show();
        }

    }

    void deleteAllData(String TABLE_NAME) {
        SQLiteDatabase db = this.getWritableDatabase() ;
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    void deleteAllDataWhereData(String TABLE_NAME, String whereColumn, String whereData) {
        SQLiteDatabase db = this.getWritableDatabase() ;
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + whereColumn + " = " + whereData);
    }
}
