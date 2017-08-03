package modle.test.com.commlibary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.commlibary.http.request.OpsRequest;
import com.commlibary.http.response.ResponseListener;
import com.commlibary.http.response.ResponseObject;

import modle.test.com.commlibary.presenter.impl.TestPresenterImpl;

public class MainActivity extends AppCompatActivity implements IShowView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TestPresenterImpl testPresenter = new TestPresenterImpl(MainActivity.this,this);
        testPresenter.loadData();
        OpsRequest<Arequest,Areponse> request= OpsRequest.createGet("http://119.23.134.46:8091/v1/");
        Arequest are = new Arequest();
        request.requestValue(are).responseClass(Areponse.class).execute(new ResponseListener<Areponse>() {
            @Override
            public void onResponse(ResponseObject<Areponse> reponse) {

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
