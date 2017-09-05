package modle.test.com.commlibary;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.commlibary.audio.AudioRecordUtil;
import com.commlibary.audio.AudioTrackManager;
import com.commlibary.audio.tester.AudioPlayerTester;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

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
//                AudioRecordUtil.getInstance().startRecord();
                isStopTalk = false;
                new AudioSend().start();
            }
        });

        mEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AudioRecordUtil.getInstance().stopRecord();
                isStopTalk = true;
            }
        });

        mStartPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // AudioTrackManager.getInstance().startPlay("/sdcard/new.wav");
               // mediaPlayerPlay("new.wav");
               // new AudioPlayerTester().startTesting();
                new AudioPlay().start();
            }
        });




    }

    /**
     * 音频获取源
     * */
    private int audioSource = MediaRecorder.AudioSource.MIC;
    /**
     * 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
     * */
    private static int sampleRateInHz = 44100;
    /**
     * 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
     * */
    private static int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
    /**
     * 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。
     * */
    private static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    boolean isStopTalk = false;

    public class AudioSend extends Thread {


        @Override
        public void run() {
            super.run();
            Socket socket = null;
            OutputStream os = null;
            AudioRecord recorder = null;
            try {
                socket = new Socket("192.168.1.106",8888);
                socket.setSoTimeout(5000);
                os = socket.getOutputStream();
                // 获得录音缓冲区大小
                int  bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz,channelConfig,audioFormat);
                Log.e("", "录音缓冲区大小"+bufferSizeInBytes);

                // 获得录音机对象
                recorder = new AudioRecord(audioSource,sampleRateInHz,channelConfig,audioFormat,bufferSizeInBytes);

                recorder.startRecording();// 开始录音
                byte[] readBuffer = new byte[640];// 录音缓冲区

                int length = 0;

                while (!isStopTalk) {
                    length = recorder.read(readBuffer, 0, 640);// 从mic读取音频数据
                    if (length > 0 && length % 2 == 0) {
                        os.write(readBuffer, 0, length);// 写入到输出流，把音频数据通过网络发送给对方
                    }
                }
                recorder.stop();
                recorder.release();
                recorder = null;
                os.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    AudioTrack audioTrack;
    public class AudioPlay extends Thread {
        public AudioPlay() {
        }

        @Override
        public void run() {
            super.run();
            try {
                Socket socket = null;
                socket = new Socket("192.168.1.106",8888);
                socket.setSoTimeout(5000);
                InputStream is = socket.getInputStream();
                int bufferSize = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
                audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize * 2, AudioTrack.MODE_STREAM);

                // 设置喇叭音量
                audioTrack.setStereoVolume(1.0f, 1.0f);

                // 开始播放声音
                audioTrack.play();
                byte[] audio = new byte[1024 *2];// 音频读取缓存
                int length = 0;

                while (!isStopTalk) {
                    length = is.read(audio);// 从网络读取音频数据
                    byte[] temp = audio.clone();
                    if (length > 0 && length % 2 == 0) {
                        // for(int
                        // i=0;i<length;i++)audio[i]=(byte)(audio[i]*2);//音频放大1倍
                        audioTrack.write(audio, 0, temp.length);// 播放音频数据
                    }
                }
                audioTrack.stop();
                audioTrack.release();
                audioTrack = null;
                is.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        AudioRecordUtil.getInstance().stopRecord();
    }
}
