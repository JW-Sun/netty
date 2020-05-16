package com.jw.netty07_groupChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class GroupChatServer {
    private Selector selector;

    private ServerSocketChannel listenChannel;

    private static final int port = 6667;

    public GroupChatServer() {
        try {
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            // bind port
            listenChannel.socket().bind(new InetSocketAddress(port));
            // 设置非阻塞
            listenChannel.configureBlocking(false);
            // listenChannel注册到Selector上
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 客户端来了需要监听
    public void listen() {
        while (true) {
            try {
                int count = selector.select(2000);
                if (count == 0) {
                    System.out.println("=====无数据，等待=====");
                } else {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        // 如果监听到的连接事件accept
                        if (key.isAcceptable()) {
                            // 代表来自客户端的连接来了
                            SocketChannel socketChannel = listenChannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            // 提示客户端连接了
                            System.out.println(socketChannel.getRemoteAddress() + " 上线了 ");
                        }
                        // 通道发生read事件，通道就是可读的状态
                        if (key.isReadable()) {
                            // 处理读（专门方法）
                            readData(key);
                        }

                        // 把当前的key删除，防止重复处理
                        iterator.remove();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void readData(SelectionKey selectionKey) {
        // 定义一个SocketChannel
        SocketChannel channel = null;
        try {
            // 取到关联的Channel
            channel = (SocketChannel) selectionKey.channel();
            // 创建缓冲
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int read = channel.read(buffer);
            // 根据读的read的值进行判断
            if (read > 0) {
                // 读到了数据，将数据转换成字符串
                String s = new String(buffer.array());
                //输出该消息
                System.out.println("from client " + s);

                // 向其他的客户端转发消息，不再向自己发消息,将自己的channel进行排除的参数进行更进一步的操作
                sendMsgToClients(s, channel);
            }
        } catch (Exception e) {
            try {
                System.out.println(channel.getRemoteAddress() + " 离线了 ");
                // 取消注册，关闭通道
                selectionKey.cancel();
                channel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    /*转发消息给其他客户（socketChannel），*/
    private void sendMsgToClients(String msg, SocketChannel excludeChannel) throws IOException {
        System.out.println("服务器开始转发消息。。。");
        //遍历所有注册到selector上的SocketChannel，并排除发送消息的客户端
        Set<SelectionKey> keys = selector.keys();
        for (SelectionKey key : keys) {
            // 取出通道
            Channel targetChannel = key.channel();
            // 排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != excludeChannel) {
                // 转发
                SocketChannel socketChannel = (SocketChannel) targetChannel;
                // 将msg存储到buffer中
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                // 将buffer 的数据写入到通道之中
                socketChannel.write(buffer);
            }
        }

    }

    public static void main(String[] args) {
        GroupChatServer server = new GroupChatServer();
        server.listen();
    }
}
