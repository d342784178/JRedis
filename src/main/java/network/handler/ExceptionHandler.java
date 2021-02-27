package network.handler;

import command.model.ErrRedisResult;
import exception.IRedisException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-02-27
 * Time: 10:55
 */
public class ExceptionHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof IRedisException) {
            IRedisException redisException = (IRedisException) cause;
            ctx.writeAndFlush(redisException.buildRedisResult());
        } else {
            cause.printStackTrace();
            ctx.writeAndFlush(new ErrRedisResult("unknow exception").encode());
        }
        //echo(ctx);
    }
}
