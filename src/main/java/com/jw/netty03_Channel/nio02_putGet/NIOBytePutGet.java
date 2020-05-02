package com.jw.netty03_Channel.nio02_putGet;

import java.nio.ByteBuffer;

/*17-Buffer的类型化和只读*/
public class NIOBytePutGet {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        //类型化放入数据
        buffer.putInt(100);
        buffer.putLong(10L);
        buffer.putChar('a');
        buffer.putShort((short) 2);

        //一定要取出对应的数值
        buffer.flip();

        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getChar());
        System.out.println(buffer.getShort());
    }
}
