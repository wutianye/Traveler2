package com.example.traveler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AttractionsDataManager extends SQLiteOpenHelper {
    private static final String DB_NAME = "attractions_data";
    private static final String TABLE_NAME = "attractions";
    private static final int DB_VERSION = 1;
    private static final String CITY_NAME = "city_name";
    private static final String ATTRACTION_NAME = "attraction_name";
    private Context mContext = null;


    public static final String DB_CREATE = "create table " + TABLE_NAME + " ("
            + "id integer primary key autoincrement, "
            + CITY_NAME + " varchar(10) not null, "
            + ATTRACTION_NAME + " varchar(20) not null)";


    public AttractionsDataManager(Context context){
        super(context,DB_NAME,null,DB_VERSION);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}

