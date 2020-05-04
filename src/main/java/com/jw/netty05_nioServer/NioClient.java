package com.jw.netty05_nioServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioClient {
    public static void main(String[] args) throws IOException {
        // 得到网络通道
        SocketChannel socketChannel = SocketChannel.open();

        // 设置非阻塞
        socketChannel.configureBlocking(false);

        // 提供服务端的ip和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 7777);

        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("因为连接时间，客户端不会阻塞，可以做其他的工作");
            }
        }

        // 连接成功发送数据
        String msg = "hello";
        ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
        // 将buffer写到channel
        socketChannel.write(byteBuffer);
        System.in.read();
    }
}

