package modle.test.com.commlibary.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


/**
 * Created by ljt on 2016/7/21.
 * 有设备位置坐标
 */
public class Device implements Serializable {

    /**
     * 【车辆图标】
     */
    public String icon;
    /**
     * 【是否显示控制台：0，不显示，1：显示】
     */
    public String consoleFlag = "";
    /**
     * 【离线、在线、在线静止时长】
     */
    public String time;
    /**
     * 【设备名称】
     */
    public String neckletName;
    /**
     * 【状态  0：离线；1：在线静止；2：在线运动】
     */
    public String status = "2";
    /**
     * 【设备IMEI】
     */
    public String imei;
    /**
     * 【定位方式】
     * GPS("卫星定位","GPS"),LBS("基站定位","LBS"),WIFI("WIFI定位","WIFI");  BEACON：蓝牙定位
     */
    public String posType = "";
    /**
     * 【速度类型 1:正常  2:较快  3:暂无速度 0:设备图标为车辆】
     */
    public String speedType = "0";
    /**
     * 【设备类型】
     */
    public String mcType = "";
    /**
     * 【录音时长】
     */
    public String isSetTime = "";
    /**
     * 【经度】
     */
    public String longitude;
    /**
     * 【纬度】
     */
    public String latitude;

    /**
     * 定位时间
     */
    public String gpsTime;

    /**
     * 【是否有行程：0，有，1：没有】
     */
    public String tripFlag = "";
    /**
     * 【是否激活。 1：是 ，0：否】
     */
    public String activationFlag = "0";
    /**
     * 【是否过期  0：已过期，1：没过期】
     */
    public String expireFlag = "0";

    public String activeNum = "0";

    public int bitmap = 0;

    /**
     * 【方向】
     */
    public String direction = "0";

    public int getDirection() {
        int angle;
        try {
            angle = Integer.valueOf(direction);

        }catch (Exception e){
            return 0;
        }
        return angle;
    }

    public String recordFlag = "0";

    /**
     * 蓝牙定位的位置
     */
    public String locDesc = "";

    /**
     * 是否显示解绑:  1 有 0无
     */
    public String isBind = "0";

    /**
     * shutDownStatus   值为NORMAL  正常    值为DISABLE 停机
     */
    public String shutDownStatus = "NORMAL";

    public double latitudeAsDouble() {
        try {
            return Double.parseDouble(latitude);
        } catch (Exception e) {

        }
        return 0;
    }

    public double longitudeAsDouble() {
        try {
            return Double.parseDouble(longitude);
        } catch (Exception e) {

        }
        return 0;
    }


    public String getNeckletName() {
        return neckletName;
    }

    public void setNeckletName(String neckletName) {
        this.neckletName = neckletName;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }




}
