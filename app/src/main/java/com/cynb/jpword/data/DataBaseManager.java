package com.cynb.jpword.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.cynb.jpword.tools.Converter;
import com.cynb.jpword.tools.FileHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            + "VALUES ('" + japanese + "', '" + chinese + "', '" + announce + "', " + String.valueOf(category) + ", " + String.valueOf(libId) + ", " + String.valueOf(learningStatus) + ","
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

    @SuppressWarnings("unchecked")
    public boolean importLibrary(String libFileName) throws Exception{
        StringBuilder sb = new StringBuilder("");
        File libFile = FileHelper.getFileByName(libFileName);
        if (libFile == null || !libFile.exists()){
            Log.e("LibLoadError","cannot find lib file in jpword directory");
            return false;
        }
        FileInputStream input = new FileInputStream(libFile);
        byte[] temp = new byte[1024];
        int len;
        while ((len = input.read(temp)) > 0) {
            sb.append(new String(temp, 0, len));
        }
        input.close();
        String jsonStr = sb.toString();
        Gson gson = new Gson();
        Map<String, Object>libMap = gson.fromJson(jsonStr, new TypeToken<Map<String, Object>>() {}.getType());
        String libName = libMap.get("name").toString();
        if (hasLibrary(libName)){
            Log.e("LibLoadError","already has a same library. libname:" + libName);
            return false;
        }
        int newId = addLibrary(libName);
        List<Map<String, Object>>wordList = (ArrayList<Map<String, Object>>)libMap.get("words");
        for(Map<String, Object> wordMap:wordList){
            String chinese = wordMap.get("chinese").toString();
            String japanese = wordMap.get("japanese").toString();
            String announce = wordMap.get("announce").toString();
            int category = (int)Float.parseFloat(wordMap.get("category").toString());
            int learningStatus = (int)Float.parseFloat(wordMap.get("learningStatus").toString());
            Word newWord = new Word(0, chinese, japanese, announce, category, learningStatus, newId);
            addWord(newWord);
        }
        return true;
    }

    private boolean hasLibrary(String libName) {
        SQLiteDatabase db = wordDBOpenHelper.getWritableDatabase();
        boolean result;
        Cursor cursor =  db.rawQuery("SELECT * FROM word_libs WHERE lib_name=?;", new String[]{libName});
        result = cursor.moveToFirst();
        cursor.close();
        return result;
    }

    private int addLibrary(String libName){
        if(hasLibrary(libName)){
            Log.i("LibAddError", "aleady has such library, fail to add the library: " + libName);
            return 0;
        }
        SQLiteDatabase db = wordDBOpenHelper.getWritableDatabase();
        String sql = "INSERT INTO word_libs (lib_name, status, add_time, update_time) VALUES "
            + "('" + libName + "', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);";
        db.execSQL(sql);
        Cursor cursor =  db.rawQuery("SELECT * FROM word_libs WHERE lib_name=?;", new String[]{libName});
        int id = 0;
        if (cursor.moveToFirst()){
            id = cursor.getInt(cursor.getColumnIndex("id"));
        }
        cursor.close();
        return id;
    }

    public List<WordLibrary> getAllLibrarys() {
        List<WordLibrary> libs = new ArrayList<>();
        SQLiteDatabase db = wordDBOpenHelper.getWritableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM word_libs;", new String[]{});
        if (!cursor.moveToFirst()) {
            cursor.close();
            return libs;
        }
        do {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String libName = cursor.getString(cursor.getColumnIndex("lib_name"));
            int markWordId = cursor.getInt(cursor.getColumnIndex("mark_word_id"));
            WordLibrary lib = new WordLibrary(id, libName, markWordId);
            libs.add(lib);
        } while (cursor.moveToNext());
        cursor.close();
        return libs;
    }
}
