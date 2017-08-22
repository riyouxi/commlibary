package modle.test.com.commlibary.presenter.impl;


import android.content.Context;

import com.commlibary.http.request.OpsRequest;
import com.commlibary.http.response.ResponseListener;
import com.commlibary.http.response.ResponseObject;

import modle.test.com.commlibary.Areponse;
import modle.test.com.commlibary.Arequest;
import modle.test.com.commlibary.IShowView;
import modle.test.com.commlibary.presenter.ITestPresenter;

public class TestPresenterImpl implements ITestPresenter {

    private IShowView mIshowView;
    public TestPresenterImpl(Context context, IShowView iShowView){
        mIshowView = iShowView;
    }

    @Override
    public void loadData() {

        OpsRequest<Arequest,Areponse> ops = OpsRequest.createPost("http://apis.juhe.cn/idcard/index");
        Arequest aq = new Arequest();
        aq.setCardno("1212122");
        aq.setKey("78aac33babed4df27af43d30352c974c");
        ops.requestValue(aq).responseClass(Areponse.class).execute(new ResponseListener<Areponse>() {
            @Override
            public void onResponse(ResponseObject<Areponse> reponse) {
                mIshowView.showValue(reponse.getResult());
            }

            @Override
            public void onError(Exception error) {

            }
        },"");
    }
}
