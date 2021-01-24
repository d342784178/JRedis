package network.handler;

import command.model.IRedisResult;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import network.encode.RedisCommandCodec;
import org.apache.commons.lang3.StringEscapeUtils;

import java.nio.charset.StandardCharsets;

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
        System.out.println(StringEscapeUtils.escapeJava(encode.toString(StandardCharsets.UTF_8)));
        out.writeBytes(encode);
    }
}
