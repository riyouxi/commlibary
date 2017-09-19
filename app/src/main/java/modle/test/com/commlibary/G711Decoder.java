package modle.test.com.commlibary;

/**
 * 描述：
 * 作者：chezi008 on 2017/3/10 9:59
 * 邮箱：chezi008@163.com
 */

public class G711Decoder {

    static {
        System.loadLibrary("G711");
    }
    public native int VoiceEncode(byte[] src,byte[] dest,int srclen);
    public native int VoiceDecode(byte[] src,byte[] dest,int srclen);
}
