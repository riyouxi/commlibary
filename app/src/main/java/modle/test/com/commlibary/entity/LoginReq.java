package modle.test.com.commlibary.entity;

/**
 * Created by Administrator on 2017/8/21.
 */

public class LoginReq {

    String userName;

    String loginType;

    String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
