package network;

import io.netty.buffer.ByteBuf;
import io.netty.util.ByteProcessor;
import lombok.Getter;

import java.nio.charset.StandardCharsets;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 16:10
 */
public class RedisCommandDesc {
    @Getter
    private char type;
    @Getter
    private int  len;


    public static RedisCommandDesc parse(ByteBuf byteBuf) {
        int end = findEnd(byteBuf);
        if (end > 0) {
            char   type    = (char) byteBuf.readByte();
            String content = readContent(byteBuf, end);
            return new RedisCommandDesc(type,
                    Integer.parseInt(content, 10));
        } else {
            return null;
        }
    }

    private static String readContent(ByteBuf byteBuf, int endIdx) {
        String lengthStr = byteBuf.readBytes(endIdx - 1 - byteBuf.readerIndex()).toString(StandardCharsets.UTF_8);
        byteBuf.skipBytes(2);
        return lengthStr;
    }

    public static int findEnd(ByteBuf byteBuf) {
        int i = byteBuf.forEachByte(ByteProcessor.FIND_LF);
        if (i > 0) {
            return '\r' == byteBuf.getByte(i - 1) ? i : -1;
        } else {
            return -1;
        }
    }

    private RedisCommandDesc(char type, int len) {
        this.type = type;
        this.len = len;
    }

    public <T> T decode(ByteBuf byteBuf) {
        switch (type) {
            case '*'://数组
                String[] strArray = new String[len];
                for (int i = 0; i < len; i++) {
                    RedisCommandDesc parse = parse(byteBuf);
                    if (parse != null) {
                        strArray[i] = parse.decode(byteBuf);
                    } else {
                        return null;
                    }
                }
                return (T) strArray;
            case '$'://字符串
                int end = findEnd(byteBuf);
                if (end > 0) {
                    String content = readContent(byteBuf, end);
                    return (T) content;
                }
                return null;
            default:
                throw new IllegalArgumentException("unexpected command type:" + type);
        }
    }
}
