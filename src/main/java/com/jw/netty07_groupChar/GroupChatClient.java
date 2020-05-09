package com.jw.netty07_groupChar;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class GroupChatClient {

    // 定义相关属性
    private String HOST = "localhost";
    private int PORT = 6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    // 初始化工作
    public GroupChatClient() throws IOException {
        // 拿到选择器
        selector = Selector.open();
        // 连接服务器
        socketChannel = socketChannel.open(new InetSocketAddress(HOST, PORT));
        // 设置非阻塞
        socketChannel.configureBlocking(false);
        // 将channel注册到selector
        socketChannel.register(selector, SelectionKey.OP_READ);
        // 拿到username
        username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username + " is ok! ");
    }

    /*向服务器发送消息*/
    public void sendInfo(String info) {
        info = username + "---" + info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读取从服务器端回复的消息
    public void readInfo() {
        try {
            int readChannels = selector.select();
            if (readChannels > 0) {
                // 有可用通道
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isReadable()) {
                        // 可读 得到相关通道
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        // 读取
                        channel.read(buffer);
                        // 把读到的缓冲区的数据转成字符串
                        String msg = new String(buffer.array());
                        System.out.println(msg.trim());
                    }
                }
                iterator.remove();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        GroupChatClient client = new GroupChatClient();

        // 线程每隔三秒读取从服务器发送的数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    client.readInfo();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        // 发送数据给服务端
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String s = sc.nextLine();
            client.sendInfo(s);
        }
    }
}
