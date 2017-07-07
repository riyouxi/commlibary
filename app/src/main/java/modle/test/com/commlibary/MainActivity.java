package modle.test.com.commlibary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.commlibary.http.OpsRequest;
import com.commlibary.http.ResponseListener;
import com.commlibary.http.ResponseObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OpsRequest<Arequest,Areponse> ops = OpsRequest.createPost("http://apis.juhe.cn/idcard/index");
        Arequest aq = new Arequest();
        aq.setCardno("1212122");
        aq.setKey("78aac33babed4df27af43d30352c974c");
        ops.requestValue(aq).responseClass(Areponse.class).execute(new ResponseListener<Areponse>() {
            @Override
            public void onResponse(Areponse reponse) {
                System.out.print(reponse);
            }

            @Override
            public void onError(Exception error) {

            }
        },"");
    }


}
