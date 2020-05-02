package com.jw.netty03_Channel.nio02_putGet;

import java.nio.ByteBuffer;

/*17*/
public class ReadOnlyBuffer {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        for (int i = 0; i < 64; i++) {
            //默认是按照byte进行放置
            buffer.put((byte) i);
        }

        buffer.flip();

        //得到一个只读的buffer
        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();

        System.out.println(readOnlyBuffer.getClass());

        while (readOnlyBuffer.hasRemaining()) {
            System.out.println(readOnlyBuffer.get());
        }

        // 只读buffer是不能够再次写入的
        //readOnlyBuffer.put((byte) 1);

    }
}
