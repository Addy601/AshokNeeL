package com.addy.ashok_neel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    public  static final String Database_Name="Login.db";
    public  static final String Table_Name="Login_table";
    public  static final String COL_1="Username";
    public  static final String COL_2="Password";
    public Database(@Nullable Context context) {
        super(context,Database_Name,null,1);
        SQLiteDatabase db =this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ Table_Name+"(Username Text,Password Text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     db.execSQL("DROP TABLE IF EXISTS'"+Table_Name+"'");
     onCreate(db);
    }
}
