package com.example.project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BioHelperDB extends SQLiteOpenHelper {
    private static final String SQL_CREATE_BIO =
            "CREATE TABLE " + BioInfoContract.BioEntry.TABLE_NAME + " (" +
                    BioInfoContract.BioEntry._ID + " INTEGER PRIMARY KEY," +
                    BioInfoContract.BioEntry.USER_NAME + " TEXT," +
                    BioInfoContract.BioEntry.CITY + " TEXT," +
                    BioInfoContract.BioEntry.COUNTRY + " TEXT," +
                    BioInfoContract.BioEntry.HEIGHT + " TEXT," +
                    BioInfoContract.BioEntry.WEIGHT + " TEXT," +
                    BioInfoContract.BioEntry.AGE + " INTEGER," +
                    BioInfoContract.BioEntry.SEX + " INTEGER)";

    private static final String SQL_DELETE_BIO =
            "DROP TABLE IF EXISTS " + BioInfoContract.BioEntry.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Bio.db";

    public BioHelperDB(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_BIO);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_BIO);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

