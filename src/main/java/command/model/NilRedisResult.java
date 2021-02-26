package command.model;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;

import java.nio.charset.StandardCharsets;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 21:26
 */
public class NilRedisResult implements IRedisResult {
    @Override
    public ByteBuf encode() {
        byte[]  lengthBytes = String.valueOf("nil".length()).getBytes(StandardCharsets.UTF_8);
        ByteBuf result      = UnpooledByteBufAllocator.DEFAULT.buffer(lengthBytes.length + 3 + lengthBytes.length + 2);
        result.writeByte('$');
        result.writeBytes(lengthBytes);
        result.writeByte('\r');
        result.writeByte('\n');
        result.writeBytes("nil".getBytes(StandardCharsets.UTF_8));
        result.writeByte('\r');
        result.writeByte('\n');
        return result;
    }
}
