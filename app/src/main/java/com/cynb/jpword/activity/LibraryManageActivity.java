package com.cynb.jpword.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.cynb.jpword.tools.FileHelper;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class LibraryManageActivity extends CommonFullscreenActivity implements View.OnClickListener {
    private Context mContext;
    private View mContentView;
    private Button mImportLibBtn;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;

    @Override
    protected void initialize() {
        super.initialize();

        setContentView(R.layout.activity_library_manage);

        mContext = LibraryManageActivity.this;
        mContentView = findViewById(R.id.library_manage_content);
        bindViews();
    }

    private void bindViews(){
        mImportLibBtn = (Button)findViewById(R.id.import_lib_btn);
        mImportLibBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.import_lib_btn:
                alert = null;
                builder = new AlertDialog.Builder(this);
                List<File> libFiles;
                try{
                    libFiles = FileHelper.getImportLibraryFileList(this);
                } catch (IOException e){
                    Toast.makeText(this, "读取文件失败...", Toast.LENGTH_SHORT).show();
                    break;
                }
                final String[] libNames = new String[libFiles.size()];
                int i = 0;
                for(File file: libFiles) {
                    libNames[i] = file.getName();
                    i++;
                }
                alert = builder.setTitle("选择导入文件:")
                                .setItems(libNames, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getApplicationContext(), "你选择了" + libNames[which], Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("取消", new OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).create();
                alert.show();
                break;
        }
    }
}
