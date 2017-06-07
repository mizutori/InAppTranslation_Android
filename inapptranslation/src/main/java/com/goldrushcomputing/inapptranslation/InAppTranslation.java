package com.goldrushcomputing.inapptranslation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.BindingAdapter;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;

import static com.goldrushcomputing.inapptranslation.IATTranslationContract.Translation.TABLE_NAME;

/**
 * Created by Takamitsu Mizutori on 2017/03/19.
 */

public class InAppTranslation {
    private static final String TAG = InAppTranslation.class.getSimpleName();

    private static InAppTranslation sharedInstance = null;
    private String source = null;
    private String target = null;
    public enum TargetTextType {
        Text,
        TextOn,
        TextOff,
        Hint
    }

    public InAppTranslation() {
        target = Locale.getDefault().getLanguage();

    }

    public static InAppTranslation getInstance() {
        if (sharedInstance == null) {
            sharedInstance = new InAppTranslation();
        }
        return sharedInstance;
    }

    public static void setSourceLanguage(String languageCode){
        getInstance().source = languageCode;
    }

    public static void setTargetLanguage(String languageCode){
        getInstance().target = languageCode;
    }

    public static void clearCache(Context context){
        getInstance().clearCacheTable(context);
    }

    public static void localize(String text) {
        //getInstance().translate(text, null, null, view, TargetTextType.Hint);
    }

    @BindingAdapter({"decorateText"})
    public static void decorateText(final View view, String text) {
        ((TextView)view).setText("<<<" + text + ">>>");
    }

    @BindingAdapter({"localizeText"})
    public static void localizeText(final View view, String text) {
        getInstance().translate(text, null, null, view, TargetTextType.Text);
    }

    @BindingAdapter({"localizeTextOff"})
    public static void localizeTextOff(final View view, String text) {
        getInstance().translate(text, null, null, view, TargetTextType.TextOff);
    }

    @BindingAdapter({"localizeTextOn"})
    public static void localizeTextOn(final View view, String text) {
        getInstance().translate(text, null, null, view, TargetTextType.TextOn);
    }

    @BindingAdapter({"localizeHint"})
    public static void localizeHint(final View view, String text) {
        getInstance().translate(text, null, null, view, TargetTextType.Hint);
    }

    private void translate(final String query, String source, final String target, final View view, final TargetTextType type) {
        if(source == null){
            source = this.source;
        }

        final String targetLang;
        if(target == null){
            targetLang = this.target;
        }else{
            targetLang = target;
        }

        final Context context = view.getContext();
        String cachedTranslation = getCachedTransaltion(context, query, targetLang);

        if(cachedTranslation != null){
            attachTranslation(view, type, cachedTranslation);
        }else{
            GetTranslationAsyncTask task = new GetTranslationAsyncTask() {
                @Override
                protected void onPostExecute(String result) {
                    if(result != null){
                        Log.d(TAG, "Translated test is " + result);
                        attachTranslation(view, type, result);

                        cacheTransaltion(context, query, result, targetLang);
                    }
                }
            };
            task.execute(query, source, targetLang);
        }

    }

    private void attachTranslation(View view, TargetTextType type, String result){
        if (view instanceof EditText){
            if(type == TargetTextType.Hint){
                ((EditText)view).setHint(result);
            }else if(type == TargetTextType.Text){
                ((EditText)view).setText(result);
            }
        }else if (view instanceof ToggleButton){
            ToggleButton toggle = (ToggleButton)view;
            if(type == TargetTextType.TextOff){
                toggle.setTextOff(result);
                if(toggle.isChecked() == false){
                    toggle.setText(result);
                }
            }else if(type == TargetTextType.TextOn){
                toggle.setTextOn(result);
                if(toggle.isChecked() == true){
                    toggle.setText(result);
                }
            }else if(type == TargetTextType.Text){
                toggle.setText(result);
            }
        }else if (view instanceof Switch){
            Switch switchWidget = (Switch)view;
            if(type == TargetTextType.TextOff){
                switchWidget.setTextOff(result);
            }else if(type == TargetTextType.TextOn){
                switchWidget.setTextOn(result);
            }else if(type == TargetTextType.Text){
                switchWidget.setText(result);
            }
        }else if (view instanceof Button){
            ((Button)view).setText(result);
        }else if (view instanceof TextView){
            ((TextView)view).setText(result);
        }
    }

    private class GetTranslationAsyncTask extends
            AsyncTask<Object, Void, String> {
        final String TAG = "GetTranslationAsyncTask";

        final String urlTemplate = "https://translation.googleapis.com/language/translate/v2?key=%1$s&source=%2$s&target=%3$s&q=%4$s";
        final String urlTemplateWithoutSource = "https://translation.googleapis.com/language/translate/v2?key=%1$s&target=%2$s&q=%3$s";

        public GetTranslationAsyncTask() {

        }

        protected String doInBackground(Object... params) {
            String query = (String) params[0];
            String source = (String) params[1];
            String target = (String) params[2];

            String translatedText = null;
            HttpURLConnection urlConnection = null;

            try {
                String urlString;

                if(query == null || query.isEmpty()){
                    Log.d(TAG, "query is null or empty");
                    return query;
                }

                String apiKey = BuildConfig.GoogleTranslateApiKey;
                if(apiKey == null || apiKey.isEmpty()){
                    Log.d(TAG, "Google Translate Api Key is not set in local.properties");
                    return query;
                }

                String queryEncoded = URLEncoder.encode(query, "utf-8");

                if(source == null && target != null){
                    urlString = String.format(urlTemplateWithoutSource, apiKey, target, queryEncoded);
                }else if(source != null && target != null){
                    urlString = String.format(urlTemplate, apiKey, source, target, queryEncoded);
                }else{
                    Log.d(TAG, "The source and target langauges are both not set.");
                    return query;
                }

                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(20000);
                urlConnection.setRequestMethod("GET");
                urlConnection.setInstanceFollowRedirects(true);

                int resp = urlConnection.getResponseCode();

                switch (resp){
                    case HttpURLConnection.HTTP_OK:
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                        StringBuilder result = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }
                        in.close();

                        JSONTokener json = new JSONTokener(result.toString());
                        if (json != null) {
                            JSONObject rootObject = (JSONObject) json.nextValue();
                            if(rootObject != null){
                                JSONObject dataObject = rootObject.getJSONObject("data");
                                if(dataObject != null){
                                    JSONArray translations = dataObject.getJSONArray("translations");
                                    for (int i = 0; i < translations.length(); i++) {
                                        JSONObject translation = translations.getJSONObject(i);
                                        translatedText = translation.getString("translatedText");
                                        break;
                                    }
                                }else{
                                    Log.e(TAG, "[Google Translate API] Data is missing in the response" + rootObject.toString());
                                }
                            }else{
                                Log.e(TAG, "[Google Translate API] Root json object is missing in the response");
                            }
                        }
                        break;
                    default:
                        InputStream errorIn = new BufferedInputStream(urlConnection.getErrorStream());
                        BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorIn));

                        StringBuilder errorResult = new StringBuilder();
                        String errorLine;
                        while ((errorLine = errorReader.readLine()) != null) {
                            errorResult.append(errorLine);
                        }
                        errorIn.close();
                        String errorResponseString = errorResult.toString();

                        String errorText = String.format("Failed to get data from Google Translate. Status code = %d, Response = %s", resp, errorResponseString);
                        Log.d(TAG, errorText);
                        break;
                }
            } catch (Exception e) {
                Log.d(TAG, "Got exception while accessing Google Translation");
                e.printStackTrace();
            } finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
            }

            if(translatedText == null){
                return query;
            }else{
                return translatedText;
            }
        }
    }


    protected String getCachedTransaltion(Context context, String key, String lang){

        // Create new helper
        IATDatabaseHelper dbHelper = new IATDatabaseHelper(context);
        // Get the database. If it does not exist, this is where it will
        // also be created.
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] projection = {
                IATTranslationContract.Translation._ID,
                IATTranslationContract.Translation.COLUMN_KEY,
                IATTranslationContract.Translation.COLUMN_VALUE,
                IATTranslationContract.Translation.COLUMN_LANG
        };

        // Filter results WHERE "key" = 'Hello'
        String selection = IATTranslationContract.Translation.COLUMN_KEY + " = ?" + " AND " + IATTranslationContract.Translation.COLUMN_LANG + " = ?";
        String[] selectionArgs = { key, lang };

        // How you want the results sorted in the resulting Cursor
        //String sortOrder = TranslationContract.Translation.COLUMN_KEY + " DESC";
        Cursor cursor = db.query(
                TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        String value = null;

        if(cursor.moveToFirst()){
            String queriedKey = cursor.getString(cursor.getColumnIndex(IATTranslationContract.Translation.COLUMN_KEY));
            value = cursor.getString(cursor.getColumnIndex(IATTranslationContract.Translation.COLUMN_VALUE));
            String queriedLang = cursor.getString(cursor.getColumnIndex(IATTranslationContract.Translation.COLUMN_LANG));
            Log.d(TAG, "chached translation is " + queriedKey + "," + value + "," + queriedLang);
        }
        cursor.close();
        return value;
    }

    protected void cacheTransaltion(Context context, String key, String value, String lang){
        // Create new helper
        IATDatabaseHelper dbHelper = new IATDatabaseHelper(context);
        // Get the database. If it does not exist, this is where it will
        // also be created.
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create insert entries
        ContentValues values = new ContentValues();
        values.put(IATTranslationContract.Translation.COLUMN_KEY, key);
        values.put(IATTranslationContract.Translation.COLUMN_VALUE, value);
        values.put(IATTranslationContract.Translation.COLUMN_LANG, lang);

        // Insert the new row, returning the primary key value of the new row
        try{
            long newRowId;
            newRowId = db.insertOrThrow(
                    TABLE_NAME,
                    null,
                    values);
        }catch(SQLiteConstraintException sce){
            Log.d(TAG, "key(" + key + ") already exist for lang(" + lang + ")");
            //android.database.sqlite.SQLiteConstraintException: UNIQUE constraint failed: Translation.key, Translation.lang (code 2067)
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void clearCacheTable(Context context){
        IATDatabaseHelper dbHelper = new IATDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
    }
}
