package network.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.lang3.StringEscapeUtils;

import java.nio.charset.StandardCharsets;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-17
 * Time: 16:33
 */
public class EchoHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        String  input   = byteBuf.toString(StandardCharsets.UTF_8);
        System.out.println(StringEscapeUtils.escapeJava(input));
        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
