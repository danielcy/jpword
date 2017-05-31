package com.cynb.jpword.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.cynb.jpword.data.DataBaseManager;
import com.cynb.jpword.data.GlobalManager;
import com.cynb.jpword.data.Word;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class AddWordActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private Button mCommitBtn;
    private Button mUndoBtn;
    private EditText mJapaneseInput;
    private EditText mChineseInput;
    private DataBaseManager dbManager;
    private Context mContext;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_word);
        mContentView = findViewById(R.id.add_word_content);
        mContext = AddWordActivity.this;

        dbManager = new DataBaseManager(mContext);
        bindViews();
    }

    private void bindViews(){
        mCommitBtn = (Button)findViewById(R.id.commit_btn);
        mUndoBtn = (Button)findViewById(R.id.undo_btn);
        mJapaneseInput = (EditText)findViewById(R.id.japanese_input_edit);
        mChineseInput = (EditText)findViewById(R.id.chinese_input_edit);

        mCommitBtn.setOnClickListener(this);
        mUndoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commit_btn:
                String jpword = mJapaneseInput.getText().toString();
                String chword = mChineseInput.getText().toString();
                if (jpword.equals("") || chword.equals("")) {
                    Toast.makeText(this, "单词不能为空", Toast.LENGTH_SHORT).show();
                    break;
                }
                Word newWord = new Word(0, chword, jpword, 0, 0, GlobalManager.currentLibrary);
                dbManager.addWord(newWord);
                mJapaneseInput.setText("");
                mChineseInput.setText("");
                Toast.makeText(this, "插入完毕", Toast.LENGTH_SHORT).show();
                break;
            case R.id.undo_btn:
                Word delWord = dbManager.deleteLastWord();
                if (delWord == null) {
                    Toast.makeText(this, "没有可以删除的单词", Toast.LENGTH_SHORT).show();
                    break;
                }
                int id = delWord.getId();
                String japanese = delWord.getJapanese();
                String chinese = delWord.getChinese();
                String showText = "删除单词: id-" + String.valueOf(id) + japanese + " " + chinese;
                Toast.makeText(this, showText, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
