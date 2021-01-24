package network.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import network.encode.RedisCommandDecoder;
import network.model.RedisCommand;
import org.apache.commons.lang3.StringEscapeUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 16:10
 */
public class RedisFrameDecodeHandler extends ByteToMessageDecoder {
    RedisCommandDecoder decoder = new RedisCommandDecoder();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(StringEscapeUtils.escapeJava(byteBuf.toString(StandardCharsets.UTF_8)));
        super.channelRead(ctx, msg);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        RedisCommand decode;
        while ((decode = decoder.decode(ctx, in)) != null) {
            out.add(decode);
        }
    }
}
