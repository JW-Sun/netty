package com.jw.netty03_Channel.nio01_FileChannel;
/*14*/

import javax.naming.event.ObjectChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/*从文件中读取数据到buffer中*/
public class File02 {
    public static void main(String[] args) throws IOException {
        File file = new File("E:\\JavaIDEAProject\\netty\\src\\main\\java\\com\\jw\\netty03_Channel\\nio01_FileChannel\\ouput\\file01.txt");

        FileInputStream fileInputStream = new FileInputStream(file);

        FileChannel channel = fileInputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate((int) file.length());

        //将通道的数据写到缓冲区中
        channel.read(buffer);


        //将byteBuffer中的数据转成string
        System.out.println(new String(buffer.array()));

        fileInputStream.close();


    }
}
