package command;

import command.model.IRedisResult;
import command.model.RedisCommand;
import operating.intf.IRedisObject;
import operating.intf.List;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 12:11
 */
public class DelCommand extends AbstractCommand {
    @Override
    public String name() {
        return CommandConstants.DEL;
    }

    @Override
    public IRedisResult execute(RedisCommand command) {
        return command.getDataBase().del(command.getArgs()[1]);
    }


    @Override
    public Class<?>[] support() {
        return new Class[]{List.class};
    }
}
