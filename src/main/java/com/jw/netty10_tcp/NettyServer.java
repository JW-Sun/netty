package com.jw.netty10_tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
// 45 46 47 48 49 50
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {

        // 创建bossGroup和workerGroup
        // 创建两个线程组
        // bossGroup仅处理连接请求
        // 真正的业务处理交给workerGroup处理
        // 两个都是无限循环
        /* 4. bossGroup和workerGroup含有的子线程（NioEventLoop）的个数，默认实际为cpu核数*2 这里就是8个
        *     如果新增客户端，就增加这个处理的线程。并且12345678之后会循环利用1（类似round-robin） */
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建服务器端的启动对象，配置参数
            ServerBootstrap bootStrap = new ServerBootstrap();

            // 使用链式编程进行设置
            bootStrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列得到连接的个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // 设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 创建一个通道初始化对象（匿名对象）
                        // 给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 可以使用一个集合管理SocketChannel
                            System.out.println("客户socketChannel hashcode = " + ch.hashCode());
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });

            System.out.println("server ready");

            // 绑定一个端口并且同步，生成一个ChannelFuture对象
            ChannelFuture channelFuture = bootStrap.bind(6668).sync();

            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
