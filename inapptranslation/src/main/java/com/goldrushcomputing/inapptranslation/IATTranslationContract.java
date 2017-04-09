package com.goldrushcomputing.inapptranslation;

import android.provider.BaseColumns;

/**
 * Created by Takamitsu Mizutori on 2017/03/28.
 */

public final class IATTranslationContract {

    public static final  int    DATABASE_VERSION   = 2;
    public static final  String DATABASE_NAME      = "Translation.db";
    private static final String TEXT_TYPE          = " TEXT";
    private static final String UNIQUE          = " unique";
    private static final String COMMA_SEP          = ",";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private IATTranslationContract() {}

    public static abstract class Translation implements BaseColumns {
        public static final String TABLE_NAME       = "Translation";
        public static final String COLUMN_LANG = "lang";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_VALUE = "value";


        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_KEY + TEXT_TYPE + COMMA_SEP +
                COLUMN_VALUE + TEXT_TYPE + COMMA_SEP +
                COLUMN_LANG + TEXT_TYPE + COMMA_SEP +
                "UNIQUE(" + COLUMN_KEY + COMMA_SEP + COLUMN_LANG + ")"
                + " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}