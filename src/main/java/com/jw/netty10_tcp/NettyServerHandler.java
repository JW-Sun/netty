package com.jw.netty10_tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /* 读取数据实际（读取客户端发送的消息）
    *  ctx上下文消息，含有管道pipeline，通道channel，地址
    *  msg 客户端发送的数据 默认是Object的形式
    * */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        /*
        System.out.println("服务器读取线程: " + Thread.currentThread().getName());

        System.out.println("server ctx = " + ctx);

        // 看看channel和pipeline的关系。
        Channel channel = ctx.channel();
        ChannelPipeline pipeline = ctx.pipeline();

        // msg转换成ByteBuf
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("客户端发送的消息: " + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端的地址: " + channel.remoteAddress());
         */

        // 假设是有一个非常耗时的业务，需要异步执行任务，
        // 提交到该channel对应的nioEventLoop的taskQueue中
        /*
        *   解决方案一：用户自定义普通任务。这样服务端就不会发生阻塞的情况。
        *
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello client1", CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    System.out.println("发生异常 " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        * */

        /*
        *   2.用户自定义定时任务  该任务提交到scheduleTaskQueue中
        *  */
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello client ---", CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    System.out.println("发生异常 " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, 5, TimeUnit.SECONDS);

    }

    // 数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 数据写入到缓存并且进行刷新
        // 一般讲，对发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端1", CharsetUtil.UTF_8));
        System.out.println(System.currentTimeMillis());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 发生异常之后就需要关闭通道
        ctx.close();
    }
}
