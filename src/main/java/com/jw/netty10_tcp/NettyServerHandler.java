package com.jw.netty10_tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /* 读取数据实际（读取客户端发送的消息）
    *  ctx上下文消息，含有管道pipeline，通道channel，地址
    *  msg 客户端发送的数据 默认是Object的形式
    * */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ctx = " + ctx);
        // msg转换成ByteBuf
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("客户端发送的消息: " + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端的地址: " + ctx.channel().remoteAddress());
    }

    // 数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 数据写入到缓存并且进行刷新
        // 一般讲，对发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 发生异常之后就需要关闭通道
        ctx.close();
    }
}
