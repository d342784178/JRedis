package network.handler;

import command.model.RedisCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-17
 * Time: 22:34
 */
public class CommandExecutorHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RedisCommand command = (RedisCommand) msg;
        Object       result  = command.exec();
        System.out.println(result);
        ctx.write(result);
        //echo(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private void echo(ChannelHandlerContext ctx) {
        ByteBuf buffer = ctx.alloc().buffer(100);
        buffer.writeBytes("*1\r\n$4\r\necho\r\n".getBytes());
        ctx.writeAndFlush(buffer);
    }

}
