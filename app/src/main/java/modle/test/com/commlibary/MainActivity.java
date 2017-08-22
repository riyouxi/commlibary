package modle.test.com.commlibary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.commlibary.audio.AudioRecordUtil;
import com.commlibary.http.request.OpsRequest;
import com.commlibary.http.response.ResponseListener;
import com.commlibary.http.response.ResponseObject;

import modle.test.com.commlibary.entity.GpsReq;
import modle.test.com.commlibary.entity.GpsResp;
import modle.test.com.commlibary.entity.LoginReq;
import modle.test.com.commlibary.entity.LoginResp;
import modle.test.com.commlibary.presenter.impl.TestPresenterImpl;

public class MainActivity extends AppCompatActivity implements IShowView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TestPresenterImpl testPresenter = new TestPresenterImpl(MainActivity.this,this);
       // testPresenter.loadData();
//        LoginReq re = new LoginReq();
//        re.setUserName("user1");
//        re.setPassword("user1");
//        re.setLoginType("herdsman");
//        OpsRequest<LoginReq,LoginResp> request= OpsRequest.createPost(ServerHttpUrl.getLoginUrl());
//        request.requestValue(re).responseClass(LoginResp.class).execute(new ResponseListener<LoginResp>() {
//            @Override
//            public void onResponse(ResponseObject<LoginResp> reponse) {
//
//            }
//
//            @Override
//            public void onError(Exception error) {
//
//            }
//        },"");

        GpsReq req = new GpsReq();
        req.setToken("MTUwMzI4MTQyNjM2Nl8xX2hlcmRzbWFu");

        OpsRequest<GpsReq,GpsResp> request = OpsRequest.createPost(ServerHttpUrl.getGpsList());
        request.requestValue(req).responseClass(GpsResp.class).execute(new ResponseListener<GpsResp>() {
            @Override
            public void onResponse(ResponseObject<GpsResp> reponse) {

            }

            @Override
            public void onError(Exception error) {

            }
        },"");


    }


    @Override
    public void showValue(Areponse areponse) {
        System.out.print("..................");
    }
}
