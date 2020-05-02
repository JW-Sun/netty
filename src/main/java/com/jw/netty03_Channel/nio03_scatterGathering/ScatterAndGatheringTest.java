package com.jw.netty03_Channel.nio03_scatterGathering;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/* 19 20  Buffer的分散和聚集
*
*  Scatter: 将数据写入到buffer时，可以采用buffer数组，依次写入分散
*  Gathering: 从buffer中读取数据的时候，可以采用buffer数组，一次读取
*
* */
public class ScatterAndGatheringTest {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        // 绑定端口到socket，并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        // 创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];

        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        // 等客户端连接 telnet
        SocketChannel accept = serverSocketChannel.accept();
        int messLen = 8;
        while (true) {
            int byteRead = 0;
            while (byteRead != messLen) {
                long l = accept.read(byteBuffers);
                byteRead += l;
                System.out.println("bbteRead: " + byteRead);

                //使用流打印，当前buffer的position和limit
                Arrays.asList(byteBuffers).stream().map(buffer -> "position=" + buffer.position() + ",limit=" + buffer.limit()).forEach(System.out::println);
            }

            // 将所有的buffer进行翻转
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip());

            long byteWrite = 0L;
            while (byteWrite < messLen) {
                long write = accept.write(byteBuffers);
                byteWrite += write;
            }

            // 将所有的buffer进clear
            Arrays.asList(byteBuffers).forEach(buffer -> {
                buffer.clear();
            });

            System.out.println("byteRead=" + byteRead + " byteWrite=" + byteWrite + " byteLen=" + messLen);
        }
    }
}
