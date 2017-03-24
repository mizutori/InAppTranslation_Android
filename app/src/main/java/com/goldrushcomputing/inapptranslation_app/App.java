package com.goldrushcomputing.inapptranslation_app;

import android.app.Application;

import com.goldrushcomputing.inapptranslation.InAppTranslation;

/**
 * Created by Mizutori on 2017/03/20.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        InAppTranslation.setSourceLanguage("en");

        //InAppTranslation.setTargetLanguage("ja");
    }


}
