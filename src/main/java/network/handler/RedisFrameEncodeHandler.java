package network.handler;

import command.model.IRedisResult;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import network.codec.RedisCommandCodec;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 21:28
 */
public class RedisFrameEncodeHandler extends MessageToByteEncoder<IRedisResult> {

    RedisCommandCodec codec = new RedisCommandCodec();

    @Override
    protected void encode(ChannelHandlerContext ctx, IRedisResult msg, ByteBuf out) throws Exception {
        ByteBuf encode = codec.encode(msg);
        out.writeBytes(encode);
    }
}
