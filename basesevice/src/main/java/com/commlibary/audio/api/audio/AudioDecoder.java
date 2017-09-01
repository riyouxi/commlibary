/*
 *  COPYRIGHT NOTICE
 *  Copyright (C) 2016, Jhuster <lujun.hust@gmail.com>
 *  https://github.com/Jhuster/AudioDemo
 *
 *  @license under the Apache License, Version 2.0
 *
 *  @file    AudioDecoder.java
 *
 *  @version 1.0
 *  @author  Jhuster
 *  @date    2016/04/02
 */
package com.commlibary.audio.api.audio;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class AudioDecoder {

    private static final String TAG = AudioDecoder.class.getSimpleName();

    private static final String DEFAULT_MIME_TYPE = "audio/mp4a-latm";
    private static final int DEFAULT_CHANNEL_NUM = 2;
    private static final int DEFAULT_SAMPLE_RATE = 44100;
    private static final int DEFAULT_MAX_BUFFER_SIZE = 1024*2;

    private MediaCodec mMediaCodec;
    private OnAudioDecodedListener mAudioDecodedListener;
    private boolean mIsOpened = false;
    private boolean mIsFirstFrame = true;

    public interface OnAudioDecodedListener {
        void onFrameDecoded(byte[] decoded, long presentationTimeUs);
    }

    public boolean open() {
        if (mIsOpened) {
            return true;
        }
        return open(DEFAULT_SAMPLE_RATE, DEFAULT_CHANNEL_NUM, DEFAULT_MAX_BUFFER_SIZE);
    }

    public boolean open(int samplerate, int channels, int maxBufferSize) {
        Log.i(TAG, "open audio decoder: " + samplerate + ", " + channels + ", " + maxBufferSize);
        if (mIsOpened) {
            return true;
        }

        try {
            mMediaCodec = MediaCodec.createDecoderByType(DEFAULT_MIME_TYPE);
            MediaFormat format = new MediaFormat();
            format.setString(MediaFormat.KEY_MIME, DEFAULT_MIME_TYPE);
            format.setInteger(MediaFormat.KEY_CHANNEL_COUNT, channels);
            format.setInteger(MediaFormat.KEY_SAMPLE_RATE, samplerate);
            format.setInteger(MediaFormat.KEY_BIT_RATE, 96000);//比特率
            format.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
            format.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, maxBufferSize);
            mMediaCodec.configure(format, null, null, 0);
            mMediaCodec.start();
            mIsOpened = true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        Log.i(TAG, "open audio decoder success !");
        return true;
    }

    public void close() {
        Log.i(TAG, "close audio decoder +");
        if (!mIsOpened) {
            return;
        }
        mMediaCodec.stop();
        mMediaCodec.release();
        mMediaCodec = null;
        mIsOpened = false;
        Log.i(TAG, "close audio decoder -");
    }

    public boolean isOpened() {
        return mIsOpened;
    }

    public void setAudioDecodedListener(OnAudioDecodedListener listener) {
        mAudioDecodedListener = listener;
    }

    public synchronized boolean decode(byte[] input, int length) {
        Log.e("解码之前的数据：",bytesToHexString(input));
        if (!mIsOpened) {
            return false;
        }

        try {
            ByteBuffer[] inputBuffers = mMediaCodec.getInputBuffers();
            int inputBufferIndex = mMediaCodec.dequeueInputBuffer(-1);
            if (inputBufferIndex >= 0) {
                ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
                inputBuffer.clear();
                inputBuffer.put(input,0,length);
                mMediaCodec.queueInputBuffer(inputBufferIndex, 0, length, 0, 0);
            }
        } catch (Throwable t) {
            Log.e("异常",t.toString());
            t.printStackTrace();
            return false;
        }
        return false;
    }

    public synchronized boolean retrieve() {
        if (!mIsOpened) {
            return false;
        }

        try {
            ByteBuffer[] outputBuffers = mMediaCodec.getOutputBuffers();
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            int outputBufferIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, 1000);

            if (outputBufferIndex >= 0) {
                Log.d(TAG, "decode retrieve frame " + bufferInfo.size);
                ByteBuffer outputBuffer = outputBuffers[outputBufferIndex];
                byte[] outData = new byte[bufferInfo.size];
                outputBuffer.get(outData);
                Log.e("解码后的数据：",bytesToHexString(outData));
                if (mAudioDecodedListener != null) {
                    mAudioDecodedListener.onFrameDecoded(outData, bufferInfo.presentationTimeUs);
                }

                Log.d(TAG, " " + outData.toString());
                mMediaCodec.releaseOutputBuffer(outputBufferIndex, false);
            }
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
        return true;
    }

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
}
