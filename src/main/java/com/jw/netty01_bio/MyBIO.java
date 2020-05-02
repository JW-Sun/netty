package com.jw.netty01_bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyBIO {
    public static void main(String[] args) throws IOException {

        //1. 创建一个线程池
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();



        ServerSocket serverSocket = new ServerSocket(5555);

        System.out.println("server start... ");

        for (;;) {
            // 监听等待客户端连接
            System.out.println("等待客户端连接.......阻塞");
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");

            //2. 如果有客户端连接，就创建一个线程，与之通讯
            newCachedThreadPool.execute(new Runnable() {
                public void run() {
                    handler(socket);
                }
            });
        }


    }

    // 与客户端通讯的方法
    public static void handler(Socket socket) {
        byte[] bytes = new byte[1024];

        //通过socket获得输入流
        try {
            System.out.println("获得线程： " + Thread.currentThread().getId());
            InputStream inputStream = socket.getInputStream();

            while (true) {
                System.out.println("read阻塞........");
                int read = inputStream.read(bytes);
                if (read != -1) {
                    System.out.println(new String(bytes, 0, read));
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("关闭socket连接");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
