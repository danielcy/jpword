package com.cynb.jpword.tools;

import android.database.Cursor;
import com.cynb.jpword.data.Word;

public class Converter {
    public static Word getWordFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex("id"));
        String japanese = cursor.getString(cursor.getColumnIndex("japanese"));
        String chinese = cursor.getString(cursor.getColumnIndex("chinese"));
        int category = cursor.getInt(cursor.getColumnIndex("category"));
        int libId = cursor.getInt(cursor.getColumnIndex("lib_id"));
        int learningStatus = cursor.getInt(cursor.getColumnIndex("learning_status"));
        return new Word(id, chinese, japanese, category, learningStatus, libId);
    }
}
