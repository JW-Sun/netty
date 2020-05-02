package com.jw.netty03_Channel.nio01_FileChannel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/*15*/
/*使用Channel通道和方法read write完成对文件的拷贝*/
public class File03 {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("E:\\JavaIDEAProject\\netty\\src\\main\\java\\com\\jw\\netty03_Channel\\nio01_FileChannel\\data\\1.txt");
        FileChannel channel1 = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("E:\\JavaIDEAProject\\netty\\src\\main\\java\\com\\jw\\netty03_Channel\\nio01_FileChannel\\data\\2.txt");
        FileChannel channel2 = fileOutputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(512);

        //将channel1的数据放到buffer中
        while (true) {

            /*重要操作,清空buffer,复位position不然会read == 0然后产生无限重复的情况*/
            buffer.clear();

            int read = channel1.read(buffer);
            System.out.println(read);
            if (read == -1) {
                //读完
                break;
            }
            //将buffer中的数据写到channel02中,放之前要进行翻转
            buffer.flip();
            channel2.write(buffer);
        }

        fileInputStream.close();
        fileOutputStream.close();
    }
}
