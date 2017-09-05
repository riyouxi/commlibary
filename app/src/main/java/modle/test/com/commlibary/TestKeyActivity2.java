package modle.test.com.commlibary;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/29.
 */

public class TestKeyActivity2 extends Activity {
    private ListView listView;

    private int keyCode;

    private Button mStart,mEnd,mStartPlay;

    private Map<Integer,Integer> data = new HashMap<>();

    boolean flag = false;
    int pos = 0;
    int index = 0;
    //private List<String> data = new ArrayList<String>();
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //listView = new ListView(this);
       // listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,getData()));
//        setContentView(listView);
//        listView.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
//                    pos-=1;
//                    listView.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            listView.setItemChecked(pos, true);
//                        }
//                    });
//                }else if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP){
//                    pos += 1;
//                    listView.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            listView.setItemChecked(pos, true);
//                        }
//                    });
//                }
//                return fa;
//            }
//        });
        setContentView(R.layout.test_key_activity2);
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,getData()));
        mStart = (Button) findViewById(R.id.start);
        mEnd = (Button) findViewById(R.id.stop);
        mStartPlay = (Button) findViewById(R.id.start_paly);
        mStartPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TestKeyActivity2.this,"df",Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if( i == KeyEvent.KEYCODE_DPAD_DOWN && listView.getSelectedItemPosition() ==0){
                    pos = 3;
                    mStartPlay.requestFocus();
                    return true;
                }else if(keyEvent.getAction() == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_DPAD_UP &&pos>=3){
                    if(index < listView.getCount()){
                        index+=1;
                        listView.setSelection(index);
                    }
                    return false;


                }else if(keyEvent.getAction() == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_DPAD_DOWN&&pos>=3){
                    if(index >0){
                        index -=1;
                        listView.setSelection(index);
                    }
                    return false;

                }
                return true;
            }
        });
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
            if(pos >0){
                pos -=1;
            }

            if(pos==1){
                mEnd.requestFocus();
            }else if(pos ==2){
                mStartPlay.requestFocus();
            }else if(pos == 0){
                mStart.requestFocus();
            }

            return true;
        }else if(keyCode == KeyEvent.KEYCODE_DPAD_UP){

            if(pos <4){
                pos+=1;
            }
            if(pos == 0){
                mStart.requestFocus();
            }else if(pos==1){
                mEnd.requestFocus();
            }else if(pos ==2){
                mStartPlay.requestFocus();
            }else if(pos ==3){
                listView.requestFocus();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private List<String> getData(){

        List<String> data = new ArrayList<String>();
        for(int i =0;i<20;i++){
            data.add("测试数据"+i);
        }
        return data;
    }


}
