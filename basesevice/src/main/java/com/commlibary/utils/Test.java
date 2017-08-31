package com.commlibary.utils;


import android.graphics.Bitmap;
import android.provider.Settings;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class Test {

    private ServerSocket server;
    private int port = 8888;
    // 使用ArrayList存储所有的Socket
    public List<Socket> socketList = new ArrayList<>();
    // 模仿保存在内存中的socket
    public Map<Integer, Socket> socketMap = new HashMap();


    public static void main(String[] args){
        new Test().talk();
    }

    public  Test(){
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
        }
    }

    public void talk() {
        try {
            System.out.println("等待用户接入 : ");
            // 使用accept()阻塞等待客户请求
            Socket socket = null;

            socket = server.accept();
            if (socketMap != null ) {
                socketMap.put(socket.getPort(), socket);
            }
            System.out.println("用户接入 : " + socket.getPort());
            // 开启一个子线程来等待另外的socket加入
            new Thread(new Runnable() {
                public void run() {
                    // 再次创建一个socket服务等待其他用户接入
                    talk();
                }
            }).start();
            // 从客户端获取信息
            // 装饰流BufferedReader封装输入流（接收客户端的流）
            BufferedInputStream bis = new BufferedInputStream(
                    socket.getInputStream());

            DataInputStream dis = new DataInputStream(bis);
            byte[] bytes = new byte[1024]; // 一次读取一个byte
            while (dis.read(bytes) != -1) {
                System.out.println(bytesToHexString(bytes));
                for (Map.Entry<Integer, Socket> entry : socketMap.entrySet()) {
                    if(entry.getKey().equals(socket.getPort())){
                        continue;
                    }
                    Socket temp = entry.getValue();
                    temp.getOutputStream().write(bytes);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static void doSomething(String ret) {
        System.out.println(ret);
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static String BytesHexString(byte[] b) {
        String ret = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }



}
