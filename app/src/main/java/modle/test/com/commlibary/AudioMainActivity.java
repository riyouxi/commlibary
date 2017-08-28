package modle.test.com.commlibary;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import com.commlibary.audio.AudioRecordUtil;

import static modle.test.com.commlibary.base.BaseApplication.context;

/**
 * Created by shanshan on 2017/8/28.
 */

public class AudioMainActivity extends Activity {

    private Button mStart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_main_acitivity);
        mStart = (Button) findViewById(R.id.start);

        if (PackageManager.PERMISSION_GRANTED ==   ContextCompat.
                checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO)) {
        }else{
            //提示用户开户权限音频
            String[] perms = {"android.permission.RECORD_AUDIO"};
            ActivityCompat.requestPermissions(AudioMainActivity.this,perms, 1);
        }
        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AudioRecordUtil.getInstance().startRecord();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        AudioRecordUtil.getInstance().stopRecord();
    }
}
