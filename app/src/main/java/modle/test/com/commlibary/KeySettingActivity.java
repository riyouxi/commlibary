package modle.test.com.commlibary;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by Administrator on 2017/8/30.
 */

public class KeySettingActivity extends Activity {

    private int settingKeyCode;

    private EditText mUp,mDown,mLeft,mRight,mOk,mBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.key_setting_activity);
        mUp = (EditText) findViewById(R.id.up);
        mDown = (EditText) findViewById(R.id.down);
        mLeft = (EditText) findViewById(R.id.left);
        mRight = (EditText) findViewById(R.id.right);
        mOk = (EditText) findViewById(R.id.ok);
        mBack = (EditText) findViewById(R.id.back);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return true;
    }
}
