package command.template;

import command.model.IRedisResult;
import command.model.IntRedisResult;
import command.model.RedisCommand;
import container.DataBase;
import io.netty.channel.ChannelHandlerContext;
import utils.ArrayOperator;

import java.util.Iterator;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-02-27
 * Time: 11:59
 */
public abstract class NoKeyCommand extends AbstractCommand implements INoKeyCommand {

    @Override
    final public IRedisResult execute(RedisCommand command) {
        super.execute(command);
        DataBase              db   = command.getDb();
        ArrayOperator<String> args = new ArrayOperator<>(command.getArgs(), 1);

        IRedisResult result;
        if (multiArgs()) {
            Iterator<ArrayOperator<String>> iterable = args.iterable(1);
            int                             num      = 0;
            while (iterable.hasNext()) {
                ArrayOperator<String> sliceArgs = iterable.next();
                IRedisResult          tmpResult = innerExecute(command.getCtx(), db, sliceArgs);
                if (tmpResult != null) {
                    num += 1;
                }
            }
            result = new IntRedisResult(num);
        } else {
            result = innerExecute(command.getCtx(), db, args);
        }
        return result;
    }

    protected boolean multiArgs() {
        return false;
    }

    protected IRedisResult innerExecute(ChannelHandlerContext ctx, DataBase db, ArrayOperator<String> args) {
        return null;
    }


}
