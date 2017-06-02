package com.cynb.jpword.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class WordDBOpenHelper extends SQLiteOpenHelper {
    public WordDBOpenHelper(Context context, String name, CursorFactory factoryint,int version) {
        super(context, name, factoryint, version  );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE word_libs (\n"
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + "lib_name varchar(16) NOT NULL,\n"
            + "mark_word_id int UNSIGNED,\n"
            + "status tinyint UNSIGNED NOT NULL,\n"
            + "add_time timestamp NOT NULL,\n"
            + "update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP\n"
            + ");");

        db.execSQL("CREATE TABLE words (\n"
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + "japanese varchar(16) NOT NULL,\n"
            + "chinese varchar(64) NOT NULL,\n"
            + "announce varchar(16) NOT NULL,\n"
            + "category int UNSIGNED,\n"
            + "lib_id int UNSIGNED,\n"
            + "learning_status tinyint UNSIGNED,\n"
            + "status tinyint UNSIGNED NOT NULL,\n"
            + "add_time timestamp NOT NULL,\n"
            + "update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP\n"
            + ");");

        db.execSQL("INSERT INTO word_libs (lib_name,mark_word_id,status,add_time,update_time) VALUES ('default', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);");
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        
    }
}
