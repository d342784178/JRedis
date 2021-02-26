package network.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.apache.commons.lang3.StringEscapeUtils;

import java.nio.charset.StandardCharsets;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-17
 * Time: 16:33
 */
public class EchoHandler extends ChannelDuplexHandler {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        String  input   = byteBuf.toString(StandardCharsets.UTF_8);
        System.out.println("[IN]: " + StringEscapeUtils.escapeJava(input));
        ctx.fireChannelRead(msg);
        //ctx.write(msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("[OUT]: " + StringEscapeUtils.escapeJava(byteBuf.toString(StandardCharsets.UTF_8)));
        System.out.println("");
        ctx.write(msg);
    }

}
