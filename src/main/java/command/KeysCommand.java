package command;

import command.model.ArrayRedisResult;
import command.model.IRedisResult;
import command.model.RedisCommand;
import container.DataBase;
import operating.intf.List;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 12:11
 */
public class KeysCommand extends AbstractCommand {
    @Override
    public String name() {
        return CommandConstants.KEYS;
    }

    @Override
    public IRedisResult execute(RedisCommand command) {
        DataBase dataBase = command.getDataBase();
        return new ArrayRedisResult(dataBase.keys(command.getArgs()[1]));
    }

    @Override
    public Class<?>[] support() {
        return new Class[]{List.class};
    }
}
