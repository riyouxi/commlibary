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
        OpsRequest<Arequest,Areponse> ops = OpsRequest.createPost("http://192.168.19.60:8000/blog/");
        Arequest aq = new Arequest();
        ops.requestValue(aq).responseClass(Areponse.class).execute(new ResponseListener<Areponse>() {
            @Override
            public void onResponse(ResponseObject<Areponse> reponse) {
            }

            @Override
            public void onError(Exception error) {

            }
        },"");
    }


}
