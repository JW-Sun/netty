package com.jw.netty05_nioServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
    public static void main(String[] args) throws IOException {
        // 创建ServerSocketChannel -> ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 得到一个selector对象
        Selector selector = Selector.open();

        // 绑定一个端口7777，在服务器端进行监听。
        serverSocketChannel.socket().bind(new InetSocketAddress(7777));
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        // 把serverSocketChannel注册到selector上 关注事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环等待客户端连接
        while (true) {
            // 等1秒没有任何事件发生
            if (selector.select(1000) == 0) {
                System.out.println("服务器等待了一秒，无连接就不进行等待了");
                continue;
            }

            // 如果返回的大于0，获取到相关的selection集合
            // 如果返回的值>0，那么就获取到相关的selectionKey集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()) {
                // 获取到selectionKey
                SelectionKey key = keyIterator.next();
                // 根据key对应的通道发生的事件做不同的处理
                if (key.isAcceptable()) {
                    // 如果是OP_ACCEPT,有新的客户端进行连接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功，生成一个socketChannel=" + socketChannel.hashCode());

                    // 将socketChannel设置为非阻塞
                    socketChannel.configureBlocking(false);
                    // 注册到selector上,关注事件为READ， 同时给channel关联一个buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                // 发生的是读的事件
                if (key.isReadable()) {
                    // 通过key反向获取对应的channel
                    SocketChannel channel = (SocketChannel) key.channel();

                    // 获取该channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();

                    // 将当前通道数据读到buffer中
                    channel.read(buffer);
                    System.out.println("from client " + new String(buffer.array()));
                }

                // 手动从集合中移除当前的selectionKey,防止重复操作
                keyIterator.remove();
            }
        }
    }
}
