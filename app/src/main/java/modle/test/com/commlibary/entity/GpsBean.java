package modle.test.com.commlibary.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/21.
 */

public class GpsBean implements Serializable {

    private String neckletName;

    private String imei;

    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

