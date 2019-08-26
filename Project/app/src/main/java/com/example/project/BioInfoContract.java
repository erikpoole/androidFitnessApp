package com.example.project;

import android.provider.BaseColumns;

public class BioInfoContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private BioInfoContract() {}

    /* Inner class that defines the table contents */
    public static class BioEntry implements BaseColumns {
        public static final String TABLE_NAME = "bio";
        public static final String USER_NAME = "enter your name";
        public static final String CITY = "enter your city";
        public static final String COUNTRY = "enter your country";
        public static final String HEIGHT = "enter your height";
        public static final String WEIGHT = "enter your weight";
        public static final String AGE = "enter your age";
        public static final String SEX = "enter your sex";
    }
}
