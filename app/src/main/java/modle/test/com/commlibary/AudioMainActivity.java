package modle.test.com.commlibary;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import com.commlibary.audio.AudioRecordUtil;
import com.commlibary.audio.AudioTrackManager;
import com.commlibary.audio.tester.AudioPlayerTester;

import java.io.File;

import static modle.test.com.commlibary.base.BaseApplication.context;

/**
 * Created by shanshan on 2017/8/28.
 */

public class AudioMainActivity extends Activity {

    private boolean granted;
    private Button mStart,mEnd,mStartPlay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_main_acitivity);
        mStart = (Button) findViewById(R.id.start);
        mEnd = (Button) findViewById(R.id.stop);
        mStartPlay = (Button) findViewById(R.id.start_paly);

        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AudioRecordUtil.getInstance().startRecord();
            }
        });

        mEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AudioRecordUtil.getInstance().stopRecord();
            }
        });

        mStartPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AudioTrackManager.getInstance().startPlay("/sdcard/new.wav");
               // mediaPlayerPlay("new.wav");
               // new AudioPlayerTester().startTesting();
            }
        });

    }


    /**
     * MediaPlayer播放
     * */
    private void mediaPlayerPlay(String path){
        /* 获得MeidaPlayer对象 */
        MediaPlayer mediaPlayer = new MediaPlayer();

        /* 得到文件路径 *//* 注：文件存放在SD卡的根目录，一定要进行prepare()方法，使硬件进行准备 */
        File file = new File(Environment.getExternalStorageDirectory(),path);

        try{
                /* 为MediaPlayer 设置数据源 */
            mediaPlayer.setDataSource(file.getAbsolutePath());

                /* 准备 */
            mediaPlayer.prepare();
            mediaPlayer.start();

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }




    @Override
    protected void onStop() {
        super.onStop();
        AudioRecordUtil.getInstance().stopRecord();
    }
}
