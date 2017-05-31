package com.cynb.jpword.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseManager {
    private WordDBOpenHelper wordDBOpenHelper;
    private static final int wordDBLatestVersion = 1;

    public DataBaseManager(Context context) {
        wordDBOpenHelper = new WordDBOpenHelper(context, "word.db", null, wordDBLatestVersion);
    }

    public void addWord(Word word) {
        SQLiteDatabase db = wordDBOpenHelper.getWritableDatabase();
        String chinese = word.getChinese();
        String japanese = word.getJapanese();
        int category = word.getCategory();
        int learningStatus = word.getLearningStatus();
        int libId = word.getLibId();
        String sql = "INSERT INTO words (japanese, chinese, category, lib_id, learning_status, status, add_time, update_time) "
            + "VALUES (" + japanese + ", " + chinese + ", " + String.valueOf(category) + ", " + String.valueOf(libId) + ", " + String.valueOf(learningStatus) + ","
            + "1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);";
        db.execSQL(sql);
    }
}
