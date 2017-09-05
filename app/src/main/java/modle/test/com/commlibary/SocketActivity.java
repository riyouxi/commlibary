package modle.test.com.commlibary;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/29.
 */

public class SocketActivity extends Activity {
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


        setContentView(R.layout.socket);
        mStart = (Button) findViewById(R.id.start);
        Socket socket = null;
        try {
            socket = new Socket("192.168.1.106", 8888);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Socket finalSocket = socket;
        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //2.获取输出流，向服务器端发送信息
                            OutputStream os= finalSocket.getOutputStream();//字节输出流
                            PrintWriter pw=new PrintWriter(os);//将输出流包装为打印流
                            pw.write("用户名：whf;密码：789");
                            pw.flush();
                            finalSocket.shutdownOutput();//关闭输出流
                            System.out.println("127.0.0.1 成功" + finalSocket.toString());
                        } catch (Exception e) {
                            System.err.println("127.0.0.1 失败");
                        }
                    }
                }).start();

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
