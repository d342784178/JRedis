package command.model;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import lombok.ToString;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 21:19
 */
@ToString
public class ArrayRedisResult implements IRedisResult {


    private List<String> keys;

    public ArrayRedisResult(List<String> keys) {
        this.keys = keys;
    }

    @Override
    public ByteBuf encode() {
        int length = (keys.size() + 1) * 3 + 1 + String.valueOf(keys.size()).getBytes(StandardCharsets.UTF_8).length;
        for (String key : keys) {
            length += String.valueOf(key.length()).getBytes(StandardCharsets.UTF_8).length;
        }
        ByteBuf result = UnpooledByteBufAllocator.DEFAULT.buffer(length);
        result.writeByte('*');
        result.writeBytes(String.valueOf(keys.size()).getBytes(StandardCharsets.UTF_8));
        result.writeByte('\r');
        result.writeByte('\n');
        for (String key : keys) {
            result.writeByte('$');
            result.writeBytes(String.valueOf(key.length()).getBytes(StandardCharsets.UTF_8));
            result.writeByte('\r');
            result.writeByte('\n');
            result.writeBytes(key.getBytes(StandardCharsets.UTF_8));
            result.writeByte('\r');
            result.writeByte('\n');
        }
        return result;
    }
}
