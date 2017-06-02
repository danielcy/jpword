package com.cynb.jpword.activity;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.cynb.jpword.data.DataBaseManager;
import com.cynb.jpword.data.GlobalManager;
import com.cynb.jpword.data.Word;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class AddWordActivity extends CommonFullscreenActivity implements View.OnClickListener {
    private View mContentView;
    private Button mCommitBtn;
    private Button mUndoBtn;
    private Button mCancelUndoBtn;
    private EditText mJapaneseInput;
    private EditText mChineseInput;
    private EditText mAnnounceInput;
    private DataBaseManager dbManager;
    private Context mContext;

    @Override
    protected void initialize() {
        setContentView(R.layout.activity_add_word);
        mContentView = findViewById(R.id.add_word_content);
        mContext = AddWordActivity.this;

        dbManager = new DataBaseManager(mContext);
        bindViews();
    }

    private void bindViews(){
        mCommitBtn = (Button)findViewById(R.id.commit_btn);
        mUndoBtn = (Button)findViewById(R.id.undo_btn);
        mCancelUndoBtn = (Button)findViewById(R.id.cancel_undo_btn);
        mJapaneseInput = (EditText)findViewById(R.id.japanese_input_edit);
        mChineseInput = (EditText)findViewById(R.id.chinese_input_edit);
        mAnnounceInput = (EditText)findViewById(R.id.announce_input_edit);

        mCommitBtn.setOnClickListener(this);
        mUndoBtn.setOnClickListener(this);
        mCancelUndoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commit_btn:
                String jpword = mJapaneseInput.getText().toString();
                String chword = mChineseInput.getText().toString();
                String announce = mAnnounceInput.getText().toString();
                if (jpword.equals("") || chword.equals("") || announce.equals("")){
                    Toast.makeText(this, "单词不能为空", Toast.LENGTH_SHORT).show();
                    break;
                }
                Word newWord = new Word(0, chword, jpword, announce, 0, 0, GlobalManager.currentLibrary);
                boolean isNew = dbManager.addWord(newWord);
                if (isNew){
                    Toast.makeText(this, "插入完毕", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "单词已存在，加入额外释义", Toast.LENGTH_SHORT).show();
                }
                mJapaneseInput.setText("");
                mChineseInput.setText("");
                break;
            case R.id.undo_btn:
                Word delWord = dbManager.deleteLastWord();
                if (delWord == null) {
                    Toast.makeText(this, "没有可以删除的单词", Toast.LENGTH_SHORT).show();
                    break;
                }
                Log.i("test", "get 1");
                GlobalManager.cancelDelWordCache.push(delWord);
                Log.i("test", "get 2");
                String japanese = delWord.getJapanese();
                String chinese = delWord.getChinese();
                String showText = "删除单词: " + japanese + "-" + chinese;
                Toast.makeText(this, showText, Toast.LENGTH_SHORT).show();
                break;
            case R.id.cancel_undo_btn:
                if (GlobalManager.cancelDelWordCache.empty()) {
                    Toast.makeText(this, "没有可以撤销的动作", Toast.LENGTH_SHORT).show();
                    break;
                }
                Word lastDelWord = GlobalManager.cancelDelWordCache.pop();
                dbManager.addWord(lastDelWord);
                String show = "撤销删除: " + lastDelWord.getJapanese() + "-" + lastDelWord.getChinese();
                Toast.makeText(this, show, Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
