package com.commlibary.utils;

/**
 * Created by Administrator on 2017/8/30.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TheadServer implements Runnable {
    private Socket s = null;
    public InputStream ins;
    public TheadServer(ServerSocket ss ) throws IOException{
        System.out.println("start thread");
    }

    @Override
    public void run(){


    }
}