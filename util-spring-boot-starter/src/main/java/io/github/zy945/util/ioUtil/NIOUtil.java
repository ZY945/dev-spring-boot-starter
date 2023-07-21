package io.github.zy945.util.ioUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author 伍六七
 * @date 2023/6/13 17:20
 */
public class NIOUtil {
    public static void nio(String url) {
        try {
            RandomAccessFile file = new RandomAccessFile(url, "rw");
            FileChannel channel = file.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (channel.read(buffer) != -1) {
                buffer.flip();
                CharBuffer decode = StandardCharsets.UTF_8.decode(buffer);
//                System.out.println(decode);
                buffer.clear();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void io(String url) {
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(url);
            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                String content = new String(bytes, 0, len);
//                System.out.println(content);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String url = "F:\\study\\code\\pythoncode\\loadDataSource\\BigDataSource\\Geo9,000,000.csv";
        Date start = new Date(System.currentTimeMillis());
        System.out.println(start);
//        io(url);
        nio(url);
        Date end = new Date(System.currentTimeMillis());
        System.out.println(end);
    }
}
