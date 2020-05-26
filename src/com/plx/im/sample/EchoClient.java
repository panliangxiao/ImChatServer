package com.plx.im.sample;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class EchoClient {
 
    private final Socket mSocket;
 
    public EchoClient(String host, int port) throws IOException {
        // 创建 socket 并连接服务器
        mSocket = new Socket(host, port);
    }
 
    public void run() throws IOException {
        // 和服务端进行通信
        //客户端会稍微复杂一点点，在读取用户输入的同时，我们又想读取服务器的响应。所以，这里创建了一个线程来读服务器的响应。
        Thread readerThread = new Thread(this::readResponse);
        readerThread.start();

        OutputStream out = mSocket.getOutputStream();
        byte[] buffer = new byte[1024];
        int n;
        while ((n = System.in.read(buffer)) > 0) {
            out.write(buffer, 0, n);
        }
    }

    private void readResponse() {
        try {
            InputStream in = mSocket.getInputStream();
            byte[] buffer = new byte[1024];
            int n;
            while ((n = in.read(buffer)) > 0) {
                System.out.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
 
    public static void main(String[] argv) {
        try {
            // 由于服务端运行在同一主机，这里我们使用 localhost
            EchoClient client = new EchoClient("localhost", 9877);
            client.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}