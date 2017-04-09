package com.goldrushcomputing.inapptranslation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Takamitsu Mizutori on 2017/03/28.
 */

public class IATDatabaseHelper extends SQLiteOpenHelper {
    public IATDatabaseHelper(Context context) {
        super(context, IATTranslationContract.DATABASE_NAME, null, IATTranslationContract.DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(IATTranslationContract.Translation.CREATE_TABLE);
    }

    // Method is called during an upgrade of the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(IATTranslationContract.Translation.DELETE_TABLE);
        onCreate(db);
    }
}
