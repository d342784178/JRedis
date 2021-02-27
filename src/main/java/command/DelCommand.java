package command;

import command.model.IRedisResult;
import command.template.CommandConstants;
import command.template.NoKeyCommand;
import container.DataBase;
import io.netty.channel.ChannelHandlerContext;
import utils.ArrayOperator;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 12:11
 */
public class DelCommand extends NoKeyCommand {
    @Override
    public String name() {
        return CommandConstants.DEL;
    }

    @Override
    public int argsLength() {
        return -2;
    }

    @Override
    protected boolean multiArgs() {
        return true;
    }

    @Override
    protected IRedisResult innerExecute(ChannelHandlerContext ctx, DataBase db, ArrayOperator<String> args) {
        return db.del(args.get(0));
    }
}
