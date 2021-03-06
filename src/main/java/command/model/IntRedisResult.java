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
public class IntRedisResult implements IRedisResult {
    @Getter
    private long num;

    public IntRedisResult(long num) {
        this.num = num;
    }

    @Override
    public ByteBuf encode() {
        byte[]  intBytes = String.valueOf(num).getBytes(StandardCharsets.UTF_8);
        ByteBuf result   = UnpooledByteBufAllocator.DEFAULT.buffer(intBytes.length + 3);
        result.writeByte(':');
        result.writeBytes(intBytes);
        result.writeByte('\r');
        result.writeByte('\n');
        return result;
    }
}
