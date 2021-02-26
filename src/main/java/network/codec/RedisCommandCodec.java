package network.codec;

import command.model.IRedisResult;
import container.DataBase;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import command.model.RedisCommand;
import network.RedisCommandDesc;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 16:12
 */
public class RedisCommandCodec {

    public RedisCommand decode(ChannelHandlerContext ctx, ByteBuf bytebuf) {
        bytebuf.markReaderIndex();
        RedisCommandDesc commandDesc = RedisCommandDesc.parse(bytebuf);
        if (commandDesc == null) {
            bytebuf.resetReaderIndex();
            return null;
        }

        String[] args = commandDesc.decode(bytebuf);
        if (args == null) {
            bytebuf.resetReaderIndex();
            return null;
        }

        return new RedisCommand(args, ctx, (DataBase) ctx.attr(AttributeKey.valueOf("DB")).get());
    }

    public ByteBuf encode(IRedisResult result) {
        return result.encode();

    }
}
