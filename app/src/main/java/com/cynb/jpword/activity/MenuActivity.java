package com.cynb.jpword.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends CommonFullscreenActivity implements View.OnClickListener {
    @Override
    protected void initialize() {
        setContentView(R.layout.activity_menu);

        // Button mTodayWordBtn = (Button) findViewById(R.id.button_pos1);
        Button mAddWordBtn = (Button) findViewById(R.id.button_pos2);
        Button mMyLibBtn = (Button) findViewById(R.id.button_pos3);

        mAddWordBtn.setOnClickListener(this);
        mMyLibBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_pos2:
                Intent intent1 = new Intent(MenuActivity.this, AddWordActivity.class);
                startActivity(intent1);
                break;
            case R.id.button_pos3:
                Intent intent2 = new Intent(MenuActivity.this, LibraryManageActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
