package modle.test.com.commlibary;

/**
 * Created by Administrator on 2017/8/21.
 */

public class ServerHttpUrl {

    public static String getHostName(){

        return "http://192.168.1.126:8091";

    }

    public static String getLoginUrl(){

        return getHostName()+"/v1/v2/user/loginOther.api";

    }

    public static String getGpsList(){

        return  getHostName()+"/v1/v2/necklet/queryAllLastGps.api";
    }
}
