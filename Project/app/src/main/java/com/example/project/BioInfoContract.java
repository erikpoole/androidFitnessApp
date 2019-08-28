package com.example.project;

import android.provider.BaseColumns;

public class BioInfoContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private BioInfoContract() {}

    /* Inner class that defines the table contents */
    public static class BioEntry implements BaseColumns {
        public static final String TABLE_NAME = "bio";
        public static final String USER_NAME = "username";
        public static final String CITY = "city";
        public static final String COUNTRY = "country";
        public static final String HEIGHT = "height";
        public static final String WEIGHT = "weight";
        public static final String AGE = "age";
        public static final String SEX = "sex";
        public static final String IMG_PATH = "imgPath";
        public static final String IS_LOGGED_IN = "isLoggedIn";
    }
}
