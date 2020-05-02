package com.jw.netty03_Channel.nio01_FileChannel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
/*16*/
/*完成文件的拷贝*/
public class File04 {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("");
        FileOutputStream fileOutputStream = new FileOutputStream("");

        //获得各个流对应的fileChannel
        FileChannel channel1 = fileInputStream.getChannel();
        FileChannel channel2 = fileOutputStream.getChannel();

        //使用transform完成拷贝
        long l = channel2.transferFrom(channel1, 0, channel1.size());


        channel2.close();
        channel1.close();
        fileOutputStream.close();
        fileInputStream.close();

    }
}
