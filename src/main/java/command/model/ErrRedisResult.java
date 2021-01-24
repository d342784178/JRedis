package command.model;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;

import java.nio.charset.StandardCharsets;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 21:19
 */
public class ErrRedisResult implements IRedisResult {
    private String errInfo;

    public ErrRedisResult(String errInfo) {
        this.errInfo = errInfo;
    }

    @Override
    public ByteBuf encode() {
        byte[]  lengthBytes = String.valueOf(errInfo.length()).getBytes(StandardCharsets.UTF_8);
        ByteBuf result      = UnpooledByteBufAllocator.DEFAULT.buffer(lengthBytes.length + 3);
        result.writeByte('-');
        result.writeBytes(errInfo.getBytes(StandardCharsets.UTF_8));
        result.writeByte('\r');
        result.writeByte('\n');
        return result;
    }
}
