package modle.test.com.commlibary;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

/**
 * Created by Administrator on 2017/8/31.
 */
public class ReadAACFileThread extends Thread {

    //音频解码器
    private AACDecoderUtil audioUtil;
    //文件路径
    private String filePath;
    //文件读取完成标识
    private boolean isFinish = false;
    //这个值用于找到第一个帧头后，继续寻找第二个帧头，如果解码失败可以尝试缩小这个值
    private int FRAME_MIN_LEN = 50;
    //一般AAC帧大小不超过200k,如果解码失败可以尝试增大这个值
    private static int FRAME_MAX_LEN = 2 * 1024;
    //根据帧率获取的解码每帧需要休眠的时间,根据实际帧率进行操作
    private int PRE_FRAME_TIME = 1000 / 50;
    //记录获取的帧数
    private int count = 0;

    public ReadAACFileThread(String path) {
        this.audioUtil = new AACDecoderUtil();
        this.filePath = path;
        this.audioUtil.start();
    }

    @Override
    public void run() {
        super.run();
        File file = new File(filePath);
        //判断文件是否存在
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                //每次从文件读取的数据
                byte[] readData = new byte[2 * 1024];
                //开始时间
                BufferedInputStream is  = new BufferedInputStream(fis);
                //循环读取数据
                int len =0;
                while( (len = is.read(readData)) != -1 ){
                    audioUtil.decode(readData,0,len);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("ReadAACFileThread", "AllCount:" + count + "Error Count : " + audioUtil.getCount());
        } else {
            Log.e("ReadH264FileThread", "File not found");
        }
    }

    /**
     * 寻找指定buffer中AAC帧头的开始位置
     *
     * @param startIndex 开始的位置
     * @param data       数据
     * @param max        需要检测的最大值
     * @return
     */
    private int findHead(byte[] data, int startIndex, int max) {
        int i;
        for (i = startIndex; i <= max; i++) {
            //发现帧头
            if (isHead(data, i))
                break;
        }
        //检测到最大值，未发现帧头
        if (i == max) {
            i = -1;
        }
        return i;
    }

    /**
     * 判断aac帧头
     */
    private boolean isHead(byte[] data, int offset) {
        boolean result = false;
        if (data[offset] == (byte) 0xFF && data[offset + 1] == (byte) 0xF1
                && data[offset + 3] == (byte) 0x80) {
            result = true;
        }
        return result;
    }

    //修眠
    private void sleepThread(long startTime, long endTime) {
        //根据读文件和解码耗时，计算需要休眠的时间
        long time = PRE_FRAME_TIME - (endTime - startTime);
        if (time > 0) {
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}