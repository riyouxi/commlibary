package modle.test.com.commlibary;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

import modle.test.com.commlibary.db.TestIml;
import modle.test.com.commlibary.entity.User;

public class DbActivity extends AppCompatActivity {

    private Button btn1, btn2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.db_layout);
        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(onClickListener);
        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn1:
                    User user = new User();
                    user.setName("测试1");
                    TestIml.insertUser(user);
                    break;
                case R.id.btn2:
                    List<User> data = TestIml.getALlData();
                    for (User u : data) {
                        Log.e("db", u.getName());
                    }
                    break;
            }
        }
    };
}
