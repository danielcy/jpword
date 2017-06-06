package com.cynb.jpword.tools;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class SmallUIPoper {
    public static void popUpAJudgeAlertDialog(Context context, String title, String message, DialogInterface.OnClickListener okListener){
        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        alert = builder.setTitle(title)
            .setMessage(message)
            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            })
            .setPositiveButton("确定", okListener)
            .create();
        alert.show();
    }
}
