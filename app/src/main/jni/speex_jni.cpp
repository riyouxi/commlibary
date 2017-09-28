#include <jni.h>
#include <speex/speex_preprocess.h>
#include <speex/speex_echo.h>
#include <android/log.h>
#include <stdio.h>
#include <modle_test_com_commlibary_Speex_jni.h>
#include <speex/speex_bits.h>
#include <speex/speex.h>

#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, "IPVOD", __VA_ARGS__))

static int codec_open = 0;

static int dec_frame_size;
static int enc_frame_size;

static SpeexBits ebits, dbits;
void *enc_state;
void *dec_state;

static JavaVM *gJavaVM;

extern "C"
JNIEXPORT jint JNICALL Java_modle_test_com_commlibary_Speex_1jni_open
        (JNIEnv *env, jobject obj, jint compression) {
    int tmp;
    //计算采样时长，即是10毫秒，还是20毫秒，还是30毫秒
    int nSampleTimeLong = (160 / (8000 / 100)) * 10;

    if (codec_open++ != 0)
        return (jint) 0;

    speex_bits_init(&ebits);
    speex_bits_init(&dbits);
    SpeexPreprocessState *m_st;
    SpeexEchoState *s_es;
    m_st = speex_preprocess_state_init(160, 8000);

    int denoise = 1;
    //噪声衰减db（负数，负值越小，降噪强度越大，同时失真越大）
    int noiseSuppress = -25;
    m_st = speex_preprocess_state_init(160 * (nSampleTimeLong / 10), 8000);
    speex_preprocess_ctl(m_st, SPEEX_PREPROCESS_SET_DENOISE, &denoise);  //降噪
    int code = speex_preprocess_ctl(m_st, SPEEX_PREPROCESS_SET_NOISE_SUPPRESS,
                                    &noiseSuppress);  //设置噪声最大衰减
    int agc = 1;
    int q = 1000;
    //speex_preprocess_ctl(m_st, SPEEX_PREPROCESS_SET_AGC, &agc);//增益
   // speex_preprocess_ctl(m_st, SPEEX_PREPROCESS_SET_AGC_LEVEL, &q);

    int vad = 1;
    int vadProbStart = 80;
    int vadProbContinue = 65;
    speex_preprocess_ctl(m_st, SPEEX_PREPROCESS_SET_VAD, &vad); //静音检测
    speex_preprocess_ctl(m_st, SPEEX_PREPROCESS_SET_PROB_START, &vadProbStart); //Set


    s_es = speex_echo_state_init(160, 8000);
    int __sample_rate = 4000;
    speex_echo_ctl(s_es, SPEEX_ECHO_SET_SAMPLING_RATE, &__sample_rate);//回音消除
    speex_preprocess_ctl(m_st, SPEEX_PREPROCESS_SET_ECHO_STATE, s_es);
    //=========================================

//		ses = speex_encoder_init( &speex_nb_mode);

    enc_state = speex_encoder_init(&speex_nb_mode);
    dec_state = speex_decoder_init(&speex_nb_mode);
    tmp = compression;
    speex_encoder_ctl(enc_state, SPEEX_SET_QUALITY, &tmp);
    speex_encoder_ctl(enc_state, SPEEX_GET_FRAME_SIZE, &enc_frame_size);
    speex_decoder_ctl(dec_state, SPEEX_GET_FRAME_SIZE, &dec_frame_size);

    return (jint) 0;
}

extern "C"
JNIEXPORT jint JNICALL Java_modle_test_com_commlibary_Speex_1jni_encode
        (JNIEnv *env, jobject obj, jshortArray lin, jint offset, jbyteArray encoded, jint size) {

    jshort buffer[enc_frame_size];
    jbyte output_buffer[enc_frame_size];
    int nsamples = (size - 1) / enc_frame_size + 1;
    int i, tot_bytes = 0;

    if (!codec_open)
        return 0;

    speex_bits_reset(&ebits);

    for (i = 0; i < nsamples; i++) {
        env->GetShortArrayRegion(lin, offset + i * enc_frame_size, enc_frame_size, buffer);
        speex_encode_int(enc_state, buffer, &ebits);
    }
    //env->GetShortArrayRegion(lin, offset, enc_frame_size, buffer);
    //speex_encode_int(enc_state, buffer, &ebits);

    tot_bytes = speex_bits_write(&ebits, (char *) output_buffer,
                                 enc_frame_size);
    env->SetByteArrayRegion(encoded, 0, tot_bytes,
                            output_buffer);

    return (jint) tot_bytes;
}

extern "C"
JNIEXPORT jint Java_modle_test_com_commlibary_Speex_1jni_decode
        (JNIEnv *env, jobject obj, jbyteArray encoded, jshortArray lin, jint size) {

    jbyte buffer[dec_frame_size];
    jshort output_buffer[dec_frame_size];
    jsize encoded_length = size;

    if (!codec_open)
        return 0;

    env->GetByteArrayRegion(encoded, 0, encoded_length, buffer);
    speex_bits_read_from(&dbits, (char *) buffer, encoded_length);
    speex_decode_int(dec_state, &dbits, output_buffer);
    env->SetShortArrayRegion(lin, 0, dec_frame_size,
                             output_buffer);

    return (jint) dec_frame_size;
}

extern "C"
JNIEXPORT jint JNICALL Java_modle_test_com_commlibary_Speex_1jni_getFrameSize
        (JNIEnv *env, jobject obj) {

    if (!codec_open)
        return 0;
    return (jint) enc_frame_size;

}

extern "C"
JNIEXPORT void JNICALL Java_modle_test_com_commlibary_Speex_1jni_close
        (JNIEnv *env, jobject obj) {

    if (--codec_open != 0)
        return;

    speex_bits_destroy(&ebits);
    speex_bits_destroy(&dbits);
    speex_decoder_destroy(dec_state);
    speex_encoder_destroy(enc_state);
}
