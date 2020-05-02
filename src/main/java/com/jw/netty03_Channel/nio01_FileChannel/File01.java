package com.jw.netty03_Channel.nio01_FileChannel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
/*13*/
public class File01 {
    public static void main(String[] args) throws IOException {

        /*string 到buffer中，再到chennel中，channel中有输出流，最后输出结果*/

        String s = "asd";

        FileOutputStream fileOutputStream = new FileOutputStream("E:\\JavaIDEAProject\\netty\\src\\main\\java\\com\\jw\\netty03_Channel\\nio01_FileChannel\\ouput\\file01.txt");

        //真实类型是FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();

        //创建一个缓冲区 byteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //将string放到bytebuffer
        byteBuffer.put(s.getBytes());

        byteBuffer.flip();

        //将bytebuffer中的数据写入到channel中
        fileChannel.write(byteBuffer);

        fileOutputStream.close();
    }
}
