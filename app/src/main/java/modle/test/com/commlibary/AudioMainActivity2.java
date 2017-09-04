package modle.test.com.commlibary;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.audiofx.LoudnessEnhancer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.commlibary.audio.AudioRecordUtil;
import com.commlibary.audio.api.audio.AudioCapturer;
import com.commlibary.audio.api.audio.AudioDecoder;
import com.commlibary.audio.api.audio.AudioEncoder;
import com.commlibary.audio.api.audio.AudioPlayer;
import com.commlibary.audio.tester.AudioCodecTester;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;

/**
 * Created by shanshan on 2017/8/28.
 */

public class AudioMainActivity2 extends Activity implements AudioCapturer.OnAudioFrameCapturedListener,
        AudioEncoder.OnAudioEncodedListener, AudioDecoder.OnAudioDecodedListener {

    private boolean granted;
    private Button mStart, mEnd, mStartPlay,mOther;
    private AudioEncoder mAudioEncoder;
    private AudioDecoder mAudioDecoder;
    private AudioCapturer mAudioCapturer;
    private AudioPlayer mAudioPlayer;
    private volatile boolean mIsTestingExit = false;
    /**
     * 音频获取源
     */
    private int audioSource = MediaRecorder.AudioSource.MIC;
    /**
     * 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
     */
    private static int sampleRateInHz = 44100;
    /**
     * 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
     */
    private static int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
    /**
     * 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。
     */
    private static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    boolean isStopTalk = false;

    private FileOutputStream fos;
    private DataOutputStream bos;

    private List<byte[]> data = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_main_acitivity);
        mStart = (Button) findViewById(R.id.start);
        mEnd = (Button) findViewById(R.id.stop);
        mStartPlay = (Button) findViewById(R.id.start_paly);
        mOther = (Button) findViewById(R.id.other);

        mAudioCapturer = new AudioCapturer();
        mAudioEncoder = new AudioEncoder();
        mAudioDecoder = new AudioDecoder();

        mAudioEncoder.setAudioEncodedListener(this);
        mAudioDecoder.setAudioDecodedListener(this);
        mAudioCapturer.setOnAudioFrameCapturedListener(this);


        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    fos = new FileOutputStream(new File("/sdcard/new.aac"));
                    bos = new DataOutputStream(fos);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                mAudioEncoder.open();
                new Thread(mEncodeRenderRunnable).start();
                mAudioCapturer.startCapture();
                new Thread(upload).start();

            }
        });

        mEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStopTalk = false;
                mAudioCapturer.stopCapture();
            }
        });

        mStartPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAudioPlayer = new AudioPlayer();
                mAudioPlayer.startPlayer();
                mAudioDecoder.open();
//                new Thread(readFile).start();
                new Thread(new AudioPlayer2()).start();
                new Thread(mDecodeRenderRunnable).start();
            }
        });

        mOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AudioCodecTester().startTesting();
            }
        });


    }




    private Runnable mEncodeRenderRunnable = new Runnable() {
        @Override
        public void run() {
            while (!mIsTestingExit) {
                mAudioEncoder.retrieve();
            }
            mAudioEncoder.close();
        }
    };

    private Runnable readFile = new Runnable() {
        @Override
        public void run() {

            File file = new File("/sdcard/new.aac");
            //判断文件是否存在
            if (file.exists()) {
                try {
                    //当前帧长度
                    int len = 0;
                    //每次从文件读取的数据
                    byte[] buffer = new byte[2*1024];
                    DataInputStream ins;
                    ins = new DataInputStream(new FileInputStream(file));
                    while((len = ins.readInt()) != -1) {
                        int newLen = ins.read(buffer,0,len);
                        if(len > buffer.length){
                            Log.e("error","buffer big");
                        }
                        if(newLen == len){
                            mAudioDecoder.decode(buffer, newLen);
                        }

                    }
                } catch (FileNotFoundException e) {
                    Log.e("异常",e.toString());
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e("异常",e.toString());
                    e.printStackTrace();
                }

            }
        }
    };

    private Runnable mDecodeRenderRunnable = new Runnable() {
        @Override
        public void run() {
            while (!mIsTestingExit) {
                mAudioDecoder.retrieve();
            }
            mAudioDecoder.close();
        }
    };



    @Override
    public void onFrameDecoded(byte[] decoded, long presentationTimeUs) {
        Log.e("执行播放",bytesToHexString(decoded));
        mAudioPlayer.play(decoded, 0, decoded.length);
    }



    @Override
    public void onAudioFrameCaptured(byte[] audioData) {
        long presentationTimeUs = (System.nanoTime()) / 1000L;
        mAudioEncoder.encode(audioData, presentationTimeUs);
    }

    @Override
    public void onFrameEncoded(byte[] encoded, long presentationTimeUs) {

        data.add(encoded);
//        try {
//            bos.writeInt(encoded.length);
//            bos.write(encoded,0,encoded.length);//BufferOutputStream 将文件保存到内存卡中 *.aac
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        // mAudioDecoder.decode(encoded, presentationTimeUs);
    }

    Socket socket2 = null;
    private Runnable upload = new Runnable() {
        @Override
        public void run() {
            while (!mIsTestingExit) {
                if(data.size()>0){
                    try {

                        if(socket2 == null){
                            socket2 = new Socket("192.168.1.16", 8888);
                            socket2.setSoTimeout(5000);
                        }
                        OutputStream os = socket2.getOutputStream();
                        DataOutputStream aos = new DataOutputStream(os);
                        byte[] temp = data.get(0);
                        aos.writeInt(temp.length);
                        Log.e("length",temp.length+"");
                        Log.e("meaage","ss:"+bytesToHexString(temp));
                        aos.write(temp,0,temp.length);
                        data.remove(0);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    };


    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    Socket socket = null;
    public class AudioPlayer2 implements Runnable {

        private Vector<byte[]> tmpbytes = new Vector<byte[]>();
        private int fileLength = -1;

        AudioPlayer2() {
        }

        private void splitByte(byte[] parambytes) {
            if (parambytes != null) {
                System.out.println(parambytes.length);
                if (parambytes.length > 4) {
                    byte[] head = new byte[4];  //单包长度
                    System.arraycopy(parambytes, 0, head, 0, 4);
                    int bodyLength;

                    if (fileLength == -1) {
                        bodyLength = getint(head);
                        fileLength = bodyLength;
                    } else {
                        bodyLength = fileLength;
                    }
                    System.out.println("bodyLength:" + bodyLength + "paramlength:" + parambytes.length);

                    if (bodyLength <= parambytes.length - 4) {
                        byte[] body = new byte[bodyLength];
                        System.arraycopy(parambytes, 4, body, 0, bodyLength);

                        int reslutLen = parambytes.length - 4 - bodyLength;
                        //System.out.println(bytesToHexString(body));
                        if (reslutLen == 0) {
                            fileLength = -1;
                            tmpbytes.clear();
                            mAudioDecoder.decode(body,body.length);
                            Log.e("meaage",bytesToHexString(body));

                            //splitByte(null);
                        } else {
                            if (reslutLen > 0) {
                                fileLength = -1;
                                mAudioDecoder.decode(body,body.length);
                                Log.e("meaage",bytesToHexString(body));
                                System.out.println(bytesToHexString(body));
                                byte[] temp = new byte[reslutLen];
                                System.arraycopy(parambytes, 4 + bodyLength, temp, 0, reslutLen);
                                splitByte(temp);
                            } else if (reslutLen < 0) {
                                System.out.println("..............");
                                byte[] temp = new byte[reslutLen];
                                System.arraycopy(parambytes, 4 + bodyLength, temp, 0, reslutLen);
                                tmpbytes.clear();
                                tmpbytes.add(temp);
                            }


                        }
                    } else {
                        tmpbytes.clear();
                        System.out.println("后" + parambytes);
                        tmpbytes.add(parambytes);
                    }

                } else {
                    tmpbytes.clear();
                    System.out.println("后" + parambytes);
                    tmpbytes.add(parambytes);
                }
            }
        }

        public final int getint(byte[] paramArrayOfByte) {

            int reslut = (paramArrayOfByte[0] & 0xFF) << 24
                    | (paramArrayOfByte[1] & 0xFF) << 16
                    | (paramArrayOfByte[2] & 0xFF) << 8
                    | paramArrayOfByte[3] & 0xFF;
            return reslut;
        }

        @Override
        public void run() {
            try {
                if (socket == null) {
                    socket = new Socket("192.168.1.16", 8888);
                    socket.setSoTimeout(5000);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (!mIsTestingExit) {
                try {

                    //当前帧长度
                    int len = 0;
                    //每次从文件读取的数据
                    InputStream dis = socket.getInputStream();
                    byte[] buffer = new byte[2 * 1024];
                    while ((len = dis.read(buffer)) != 0) {
                        if (tmpbytes.size() > 0 && len > 0) {
                            int oldByteslen = tmpbytes.get(0).length;
                            System.out.println("oldlength:" + oldByteslen + "len:" + len);
                            int currenLenght = oldByteslen + len;
                            byte[] currentbytes = new byte[currenLenght];
                            System.out.println("前" + tmpbytes.get(0));
                            System.arraycopy(tmpbytes.get(0), 0, currentbytes, 0, oldByteslen);
                            System.arraycopy(buffer, 0, currentbytes, oldByteslen, len);
                            splitByte(currentbytes);
                        } else {
                            if (tmpbytes.size() == 0 && len > 0) {
                                byte[] temp = new byte[len];
                                System.arraycopy(buffer, 0, temp, 0, len);
                                splitByte(temp);
                            }

                        }

                       // socket.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
//                    DataInputStream dis = new DataInputStream(socket.getInputStream()) ;
//                    while ((len = dis.read(buffer)) != -1) {
//                        Log.e("error", "buffer big");
//                        int newLen = dis.read(buffer, 0, len);
//                        if (len > buffer.length) {
//                            Log.e("error", "buffer big");
//                        }
//                        if (newLen == len) {
//                            mAudioDecoder.decode(buffer, newLen);
//                        }
//
//                    }
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
