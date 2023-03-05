package com.example.garbageclassification;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_RECORD = "create table Record (" +
            "id integer primary key autoincrement," +
            "word text)";
    public static final String CREATE_CATEGORY = "create table Category (" +
            "id integer primary key autoincrement," +
            "garbage text," +
            "class text," +
            "english text)";
    private Context mContext;

    public MyDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RECORD);
        db.execSQL(CREATE_CATEGORY);
        Toast.makeText(mContext, "数据库加载成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
