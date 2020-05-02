package com.jw.netty02_nio.nio;

import java.nio.IntBuffer;
import java.util.Random;

public class BasicBuffer {

    public static void main(String[] args) {

        //创建一个Buffer，大小5
        IntBuffer allocate = IntBuffer.allocate(5);

        for (int i = 0; i < allocate.capacity(); i++) {
            allocate.put(new Random().nextInt() + i);
        }

        //从buffer中取数据,将buffer切换，读写切换
        allocate.flip();

        while (allocate.hasRemaining()) {
            System.out.println(allocate.get());
        }

    }

}
