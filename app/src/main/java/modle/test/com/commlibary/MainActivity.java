package modle.test.com.commlibary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import modle.test.com.commlibary.presenter.impl.TestPresenterImpl;

public class MainActivity extends AppCompatActivity implements IShowView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TestPresenterImpl testPresenter = new TestPresenterImpl(MainActivity.this,this);
        testPresenter.loadData();
    }


    @Override
    public void showValue(Areponse areponse) {
        System.out.print("..................");
    }
}
