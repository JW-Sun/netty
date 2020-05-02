package com.jw.netty03_Channel.nio02_putGet;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/* 18 */
/* 说明
*  可以让文件在内存中进行修改，操作系统不需要进行拷贝*/
public class MappedByteBufferTest03 {
    public static void main(String[] args) throws Exception {
        RandomAccessFile rw = new RandomAccessFile("1.txt", "rw");

        // 获得对应的通道
        FileChannel channel = rw.getChannel();

        /***
         *  参数一：使用的读写模式
         *  参数二：可以修改的起始位置
         *  参数三：映射到内存的大小是5 就是将文件txt的多少个字节映射到内存
         *          可以直接修改的范围就是0-5 --左闭右开 不包括5
         *          实际类型是DirectByteBuffer
         */
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        map.put(0, (byte) 'a');
        map.put(3, (byte) '2');

        rw.close();

    }
}
