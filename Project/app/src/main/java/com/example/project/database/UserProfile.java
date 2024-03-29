package com.example.project.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserProfile {
    private UserHelper _dbHelper;
    private Context _ctx;
    private String _name, _city, _country, _height, _age, _sex, _imgPath;
    private int _rowID, _goal, _weight,  _activeState;
    private Boolean _isLoggedIn, _isInDarkMode;

    public UserProfile(Context ctx) {
        _name = _city = _country = _height = _age = _sex = _imgPath = "";
        _rowID = _weight = _goal = 0;
        _activeState = 1;
        _isLoggedIn = _isInDarkMode = false;
        _ctx = ctx;
        _dbHelper = new UserHelper(_ctx);
        SQLiteDatabase db = _dbHelper.getReadableDatabase();

        // COMMENTS FROM SQLite ANDROID DOCS
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                UserContract.UserEntry.USER_NAME,
                UserContract.UserEntry.CITY,
                UserContract.UserEntry.COUNTRY,
                UserContract.UserEntry.HEIGHT,
                UserContract.UserEntry.WEIGHT,
                UserContract.UserEntry.AGE,
                UserContract.UserEntry.SEX,
                UserContract.UserEntry.IMG_PATH,
                UserContract.UserEntry.GOAL,
                UserContract.UserEntry.ACTIVE_STATE,
                UserContract.UserEntry.IS_LOGGED_IN,
                UserContract.UserEntry.IS_IN_DARK_MODE
        };

        String selection = UserContract.UserEntry.IS_LOGGED_IN + " = ?";
        String[] selectionArgs = {"1"};

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                UserContract.UserEntry.USER_NAME + " ASC";

        Cursor cursor = db.query(
                UserContract.UserEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        if (cursor.moveToFirst()) {
            _rowID = cursor.getInt(0);
            _name = cursor.getString(1);
            _city = cursor.getString(2);
            _country = cursor.getString(3);
            _height = cursor.getString(4);
            _weight = cursor.getInt(5);
            _age = cursor.getString(6);
            _sex = cursor.getString(7);
            _imgPath = cursor.getString(8);
            _goal = cursor.getInt(9);
            _activeState = cursor.getInt(10);
            _isLoggedIn = cursor.getInt(11) == 1;
            _isInDarkMode = cursor.getInt(12) == 1;
        }

        cursor.close();
        db.close();
    }

    // GETTERS
    public String getName() { return _name; }
    public String getAge() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String formattedDate = sdf.format(new Date());
        String[] currentDate = formattedDate.split("\\.");
        int year = Integer.parseInt(currentDate[0]);
        int month = Integer.parseInt(currentDate[1]);
        int day = Integer.parseInt(currentDate[2]);
        String[] dob = _age.split("/");
        int dobMonth = Integer.parseInt(dob[0]);
        int dobDay = Integer.parseInt(dob[1]);
        int dobYear = Integer.parseInt(dob[2]);

        int age = year - dobYear;
//        Log.d("age", "getAge: " + age);

        if (month > dobMonth) {
            age++;
        } else if (month == dobMonth) {
            if (dobDay <= day) {
                age++;
            }
        }
        return String.valueOf(age);
    }
    public String getSex() { return _sex; }
    public int getWeight() { return _weight; }
    public String getHeight() {
        String[] heightVals = _height.split(" ");
        return heightVals[0] + "'" +  heightVals[1] + "\"";
    }
    public String getCity() { return _city; }
    public String getCountry() { return _country; }
    public String getImgPath() { return _imgPath; }
    public int getGoal() { return _goal; }
    public int getActiveState() { return _activeState; }
    public Boolean isLoggedIn() { return _isLoggedIn; }
    public Boolean isInDarkMode() { return _isInDarkMode; }

    // SETTERS - used to change values, won't be reflected in db until update() is called.
    public void setName(String name) { _name = name; }
    public void setDOB(String age) { _age = age; }
    public void setSex(String sex) { _sex = sex; }
    public void setWeight(int weight) { _weight = weight; }
    public void setHeight(String height) { _height = height; }
    public void setCity(String city) { _city = city; }
    public void setCountry(String country) { _country = country; }
    public void setImgPath(String imgPath) { _imgPath = imgPath; }
    public void setGoal(int goal) {_goal = goal; }
    public void setActiveState(int activeState) { _activeState = activeState; }
    public void toggleDarkMode() { _isInDarkMode = !_isInDarkMode; }

    int convertDark() {
        if (_isInDarkMode) {
            return 1;
        }
        return 0;
    }
    // UPDATE PROFILE
    public void update() {
        int darkMode = convertDark();
        Log.d("darkMode", String.valueOf(darkMode));
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserContract.UserEntry.USER_NAME, _name);
        values.put(UserContract.UserEntry.CITY, _city);
        values.put(UserContract.UserEntry.COUNTRY, _country);
        values.put(UserContract.UserEntry.HEIGHT, _height);
        values.put(UserContract.UserEntry.WEIGHT, _weight);
        values.put(UserContract.UserEntry.AGE, _age);
        values.put(UserContract.UserEntry.SEX, _sex);
        values.put(UserContract.UserEntry.IMG_PATH, _imgPath);
        values.put(UserContract.UserEntry.GOAL, _goal);
        values.put(UserContract.UserEntry.ACTIVE_STATE, _activeState);
        values.put(UserContract.UserEntry.IS_IN_DARK_MODE, darkMode);
        String selection = UserContract.UserEntry.IS_LOGGED_IN + " LIKE ?";
        String[] selectionArgs = {"1"};

        db.update(
                UserContract.UserEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    // User Auth Methods
    public Boolean signUp(String password) {
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserContract.UserEntry.USER_NAME, _name);
        values.put(UserContract.UserEntry.PASSWORD, password);
        values.put(UserContract.UserEntry.CITY, _city);
        values.put(UserContract.UserEntry.COUNTRY, _country);
        values.put(UserContract.UserEntry.HEIGHT, _height);
        values.put(UserContract.UserEntry.WEIGHT, _weight);
        values.put(UserContract.UserEntry.AGE, _age);
        values.put(UserContract.UserEntry.SEX, _sex);
        values.put(UserContract.UserEntry.IMG_PATH, _imgPath);
        values.put(UserContract.UserEntry.GOAL, _goal);
        values.put(UserContract.UserEntry.ACTIVE_STATE, _activeState);
        values.put(UserContract.UserEntry.IS_LOGGED_IN, 1);

        long newRowId = db.insert(UserContract.UserEntry.TABLE_NAME, null, values);
        db.close();
        if (newRowId < 0)
            return false;
        return true;
    }

    public Boolean login(String name, String password) {
        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        String isLoggedIn = "1";
        ContentValues values = new ContentValues();
        values.put(UserContract.UserEntry.IS_LOGGED_IN, isLoggedIn);

        String selection = UserContract.UserEntry.USER_NAME + " LIKE ? AND " + UserContract.UserEntry.PASSWORD + " Like ?";
        String[] selectionArgs = { name, password };

        int count = db.update(
                UserContract.UserEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        if (count < 1) {
            return false;
        }
        return true;
    }

    public Boolean logout() {
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        String isLoggedIn = "0";
        ContentValues values = new ContentValues();
        values.put(UserContract.UserEntry.IS_LOGGED_IN, isLoggedIn);

        String selection = UserContract.UserEntry.IS_LOGGED_IN + " LIKE ?";
        String[] selectionArgs = {"1"};

        int count = db.update(
                UserContract.UserEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        db.close();
        if (count < 1) {
            return false;
        }
        return true;
    }

    public void upgrade() {
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        _dbHelper.onUpgrade(db, 1,1);
    }

    // toggleDarkMode
}
