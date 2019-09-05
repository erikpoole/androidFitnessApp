package com.example.project.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_USERS =
            "CREATE TABLE IF NOT EXISTS " + UserContract.UserEntry.TABLE_NAME + " (" +
                    UserContract.UserEntry._ID + " INTEGER PRIMARY KEY," +
                    UserContract.UserEntry.USER_NAME + " TEXT," +
                    UserContract.UserEntry.PASSWORD + " TEXT," +
                    UserContract.UserEntry.CITY + " TEXT," +
                    UserContract.UserEntry.COUNTRY + " TEXT," +
                    UserContract.UserEntry.HEIGHT + " TEXT," +
                    UserContract.UserEntry.WEIGHT + " INTEGER," +
                    UserContract.UserEntry.AGE + " INTEGER," +
                    UserContract.UserEntry.SEX + " TEXT," +
                    UserContract.UserEntry.IMG_PATH + " TEXT," +
                    UserContract.UserEntry.GOAL + " TEXT," +
                    UserContract.UserEntry.ACTIVE_STATE + " TEXT," +
                    UserContract.UserEntry.IS_LOGGED_IN + " INTEGER," +
                    UserContract.UserEntry.IS_IN_DARK_MODE + " INTEGER)";

    private static final String SQL_DELETE_USERS =
            "DROP TABLE IF EXISTS " + UserContract.UserEntry.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "getFit.db";

    public UserHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USERS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_USERS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
