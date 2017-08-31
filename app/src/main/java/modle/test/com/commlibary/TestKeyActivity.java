package modle.test.com.commlibary;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/29.
 */

public class TestKeyActivity extends Activity {
    private ListView listView;

    private int keyCode;

    private Map<Integer,Integer> data = new HashMap<>();

    boolean flag = false;
    //private List<String> data = new ArrayList<String>();
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        listView = new ListView(this);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,getData()));
        setContentView(listView);
        listView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                return flag;
            }
        });
        //setContentView(R.layout.test_key_activity);
    }



    private List<String> getData(){

        List<String> data = new ArrayList<String>();
        for(int i =0;i<20;i++){
            data.add("测试数据"+i);
        }
        return data;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("keyCode","keyCode"+keyCode);

        return flag;
    }

    private byte[] bytes = new byte[0];

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.e("dispatchKeyEvent","dispatchKeyEvent"+event.getKeyCode() +"actioncode:"+event.getAction());
        if(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN){
            if(data.size() >2&& event.getAction() == KeyEvent.ACTION_UP){
                flag = true;
                return true;
            }
            if(data.containsKey(event.getAction())){
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    data.remove(KeyEvent.ACTION_DOWN);
                }else if(event.getAction() == KeyEvent.ACTION_UP){
                    data.remove(KeyEvent.ACTION_UP);
                }
                flag = false;
                return super.dispatchKeyEvent(event);
            }
            try
            {
                if(event.getAction() == KeyEvent.ACTION_UP){
                    data.put( KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_DOWN);
                    data.put( KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_DOWN);
                    String keyCommand = "input keyevent " + KeyEvent.KEYCODE_DPAD_UP;
                    Runtime runtime = Runtime.getRuntime();
                    Process proc = runtime.exec(keyCommand);
                }
                flag = true;
                return true;

            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }else if(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP){
            if(data.size() >2 && event.getAction() == KeyEvent.ACTION_UP){
                flag = true;
                return true;
            }
            if(data.containsKey(event.getAction())){
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    data.remove(KeyEvent.ACTION_DOWN);
                }else if(event.getAction() == KeyEvent.ACTION_UP){
                    data.remove(KeyEvent.ACTION_UP);
                }
                flag = false;
                return super.dispatchKeyEvent(event);
            }
                try
                {
                    if(event.getAction() == KeyEvent.ACTION_UP ){
                        data.put( KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_DOWN);
                        data.put( KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_DOWN);
                        String keyCommand = "input keyevent " + KeyEvent.KEYCODE_DPAD_DOWN;
                        Runtime runtime = Runtime.getRuntime();
                        Process proc = runtime.exec(keyCommand);
                    }
                    flag = true;
                    return true;

                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            }

        }
        flag = false;
        return super.dispatchKeyEvent(event);
    }
}
