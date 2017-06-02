package com.cynb.jpword.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.cynb.jpword.tools.Converter;
import java.util.ArrayList;
import java.util.List;

public class DataBaseManager {
    private WordDBOpenHelper wordDBOpenHelper;
    private static final int wordDBLatestVersion = 1;

    public DataBaseManager(Context context) {
        wordDBOpenHelper = new WordDBOpenHelper(context, "word.db", null, wordDBLatestVersion);
    }

    public boolean addWord(Word word) {
        SQLiteDatabase db = wordDBOpenHelper.getWritableDatabase();
        String chinese = word.getChinese();
        String japanese = word.getJapanese();
        String announce = word.getAnnounce();
        int category = word.getCategory();
        int learningStatus = word.getLearningStatus();
        int libId = word.getLibId();

        Cursor cursor =  db.rawQuery("SELECT * FROM words WHERE status=1 AND japanese=? AND lib_id=? ORDER BY id DESC LIMIT 0,1;", new String[]{japanese, String.valueOf(libId)});
        if(cursor.moveToFirst()) {
            String oldChinese = cursor.getString(cursor.getColumnIndex("chinese"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String newChinese = oldChinese + ";" + chinese;
            db.execSQL("UPDATE words SET chinese=? WHERE id=?", new String[]{newChinese, String.valueOf(id)});
            return false;
        }
        cursor.close();
        String sql = "INSERT INTO words (japanese, chinese, announce, category, lib_id, learning_status, status, add_time, update_time) "
            + "VALUES (" + japanese + ", " + chinese + ", " + announce + ", " + String.valueOf(category) + ", " + String.valueOf(libId) + ", " + String.valueOf(learningStatus) + ","
            + "1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);";
        db.execSQL(sql);
        return true;
    }

    public Word deleteLastWord() {
        SQLiteDatabase db = wordDBOpenHelper.getWritableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM words WHERE status=1 ORDER BY id DESC LIMIT 0,1;", new String[]{});
        if(cursor.moveToFirst()) {
            Word delWord = Converter.getWordFromCursor(cursor);
            db.execSQL("DELETE FROM words WHERE id=?", new String[]{String.valueOf(delWord.getId())});
            cursor.close();
            return delWord;
        }
        cursor.close();
        return null;
    }
}
