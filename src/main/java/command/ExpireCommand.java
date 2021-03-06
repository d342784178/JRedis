package command;

import command.model.IRedisResult;
import command.model.IntRedisResult;
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
public class ExpireCommand extends NoKeyCommand {
    @Override
    public String name() {
        return CommandConstants.EXPIRE;
    }

    @Override
    public int argsLength() {
        return 3;
    }

    @Override
    protected IRedisResult innerExecute(ChannelHandlerContext ctx, DataBase db, ArrayOperator<String> args) {
        super.innerExecute(ctx, db, args);

        String keyStr     = args.get(0);
        String expireTime = args.get(1);

        return new IntRedisResult(db.expire(keyStr, Long.parseLong(expireTime)));
    }
}
