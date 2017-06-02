package com.cynb.jpword.tools;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {
    public static List<File> getImportLibraryFileList(Context context) throws IOException{
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String dirpath = Environment.getExternalStorageDirectory().getCanonicalPath() + "/jpword";
            File jpwordDir = createOrFetchDir(dirpath);
            File[] files = jpwordDir.listFiles();
            List<File> result = new ArrayList<>();
            for(File file: files) {
                String filename = file.getName();
                if(filename.substring(0,12).equals("[jpword_lib]")) {
                    result.add(file);
                }
            }
            return result;
        } else {
            Toast.makeText(context, "SD卡不存在或者不可读写", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public static File getFileByName(String filename) throws IOException {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String filepath =
                Environment.getExternalStorageDirectory().getCanonicalPath() + "/jpword/"
                    + filename;
            File file = new File(filepath);
            if (!file.exists()) {
                return null;
            } else {
                return file;
            }
        } else {
            return null;
        }
    }

    private static File createOrFetchDir(String dirpath){
        File jpwordDir = new File(dirpath);
        if (!jpwordDir.exists()) {
            if (jpwordDir.mkdirs()){
                return jpwordDir;
            } else {
                return null;
            }
        }
        return jpwordDir;
    }
}
