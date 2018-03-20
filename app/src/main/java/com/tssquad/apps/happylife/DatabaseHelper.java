package com.tssquad.apps.happylife;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "happylife_db";

    private static final String TABLE_JOB_SEARCH = "quotes";

    private static final String KEY_ID = "id";
    private static final String KEY_SEARCH = "search_string";

    private static final int DATABASE_VERSION = 1;
    private static final int mHistorySize = 5;

    private String CREATE_QUERY_JOB = "CREATE TABLE IF NOT EXISTS "+TABLE_JOB_SEARCH+"("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+KEY_SEARCH+" VARCHAR NOT NULL unique );";


    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
        /*Environment.getExternalStorageDirectory()+ File.separator+Config.APP_DIRECTORY_NAME+File.separator+TableInfoPublish.DATABASE_NAME*/
        //SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory()+ File.separator+Config.APP_DIRECTORY_NAME+File.separator+TableInfoPublish.DATABASE_NAME,null);
        Log.i("Database Operation","Database Created");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_QUERY_JOB);
        Log.i("Database Operation","Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // insert val
    public void insertTo(DatabaseHelper dop , String value){

        SQLiteDatabase sdb = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_SEARCH, value);
        sdb.insert(TABLE_JOB_SEARCH, null, cv);
        //long k = sdb.insert(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);

        Log.i("Database Operation", "Row inserted");

    }


    public void updateTo(DatabaseHelper dop , int key , String value ){
        SQLiteDatabase sdb = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String selection = (KEY_ID + "= ?");
        String args[] = {String.valueOf(key)};
        cv.put(KEY_SEARCH,value);
        sdb.update(TABLE_JOB_SEARCH,cv,selection,args);

    }

    public List<String> readAll(DatabaseHelper dop)   {
        SQLiteDatabase sdb = dop.getWritableDatabase();
        List<String> quote = new ArrayList<>();

        String[] col = {KEY_ID, KEY_SEARCH};
        String OrderBy = KEY_ID;
        Cursor  cursor = sdb.query(TABLE_JOB_SEARCH, col,null,null,null,null, OrderBy+ " DESC");

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex(KEY_SEARCH));

                quote.add(name);
                cursor.moveToNext();
            }
        }


        return quote;
    }


    public Cursor getAllFrom(DatabaseHelper dop){
        SQLiteDatabase sdb = dop.getReadableDatabase();
        String OrderBy = KEY_ID;
        String[] col = {KEY_ID, KEY_SEARCH};
        Cursor cr = null;

        return cr;
    }


    public void deleteItem(DatabaseHelper dop , String value, int id){
        SQLiteDatabase sdb = dop.getWritableDatabase();
        String selection = KEY_SEARCH + " LIKE ?";
        String args[] = {value};

        switch (id) {

            case 0:
                sdb.delete(TABLE_JOB_SEARCH, selection, args);
                break;


            default:
                break;
        }


    }

}
