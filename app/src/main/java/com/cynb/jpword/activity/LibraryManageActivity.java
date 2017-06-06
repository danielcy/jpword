package com.cynb.jpword.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import com.cynb.jpword.data.DataBaseManager;
import com.cynb.jpword.data.GlobalManager;
import com.cynb.jpword.data.WordLibrary;
import com.cynb.jpword.tools.FileHelper;
import com.cynb.jpword.tools.SmallUIPoper;
import com.cynb.jpword.tools.ToastUtil;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LibraryManageActivity extends CommonFullscreenActivity implements View.OnClickListener {
    private Context mContext;
    private DataBaseManager dbManager;

    @Override
    protected void initialize() {
        super.initialize();

        setContentView(R.layout.activity_library_manage);

        mContext = LibraryManageActivity.this;
        dbManager = new DataBaseManager(mContext);
        bindViews();
    }

    private void bindViews(){
        Button mSelectLibBtn = (Button)findViewById(R.id.select_lib_btn);
        Button mImportLibBtn = (Button)findViewById(R.id.import_lib_btn);
        Button mDeleteLibBtn = (Button)findViewById(R.id.delete_lib_btn);
        Button mCheckLibBtn  = (Button)findViewById(R.id.check_lib_btn);

        mSelectLibBtn.setOnClickListener(this);
        mImportLibBtn.setOnClickListener(this);
        mDeleteLibBtn.setOnClickListener(this);
        mCheckLibBtn. setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        AlertDialog alert;
        AlertDialog.Builder builder;
        switch (v.getId()) {
            case R.id.select_lib_btn:
                builder = new AlertDialog.Builder(this);
                final List<WordLibrary> libs = dbManager.getAllLibrarys();
                if (libs.isEmpty()) {
                    ToastUtil.showMessage(this, "没有有效的词库");
                    break;
                }
                String[] names = new String[libs.size()];
                int k = 0;
                for (WordLibrary lib:libs) {
                    names[k] = lib.getLibName();
                    k++;
                }
                alert = builder.setTitle("选择当前词库")
                                .setSingleChoiceItems(names, 0, new OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        GlobalManager.currentLibrary = libs.get(which).getId();
                                        String showStr = "当前词库切换到: " + libs.get(which).getLibName();
                                        ToastUtil.showMessage(mContext, showStr);
                                    }
                                }).create();
                alert.show();
                break;
            case R.id.import_lib_btn:
                builder = new AlertDialog.Builder(this);
                List<File> libFiles;
                try{
                    libFiles = FileHelper.getImportLibraryFileList(this);
                } catch (IOException e){
                    ToastUtil.showMessage(this, "读取文件失败...");
                    break;
                }
                if (libFiles == null || libFiles.isEmpty()){
                    ToastUtil.showMessage(this, "找不到有效的词库文件");
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
                                        String title = "导入词库";
                                        final String filename = libNames[which];
                                        String message = "确定要导入"+filename+"吗?";
                                        SmallUIPoper.popUpAJudgeAlertDialog(mContext, title,
                                            message, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    try{
                                                        if (!dbManager.importLibrary(filename)){
                                                            ToastUtil.showMessage(mContext, "导入失败，已存在同名词库或词库文件出错");
                                                        } else {
                                                            ToastUtil.showMessage(mContext, "导入成功");
                                                        }
                                                    } catch (Exception e){
                                                        e.printStackTrace();
                                                        ToastUtil.showMessage(mContext, "读取文件失败...");
                                                    }

                                                }
                                            });
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).create();
                alert.show();
                break;
            case R.id.delete_lib_btn:
                builder = new AlertDialog.Builder(this);
                final List<WordLibrary> delLibs = dbManager.getAllLibrarys();
                if (delLibs.isEmpty()) {
                    ToastUtil.showMessage(this, "没有有效的词库");
                    break;
                }
                String[] delNames = new String[delLibs.size()];
                int j = 0;
                for (WordLibrary lib:delLibs) {
                    delNames[j] = lib.getLibName();
                    j++;
                }
                final List<WordLibrary> checkDelLibs = new ArrayList<>();
                final boolean[] checkItems = new boolean[delLibs.size()];
                for(int x = 0; x < checkItems.length; x++){
                    checkItems[x] = false;
                }
                alert = builder.setTitle("选择要删除的库:")
                                .setMultiChoiceItems(delNames, checkItems, new DialogInterface.OnMultiChoiceClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                        checkItems[which] = isChecked;
                                    }
                                })
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        for(int x = 0; x < delLibs.size(); x++){
                                            if (checkItems[x]){
                                                checkDelLibs.add(delLibs.get(x));
                                            }
                                        }
                                        String title = "删除词库";
                                        String message = "确定要删除词库吗？(该操作不可恢复)";
                                        SmallUIPoper.popUpAJudgeAlertDialog(mContext,title, message , new DialogInterface.OnClickListener(){
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                for(WordLibrary lib:checkDelLibs){
                                                    if(dbManager.delLibrary(lib)){
                                                        ToastUtil.showMessage(mContext, "词库已删除: " + lib.getLibName());
                                                    } else {
                                                        ToastUtil.showMessage(mContext, "词库不存在: " + lib.getLibName());
                                                    }
                                                }
                                            }
                                        });
                                    }
                                }).create();
                alert.show();
                break;
            case R.id.check_lib_btn:
                break;
        }
    }
}
