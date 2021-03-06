package command.template;

import command.model.IRedisResult;
import command.model.RedisCommand;
import command.model.StrRedisResult;
import container.DataBase;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-02-27
 * Time: 11:59
 */
public abstract class NoKeyNoArgCommand extends AbstractCommand implements INoKeyCommand {

    @Override
    final public IRedisResult execute(RedisCommand command) {
        super.execute(command);
        DataBase db = command.getDb();
        innerExecute(command.getCtx(), db);
        return new StrRedisResult("OK".getBytes(StandardCharsets.UTF_8));
    }

    protected boolean multiArgs() {
        return false;
    }

    protected void innerExecute(ChannelHandlerContext ctx, DataBase db) {
    }


}
