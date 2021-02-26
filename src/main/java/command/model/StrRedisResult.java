package command.model;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import lombok.Getter;
import lombok.ToString;

import java.nio.charset.StandardCharsets;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 21:18
 */
@ToString
public class StrRedisResult implements IRedisResult {
    @Getter
    private String str;

    public StrRedisResult(byte[] str) {
        this.str = new String(str, StandardCharsets.UTF_8);
    }

    @Override
    public ByteBuf encode() {
        byte[]  lengthBytes = String.valueOf(str.length()).getBytes(StandardCharsets.UTF_8);
        ByteBuf result      = UnpooledByteBufAllocator.DEFAULT.buffer(lengthBytes.length + 3 + lengthBytes.length + 2);
        result.writeByte('$');
        result.writeBytes(lengthBytes);
        result.writeByte('\r');
        result.writeByte('\n');
        result.writeBytes(str.getBytes(StandardCharsets.UTF_8));
        result.writeByte('\r');
        result.writeByte('\n');
        return result;
    }
}
