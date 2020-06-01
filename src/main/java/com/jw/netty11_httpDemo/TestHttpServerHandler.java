package com.jw.netty11_httpDemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import javafx.scene.control.cell.CheckBoxTreeTableCell;

import java.net.URI;
import java.nio.ByteBuffer;

/***
 * SimpleChannelInboundHandler是ChannelInboundHandlerAdapter
 *  HttpObject客户端和服务端互相通讯的数据被封装成HttpObject
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest) {
            System.out.println("pipeline hashcode: " + ctx.pipeline().hashCode());
            System.out.println("TestHttpHandler hash: " + this.hashCode());
            System.out.println("msg type: " + msg.getClass());
            System.out.println("client address: " + ctx.channel().remoteAddress());

            HttpRequest request = (HttpRequest) msg;
            URI uri = new URI(request.uri());

            // 回复消息给http响应
            ByteBuf content = Unpooled.copiedBuffer("hello this is server", CharsetUtil.UTF_8);

            // 构造http响应，即HttpResponse
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            // 将构建好的response返回
            ctx.writeAndFlush(response);

        }
    }
}
