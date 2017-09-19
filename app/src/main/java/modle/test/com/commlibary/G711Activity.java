//package modle.test.com.commlibary;
//
//import android.app.Activity;
//import android.net.TrafficStats;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.support.annotation.Nullable;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.View;
//import android.widget.Button;
//
//import com.commlibary.audio.AudioRecordUtil;
//import com.commlibary.audio.api.audio.AudioCapturer;
//import com.commlibary.audio.api.audio.AudioDecoder;
//import com.commlibary.audio.api.audio.AudioEncoder;
//import com.commlibary.audio.api.audio.AudioPlayer;
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.Socket;
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Queue;
//import java.util.Vector;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
///**
// * Created by shanshan on 2017/8/28.
// */
//
//public class G711Activity extends Activity implements
//        AudioEncoder.OnAudioEncodedListener, AudioDecoder.OnAudioDecodedListener {
//
//    private boolean granted;
//    private Button mStart, mEnd, mStartPlay,mOther,mStopPlay;
//    private AudioEncoder mAudioEncoder;
//    private AudioDecoder mAudioDecoder;
//    private AudioCapturer mAudioCapturer;
//    private AudioPlayer mAudioPlayer;
//    private volatile boolean mIsTestingExit = false;
//
//    private volatile boolean mReadExit = false;
//
//    G711Decoder g711Decoder;
//
//
//    private List<byte[]> data = new ArrayList<>();
//    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
//
//    private AACDecoderUtil mAacUtil;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.audio_main_acitivity);
//        mStart = (Button) findViewById(R.id.start);
//        mEnd = (Button) findViewById(R.id.stop);
//        mStartPlay = (Button) findViewById(R.id.start_paly);
//        mOther = (Button) findViewById(R.id.other);
//        mStopPlay = (Button) findViewById(R.id.stop_paly);
//
//        mAudioCapturer = new AudioCapturer();
//        mAudioEncoder = new AudioEncoder();
//        mAudioDecoder = new AudioDecoder();
//        g711Decoder = new G711Decoder();
//
//        mAudioEncoder.setAudioEncodedListener(this);
//        mAudioDecoder.setAudioDecodedListener(this);
//
//        mStart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mIsTestingExit = false;
//               // mAudioEncoder.open();
//                //fixedThreadPool.execute(mEncodeRenderRunnable);
//                fixedThreadPool.execute(upload);
////                mAudioCapturer.startCapture();
//                mAudioCapturer.setOnAudioFrameCapturedListener(audioDataListener);
//
//
//            }
//        });
//
//        mEnd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mIsTestingExit = true;
//                mAudioCapturer.stopCapture();
//
//            }
//        });
//
//        mStartPlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mReadExit = false;
//                mAudioPlayer = new AudioPlayer();
//                mAudioPlayer.startPlayer();
//                //mAudioDecoder.open();
//               // fixedThreadPool.execute(mDecodeRenderRunnable);
//                fixedThreadPool.execute(new AudioPlayer2());
//                new DecodeG711().start();
//            }
//        });
//
//        mStopPlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mReadExit = true;
//                mAudioDecoder.close();
//                mAudioPlayer.stopPlayer();
//            }
//        });
//
//        mOther.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mAudioEncoder.open();
//                mAudioDecoder.open();
//                mAudioPlayer = new AudioPlayer();
//                mAudioPlayer.startPlayer();
//                new Thread(mEncodeRenderRunnable).start();
//
//                new Thread(mDecodeRenderRunnable).start();
////                mAudioCapturer.startCapture();
//                new Thread(upload).start();
//                new Thread(new AudioPlayer2()).start();
//            }
//        });
//
//
//    }
//
//
//
//
//    private Runnable mEncodeRenderRunnable = new Runnable() {
//        @Override
//        public void run() {
//            while (!mIsTestingExit) {
//                mAudioEncoder.retrieve();
//            }
//            mAudioEncoder.close();
//        }
//    };
//
//    private Runnable readFile = new Runnable() {
//        @Override
//        public void run() {
//
//            File file = new File("/sdcard/new.aac");
//            //判断文件是否存在
//            if (file.exists()) {
//                try {
//                    //当前帧长度
//                    int len = 0;
//                    //每次从文件读取的数据
//                    byte[] buffer = new byte[2*1024];
//                    DataInputStream ins;
//                    ins = new DataInputStream(new FileInputStream(file));
//                    while((len = ins.readInt()) != -1) {
//                        int newLen = ins.read(buffer,0,len);
//                        if(len > buffer.length){
//                            Log.e("error","buffer big");
//                        }
//                        if(newLen == len){
//                           // mAudioDecoder.decode(buffer, newLen);
//                        }
//
//                    }
//                } catch (FileNotFoundException e) {
//                    Log.e("异常",e.toString());
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    Log.e("异常",e.toString());
//                    e.printStackTrace();
//                }
//
//            }
//        }
//    };
//
//    private Runnable mDecodeRenderRunnable = new Runnable() {
//        @Override
//        public void run() {
//            while (!mReadExit) {
//                mAudioDecoder.retrieve();
//            }
//            mAudioDecoder.close();
//        }
//    };
//
//
//
//    @Override
//    public void onFrameDecoded(byte[] decoded, long presentationTimeUs) {
//        Log.e("执行播放",bytesToHexString(decoded));
//      //  mAudioPlayer.play(decoded, 0, decoded.length);
//    }
//
//    private AudioCapturer.OnAudioFrameCapturedListener audioDataListener = new AudioCapturer.OnAudioFrameCapturedListener(){
//
//    @Override
//    public void onAudioFrameCaptured(byte[] audioData) {
////        long presentationTimeUs = (System.nanoTime()) / 1000L;
////        mAudioEncoder.encode(audioData, presentationTimeUs);
//        byte[] temp = new byte[audioData.length];
//       // int count = g711Decoder.VoiceEncode(audioData,temp,audioData.length);
////        if(count >0){
////            data.add(temp);
//////            Log.e("编码：",bytesToHexString2(temp));
//////            byte[] tempDeco = new byte[temp.length];
//////            g711Decoder.VoiceDecode(temp,tempDeco,temp.length);
//////            mAudioPlayer.play(tempDeco,0,tempDeco.length);
////        }
//
//    }
//
//    };
//
//
//    @Override
//    public void onFrameEncoded(byte[] encoded, long presentationTimeUs) {
//        data.add(encoded);
////        try {
////            bos.writeInt(encoded.length);
////            bos.write(encoded,0,encoded.length);//BufferOutputStream 将文件保存到内存卡中 *.aac
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////         mAudioDecoder.decode(encoded,encoded.length);
//    }
//
//    private Runnable upload = new Runnable() {
//        @Override
//        public void run() {
//            while (!mIsTestingExit) {
//                if(data.size()>0){
//                    try {
//
//                        if(socket == null){
//                            socket = new Socket("192.168.1.109", 8888);
//                            socket.setSoTimeout(5000);
//                        }
//                        OutputStream os = socket.getOutputStream();
//                        DataOutputStream aos = new DataOutputStream(os);
//                        byte[] temp = data.get(0);
//                        Log.e("meaage","ss:"+bytesToHexString(temp));
//                        aos.writeInt(temp.length);
//                        aos.write(temp,0,temp.length);
//                        data.remove(0);
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        }
//    };
//
//   static  double totalWifi;
//
//    public static double getWifiTraffic(){
//        double rtotalGprs = TrafficStats.getTotalRxBytes();
//        double ttotalGprs = TrafficStats.getTotalTxBytes();
//        double rgprs = TrafficStats.getMobileRxBytes();
//        double tgprs = TrafficStats.getMobileTxBytes();
//        double rwifi = rtotalGprs - rgprs;
//        double twifi = ttotalGprs - tgprs;
//        totalWifi = rwifi + twifi;
//        return totalWifi;
//
//    }
//
//    CountDownTimer cdt = new CountDownTimer(20000, 1000) {
//        @Override
//        public void onTick(long millisUntilFinished) {
//           Log.e("totalwifi",getWifiTraffic()+"");
//        }
//        @Override
//        public void onFinish() {
//
//        }
//    };
//
//    public static String bytesToHexString2(byte[] src) {
//        StringBuilder stringBuilder = new StringBuilder("");
//        if (src == null || src.length <= 0) {
//            return null;
//        }
//        for (int i = 0; i < src.length; i++) {
//            int v = src[i] & 0xFF;
//            String hv = Integer.toHexString(v);
//            if (hv.length() < 2) {
//                stringBuilder.append(0);
//            }
//            stringBuilder.append(hv);
//        }
//        return stringBuilder.toString();
//    }
//
//
//
//    public static String bytesToHexString(byte[] src) {
//        StringBuilder stringBuilder = new StringBuilder("");
//        if (src == null || src.length <= 0) {
//            return null;
//        }
//        for (int i = 0; i < src.length; i++) {
////            int v = src[i] & 0xFF;
////            String hv = Integer.toHexString(v);
////            if (hv.length() < 2) {
////                stringBuilder.append(0);
////            }
//            stringBuilder.append(src[i]+",");
//        }
//        return stringBuilder.toString();
//    }
//
//    class DecodeG711 extends  Thread{
//        @Override
//        public void run() {
//            super.run();
//
//            while (!mReadExit){
//                if(queue.size()>0){
//                    byte[] temp = queue.poll();
//                    byte[] dstBuff = new byte[temp.length];
//                    g711Decoder.VoiceDecode(temp,dstBuff,temp.length);
////                    mAudioPlayer.play(dstBuff,0,dstBuff.length);
//                }
//            }
//        }
//    }
//
//
//    private Queue<byte[]> queue = new LinkedList<>();
//    Socket socket = null;
//    public class AudioPlayer2 implements Runnable {
//
//        private Vector<byte[]> tmpbytes = new Vector<byte[]>();
//        private int fileLength = -1;
//
//        private byte[] startBuffer = {18,16};
//
//
//
//        AudioPlayer2() {
//        }
//
//        private void splitByte(byte[] parambytes) {
//            if (parambytes != null) {
//                System.out.println(parambytes.length);
//                if (parambytes.length > 4) {
//                    byte[] head = new byte[4];  //单包长度
//                    System.arraycopy(parambytes, 0, head, 0, 4);
//                    int bodyLength;
//
//                    if (fileLength == -1) {
//                        bodyLength = getint(head);
//                        fileLength = bodyLength;
//                    } else {
//                        bodyLength = fileLength;
//                    }
//                    System.out.println("bodyLength:" + bodyLength + "paramlength:" + parambytes.length);
//
//                    if (bodyLength <= parambytes.length - 4) {
//                        byte[] body = new byte[bodyLength];
//                        System.arraycopy(parambytes, 4, body, 0, bodyLength);
//
//                        int reslutLen = parambytes.length - 4 - bodyLength;
//                        //System.out.println(bytesToHexString(body));
//                        System.out.println(bytesToHexString(body));
//                        if (reslutLen == 0) {
//                            fileLength = -1;
//                            tmpbytes.clear();
//                            //mAudioDecoder.decode(body,body.length);
//                            queue.add(body);
//                            byte[] tempDeco = new byte[body.length];
//                            g711Decoder.VoiceDecode(body,tempDeco,body.length);
//
//                            //splitByte(null);
//                        } else {
//                            if (reslutLen > 0) {
//                                fileLength = -1;
//                                tmpbytes.clear();
//                               //AudioDecoder.decode(body,body.length);
//                                queue.add(body);
//                                byte[] temp = new byte[reslutLen];
//                                System.arraycopy(parambytes, 4 + bodyLength, temp, 0, reslutLen);
//                                splitByte(temp);
//                            }
//
//
//                        }
//                    } else {
//                        tmpbytes.clear();
//                        System.out.println("后" + parambytes);
//                        tmpbytes.add(parambytes);
//                    }
//
//                } else {
//                    tmpbytes.clear();
//                    System.out.println("后" + parambytes);
//                    tmpbytes.add(parambytes);
//                }
//            }
//        }
//
//        public final int getint(byte[] paramArrayOfByte) {
//
//            int reslut = (paramArrayOfByte[0] & 0xFF) << 24
//                    | (paramArrayOfByte[1] & 0xFF) << 16
//                    | (paramArrayOfByte[2] & 0xFF) << 8
//                    | paramArrayOfByte[3] & 0xFF;
//            return reslut;
//        }
//
//        @Override
//        public void run() {
//            try {
//                if (socket == null) {
//                    socket = new Socket("192.168.1.109", 8888);
//                    socket.setSoTimeout(5000);
//
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            while (!mReadExit) {
//                try {
//
//                    //当前帧长度
//                    int len = 0;
//                    //每次从文件读取的数据
//                    InputStream dis = socket.getInputStream();
//                    byte[] buffer = new byte[ 1024 *10];
//                    while (!mReadExit && (len = dis.read(buffer)) != 0) {
//                        if (tmpbytes.size() > 0 && len > 0) {
//                            int oldByteslen = tmpbytes.get(0).length;
//                            System.out.println("oldlength:" + oldByteslen + "len:" + len);
//                            int currenLenght = oldByteslen + len;
//                            byte[] currentbytes = new byte[currenLenght];
//                            System.out.println("前" + tmpbytes.get(0));
//                            System.arraycopy(tmpbytes.get(0), 0, currentbytes, 0, oldByteslen);
//                            System.arraycopy(buffer, 0, currentbytes, oldByteslen, len);
//                            splitByte(currentbytes);
//                        } else {
//                                if (tmpbytes.size() == 0 && len > 0) {
//                                    byte[] temp = new byte[len];
//                                    System.arraycopy(buffer, 0, temp, 0, len);
//
//                                    splitByte(temp);
//                                }
//
//
//                        }
//
//                       // socket.close();
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
////                    DataInputStream dis = new DataInputStream(socket.getInputStream()) ;
////                    while ((len = dis.read(buffer)) != -1) {
////                        Log.e("error", "buffer big");
////                        int newLen = dis.read(buffer, 0, len);
////                        if (len > buffer.length) {
////                            Log.e("error", "buffer big");
////                        }
////                        if (newLen == len) {
////                            mAudioDecoder.decode(buffer, newLen);
////                        }
////
////                    }
//            }
//        }
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        return super.onKeyDown(keyCode, event);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        AudioRecordUtil.getInstance().stopRecord();
//    }
//}
