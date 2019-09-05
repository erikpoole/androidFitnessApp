package com.example.project.database;

import android.provider.BaseColumns;

public class UserContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private UserContract() {}

    /* Inner class that defines the table contents */
    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String USER_NAME = "username";
        public static final String CITY = "city";
        public static final String COUNTRY = "country";
        public static final String HEIGHT = "height";
        public static final String WEIGHT = "weight";
        public static final String AGE = "age";
        public static final String SEX = "sex";
        public static final String IMG_PATH = "imgPath";
        public static final String GOAL = "goal";
        public static final String ACTIVE_STATE = "activeState";
        public static final String PASSWORD = "password";
        public static final String IS_LOGGED_IN = "isLoggedIn";
        public static final String IS_IN_DARK_MODE = "isInDarkMode";
    }
}
