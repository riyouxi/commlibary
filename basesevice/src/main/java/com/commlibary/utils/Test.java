package com.commlibary.utils;




import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {

    // 使用ArrayList存储所有的Socket
    // 模仿保存在内存中的socket
    public Map<Integer, Socket> socketMap = new HashMap();
    private static ExecutorService ThradPool = Executors.newSingleThreadExecutor();


    public static void main(String[] args) {
        new Test();
    }

    @SuppressWarnings("resource")
	public Test() {
        try {
            // 为了简单起见，所有的异常信息都往外抛
            int port = 8888;
            // 定义一个ServiceSocket监听在端口8899上
            ServerSocket server = new ServerSocket(port);
            System.out.println("等待与客户端建立连接...");
            while (true) {
                // server尝试接收其他Socket的连接请求，server的accept方法是阻塞式的
                Socket socket = server.accept();
                System.out.println("用户接入 : " + socket.getPort());
                socketMap.put(socket.getPort(), socket);
                /**
                 * 我们的服务端处理客户端的连接请求是同步进行的， 每次接收到来自客户端的连接请求后，
                 * 都要先跟当前的客户端通信完之后才能再处理下一个连接请求。 这在并发比较多的情况下会严重影响程序的性能，
                 * 为此，我们可以把它改为如下这种异步处理与客户端通信的方式
                 */
                // 每接收到一个Socket就建立一个新的线程来处理它
                new Thread(new Task(socket)).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理Socket请求的线程类
     */
    class Task implements Runnable {
        private Vector<byte[]> tmpbytes = new Vector<byte[]>();
        private int fileLength = -1;

        private Socket socket;

        /**
         * 构造函数
         */
        public Task(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                handlerSocket();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        private void splitByte(byte[] parambytes){
        	if(parambytes!=null){
        		 System.out.println(parambytes.length);
        		if(parambytes.length>4){
        			byte[] head = new byte[4];  //单包长度
                    System.arraycopy(parambytes, 0, head, 0, 4);
                    int bodyLength;
                    
                    if(fileLength == -1){
                           bodyLength = getint(head);
                           fileLength = bodyLength;
                    }else{
                    	bodyLength = fileLength;
                    }
                    System.out.println("bodyLength:"+bodyLength+"paramlength:"+parambytes.length);
                   
                    if(bodyLength <= parambytes.length -4){
                    	byte[] body = new byte[bodyLength];
                    	System.arraycopy(parambytes, 4, body, 0, bodyLength);
                    	int reslutLen = parambytes.length-4-bodyLength;
                    	//System.out.println(bytesToHexString(body));
                    	if(reslutLen == 0){
                    		fileLength = -1;
                    		tmpbytes.clear();
                    		System.out.println("socketSize"+socketMap.size());
                    		 for (Map.Entry<Integer, Socket> entry : socketMap.entrySet()) {
                                 if(entry.getKey().equals(socket.getPort())){
                                    continue;
                                 }
                                 Socket temp = entry.getValue();
                                  try {
                                	  DataOutputStream is =new DataOutputStream(temp.getOutputStream());
                                	  is.writeInt(body.length);
                                	  is.write(body);
                                	  }catch (Exception e) {
										// TODO: handle exception
									}
                    		 }
                    		//splitByte(null);
                    	}else{
                    		if(reslutLen >0){
                    			fileLength = -1;
                                System.out.println("socketSize"+socketMap.size());
                    			for (Map.Entry<Integer, Socket> entry : socketMap.entrySet()) {
                                    if(entry.getKey().equals(socket.getPort())){
                                       continue;
                                    }
                                    Socket temp = entry.getValue();
                                     try {
                                   	  DataOutputStream is =new DataOutputStream(temp.getOutputStream());
                                   	  is.writeInt(body.length);
                                   	  is.write(body);
                                   	  }catch (Exception e) {
   										// TODO: handle exception
   									}
                       		 }
                    			System.out.println(bytesToHexString(body));
                    			byte[] temp = new byte[reslutLen];
                                System.arraycopy(parambytes, 4+bodyLength, temp, 0, reslutLen);
                                splitByte(temp);
                    		}else if(reslutLen <0){
                    			System.out.println("..............");
                    			byte[] temp = new byte[reslutLen];
                                System.arraycopy(parambytes, 4+bodyLength, temp, 0, reslutLen);
                                tmpbytes.clear();
                                tmpbytes.add(temp);
                    		}
                    		
                    		
                    	}
                    }else{
                    	tmpbytes.clear();
                    	System.out.println("后"+parambytes);
                    	tmpbytes.add(parambytes);
                    }
                    
        		}else{
        			tmpbytes.clear();
        			System.out.println("后"+parambytes);
        			tmpbytes.add(parambytes);
        		}
        	}
        }

        /**
         * 跟客户端Socket进行通信
         *
         * @throws IOException
         */
        private void handlerSocket() throws Exception {
            // 跟客户端建立好连接之后，我们就可以获取socket的InputStream，并从中读取客户端发过来的信息了
            // 从客户端获取信息
        	
        		
        	DataInputStream dis = new DataInputStream(socket.getInputStream());
            //当前帧长度
            int len = 0;
            //每次从文件读取的数据
            byte[] buffer = new byte[2 * 1024];
           // System.out.println(dis.readInt() + ".." + dis.available());

           
            while((len = dis.read(buffer))!=0){
            	if(tmpbytes.size()>0 && len>0){
            		int oldByteslen = tmpbytes.get(0).length;
            		System.out.println("oldlength:"+oldByteslen+"len:"+len);
            		int currenLenght = oldByteslen+len;
            		byte[] currentbytes = new byte[currenLenght];
            		System.out.println("前"+tmpbytes.get(0));
            		System.arraycopy(tmpbytes.get(0), 0, currentbytes, 0, oldByteslen);
            		System.arraycopy(buffer, 0, currentbytes, oldByteslen, len);
            		splitByte(currentbytes);
            	}else{
            		if(tmpbytes.size() == 0 &&len >0){
            			byte[] temp = new byte[len];
            			System.arraycopy(buffer, 0, temp, 0, len);
            			splitByte(temp);
            		}
            		
            	}
            	Thread.sleep(100);
            }
            socket.close();
//
//            byte tmpb = (byte)dis.read();
//           // byte[] currentbytes;
//            if(tmpbytes.size() > 0){  //上一次IO流中有未处理的剩余包
//                int oldBytesLen = tmpbytes.get(0).length;
//                int socketBytesLen = dis.available()+1;
//                System.out.println("oldBytesLen:"+oldBytesLen+"...."+"socketBytesLen:"+socketBytesLen);
//                int currentLength = oldBytesLen + socketBytesLen;
//                currentbytes = new byte[currentLength];
//                System.arraycopy(tmpbytes.get(0), 0, currentbytes, 0,currentLength);
//                currentbytes[oldBytesLen] = tmpb;
//                dis.read(currentbytes, oldBytesLen+1, socketBytesLen-1);
//               // dis.close();
//                splitInputStreamByte(currentbytes);
//            }else{  //正常未粘包情况
//                int socketBytesLen = dis.available()+1;
//                System.out.println(socketBytesLen);
//                currentbytes = new byte[socketBytesLen];
//                currentbytes[0] = tmpb;
//                dis.read(currentbytes, 1, socketBytesLen-1);
//              //  dis.close();
//                splitInputStreamByte(currentbytes);
//            }
//            Thread.sleep(1000);

//            System.out.println(
//                    "To Cliect[port:" + socket.getPort() + "] 回复客户端的消息发送成功");
           // dis.close();

            
            //socket.close();
        }
        
       

        /**
         * 拆分byte数组并分多线程处理
         * @param parambytes 原byte数组
         * @return 处理后剩余部分的byte数组
         */
        private  void splitInputStreamByte(byte[] parambytes) {
        	
            if(parambytes != null){
            	for(byte b: parambytes){
            		System.out.print(b);
            	}
                if(parambytes.length > 4){
                    byte[] head = new byte[4];  //单包长度
                    System.arraycopy(parambytes, 0, head, 0, 4);
                    int bodyLength = getint(head);
                    System.out.println("bodylength:"+bodyLength);
                    if(bodyLength <= parambytes.length-4){
                        final byte[] body = new byte[bodyLength];
                        System.arraycopy(parambytes, 4, body, 0, bodyLength);
                        int resultLen = parambytes.length-4-bodyLength;
                        if(resultLen == 0){
                        	System.out.println(bytesToHexString(body));
                        	 for (Map.Entry<Integer, Socket> entry : socketMap.entrySet()) {
                             if(entry.getKey().equals(socket.getPort())){
                                continue;
                             }
                             Socket temp = entry.getValue();
                              try {
                            	  DataOutputStream is =new DataOutputStream(temp.getOutputStream());
                            	  is.write(body.length);
                            	  is.write(body);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
//                            System.out.println(bytesToHexString(bytes));
                        	 	}

                            splitInputStreamByte(null);
                        }else{
                        	System.out.println(bytesToHexString(body));
                            byte[] resultbytes = new byte[resultLen];
                            System.arraycopy(parambytes, 4+bodyLength, resultbytes, 0, resultLen);
                            splitInputStreamByte(resultbytes);
                        }
                    }else{
                        tmpbytes.clear();
                        tmpbytes.add(parambytes);
                    }
                }else{
                    tmpbytes.clear();
                    tmpbytes.add(parambytes);
                }
            }
        }
    }

    public static final int getint(byte[] paramArrayOfByte){

        int reslut = (paramArrayOfByte[0]&0xFF)<<24
                |(paramArrayOfByte[1]&0xFF)<<16
                |(paramArrayOfByte[2]&0xFF)<<8
                |paramArrayOfByte[3]&0xFF;
        return reslut;
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
}

//        public void talk() {
//        try {
//            System.out.println("等待用户接入 : ");
//
//
//
//            if (socketMap != null) {
//                socketMap.put(socket.getPort(), socket);
//            }
//            System.out.println("用户接入 : " + socket.getPort());
//            // 开启一个子线程来等待另外的socket加入
//
//            // 从客户端获取信息
//
//            DataInputStream dis = new DataInputStream(socket.getInputStream());
////            byte[] bytes = new byte[2*1024]; // 一次读取一个byte
////            while (dis.read(bytes) != -1) {
////                //System.out.println(bytesToHexString(bytes));
////                for (Map.Entry<Integer, Socket> entry : socketMap.entrySet()) {
////                    if(entry.getKey().equals(socket.getPort())){
////                        continue;
////                    }
////                    Socket temp = entry.getValue();
////                    temp.getOutputStream().write(bytes);
////                    System.out.println(bytesToHexString(bytes));
////                }
////            }
//
//            //当前帧长度
//            int len = 0;
//            //每次从文件读取的数据
//            byte[] buffer = new byte[2 * 1024];
//            System.out.println(dis.readInt()+".."+ dis.available());
//            while ((len = dis.readInt()) != -1) {
//                int newLen = dis.read(buffer);
//                System.out.println(len +"..."+newLen);
//                if (len > buffer.length) {
//                }
//                if (newLen == len) {
//                    for (Map.Entry<Integer, Socket> entry : socketMap.entrySet()) {
//                        Socket temp = entry.getValue();
//                        DataOutputStream dos = new DataOutputStream(temp.getOutputStream());
//                        dos.writeInt(newLen);
//                        dos.write(buffer, 0, newLen);
//                        System.out.println(bytesToHexString(buffer));
//                    }
//                }
//
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//
//    public static void doSomething(String ret) {
//        System.out.println(ret);
//    }
//
//    public static String bytesToHexString(byte[] src) {
//        StringBuilder stringBuilder = new StringBuilder("");
//        if (src == null || src.length <= 0) {
//            return null;
//        }
//        for (int i = 0; i < src.length; i++) {
//            int v = src[i] & 0xFF;
//            String hv = Integer.toHexString(v);
//            if (hv.length() < 2) {
//                stringBuilder.append(0);
//            }
//            stringBuilder.append(hv);
//        }
//        return stringBuilder.toString();
//    }
//
//    public static String BytesHexString(byte[] b) {
//        String ret = "";
//        for (int i = 0; i < b.length; i++) {
//            String hex = Integer.toHexString(b[i] & 0xFF);
//            if (hex.length() == 1) {
//                hex = '0' + hex;
//            }
//            ret += hex.toUpperCase();
//        }
//        return ret;
//    }
//}



//    private ServerSocket server;
//    private int port = 8888;
//    // 使用ArrayList存储所有的Socket
//    public List<Socket> socketList = new ArrayList<>();
//    // 模仿保存在内存中的socket
//    public Map<Integer, Socket> socketMap = new HashMap();
//
//
//    public static void main(String[] args){
//        new Test().talk();
//    }
//
//    public  Test(){
//        try {
//            server = new ServerSocket(port);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void talk() {
//        try {
//            System.out.println("等待用户接入 : ");
//            // 使用accept()阻塞等待客户请求
//            Socket socket = null;
//
//            socket = server.accept();
//            if (socketMap != null ) {
//                socketMap.put(socket.getPort(), socket);
//            }
//            System.out.println("用户接入 : " + socket.getPort());
//            // 开启一个子线程来等待另外的socket加入
////            new Thread(new Runnable() {
////                public void run() {
////                    // 再次创建一个socket服务等待其他用户接入
////                    talk();
////                }
////            }).start();
//            // 从客户端获取信息
//            // 装饰流BufferedReader封装输入流（接收客户端的流）
//            BufferedInputStream bis = new BufferedInputStream(
//                    socket.getInputStream());
//
//            DataInputStream dis = new DataInputStream(bis);
////            byte[] bytes = new byte[2*1024]; // 一次读取一个byte
////            while (dis.read(bytes) != -1) {
////                //System.out.println(bytesToHexString(bytes));
////                for (Map.Entry<Integer, Socket> entry : socketMap.entrySet()) {
////                    if(entry.getKey().equals(socket.getPort())){
////                        continue;
////                    }
////                    Socket temp = entry.getValue();
////                    temp.getOutputStream().write(bytes);
////                    System.out.println(bytesToHexString(bytes));
////                }
////            }
//
//            //当前帧长度
//            int len = 0;
//            //每次从文件读取的数据
//            byte[] buffer = new byte[2 * 1024];
//            while ((len = dis.readInt()) != -1) {
//                int newLen = dis.read(buffer, 0, len);
//                if (len > buffer.length) {
//                    Log.e("error", "buffer big");
//                }
//                if (newLen == len) {
//                    for (Map.Entry<Integer, Socket> entry : socketMap.entrySet()) {
//                        if(entry.getKey().equals(socket.getPort())){
//                            continue;
//                        }
//                        Socket temp = entry.getValue();
//                        temp.getOutputStream().write(buffer,0,newLen);
//                        System.out.println(bytesToHexString(buffer));
//                    }
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//
//
//    public static void doSomething(String ret) {
//        System.out.println(ret);
//    }
//
//    public static String bytesToHexString(byte[] src) {
//        StringBuilder stringBuilder = new StringBuilder("");
//        if (src == null || src.length <= 0) {
//            return null;
//        }
//        for (int i = 0; i < src.length; i++) {
//            int v = src[i] & 0xFF;
//            String hv = Integer.toHexString(v);
//            if (hv.length() < 2) {
//                stringBuilder.append(0);
//            }
//            stringBuilder.append(hv);
//        }
//        return stringBuilder.toString();
//    }
//
//    public static String BytesHexString(byte[] b) {
//        String ret = "";
//        for (int i = 0; i < b.length; i++) {
//            String hex = Integer.toHexString(b[i] & 0xFF);
//            if (hex.length() == 1) {
//                hex = '0' + hex;
//            }
//            ret += hex.toUpperCase();
//        }
//        return ret;
//    }
//


