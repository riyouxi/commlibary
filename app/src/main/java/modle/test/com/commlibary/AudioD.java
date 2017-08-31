package modle.test.com.commlibary;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by shanshan on 2017/8/30.
 */

public class AudioD {

    private String encodeType;
    private String srcPath;
    private String dstPath;
    private MediaCodec mediaDecode;
    private MediaCodec mediaEncode;
    private MediaExtractor mediaExtractor;
    private ByteBuffer[] decodeInputBuffers;
    private ByteBuffer[] decodeOutputBuffers;
    private ByteBuffer[] encodeInputBuffers;
    private ByteBuffer[] encodeOutputBuffers;
    private MediaCodec.BufferInfo decodeBufferInfo;
    private MediaCodec.BufferInfo encodeBufferInfo;
    private FileOutputStream fos;
    private BufferedOutputStream bos;
    private FileInputStream fis;
    private BufferedInputStream bis;
    private ArrayList<byte[]> chunkPCMDataContainer;//PCM数据块容器
    private long fileTotalSize;
    private long decodeSize;


    public void initAACMediaEncode() {
        try {
            encodeType = MediaFormat.MIMETYPE_AUDIO_AAC;
            MediaFormat encodeFormat = MediaFormat.createAudioFormat(encodeType, 44100, 2);//参数对应-> mime type、采样率、声道数
            encodeFormat.setInteger(MediaFormat.KEY_BIT_RATE, 96000);//比特率
            encodeFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
            encodeFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 100 * 1024);
            mediaEncode = MediaCodec.createEncoderByType(encodeType);
            mediaEncode.configure(encodeFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mediaEncode == null) {
            return;
        }
        mediaEncode.start();
        encodeInputBuffers=mediaEncode.getInputBuffers();
        encodeOutputBuffers=mediaEncode.getOutputBuffers();
        encodeBufferInfo=new MediaCodec.BufferInfo();
        try {
            fos = new FileOutputStream(new File("/sdcard/new.aac"));
            bos = new BufferedOutputStream(fos,2*1024);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 编码PCM数据 得到{@link #encodeType}格式的音频文件，并保存到{@link #dstPath}
     */
    public void dstAudioFormatFromPCM(byte[] data) {

        int inputIndex;
        ByteBuffer inputBuffer;
        int outputIndex;
        ByteBuffer outputBuffer;
        byte[] chunkAudio;
        int outBitSize;
        int outPacketSize;
        byte[] chunkPCM;

//        showLog("doEncode");
            chunkPCM=data;//获取解码器所在线程输出的数据 代码后边会贴上
            if (chunkPCM == null) {
                return;
            }
            inputIndex = mediaEncode.dequeueInputBuffer(-1);//同解码器
            inputBuffer = encodeInputBuffers[inputIndex];//同解码器
            inputBuffer.clear();//同解码器
            inputBuffer.limit(chunkPCM.length);
            inputBuffer.put(chunkPCM);//PCM数据填充给inputBuffer
            mediaEncode.queueInputBuffer(inputIndex, 0, chunkPCM.length, 0, 0);//通知编码器 编码

        outputIndex = mediaEncode.dequeueOutputBuffer(encodeBufferInfo, 10000);//同解码器
        while (outputIndex >= 0) {//同解码器

            outBitSize=encodeBufferInfo.size;
            outPacketSize=outBitSize+7;//7为ADTS头部的大小
            outputBuffer = encodeOutputBuffers[outputIndex];//拿到输出Buffer
            outputBuffer.position(encodeBufferInfo.offset);
            outputBuffer.limit(encodeBufferInfo.offset + outBitSize);
            chunkAudio = new byte[outPacketSize];
            addADTStoPacket(chunkAudio,outPacketSize);//添加ADTS 代码后面会贴上
            outputBuffer.get(chunkAudio, 7, outBitSize);//将编码得到的AAC数据 取出到byte[]中 偏移量offset=7 你懂得
            outputBuffer.position(encodeBufferInfo.offset);
//                showLog("outPacketSize:" + outPacketSize + " encodeOutBufferRemain:" + outputBuffer.remaining());
            try {
                bos.write(chunkAudio,0,chunkAudio.length);//BufferOutputStream 将文件保存到内存卡中 *.aac
            } catch (IOException e) {
                e.printStackTrace();
            }

            mediaEncode.releaseOutputBuffer(outputIndex,false);
            outputIndex = mediaEncode.dequeueOutputBuffer(encodeBufferInfo, 10000);

        }
    }

    /**
     * 添加ADTS头
     * @param packet
     * @param packetLen
     */
    private void addADTStoPacket(byte[] packet, int packetLen) {
        int profile = 2; // AAC LC
        int freqIdx = 4; // 44.1KHz
        int chanCfg = 2; // CPE


// fill in ADTS data
        packet[0] = (byte) 0xFF;
        packet[1] = (byte) 0xF9;
        packet[2] = (byte) (((profile - 1) << 6) + (freqIdx << 2) + (chanCfg >> 2));
        packet[3] = (byte) (((chanCfg & 3) << 6) + (packetLen >> 11));
        packet[4] = (byte) ((packetLen & 0x7FF) >> 3);
        packet[5] = (byte) (((packetLen & 7) << 5) + 0x1F);
        packet[6] = (byte) 0xFC;
    }


}
