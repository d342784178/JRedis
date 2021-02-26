package network.handler;

import command.model.RedisCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import network.codec.RedisCommandCodec;

import java.util.List;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 16:10
 */
public class RedisFrameDecodeHandler extends ByteToMessageDecoder {
    RedisCommandCodec decoder = new RedisCommandCodec();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        RedisCommand decode;
        while ((decode = decoder.decode(ctx, in)) != null) {
            out.add(decode);
        }
    }
}
