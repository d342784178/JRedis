package command;

import command.model.RedisCommand;
import operating.intf.List;
import operating.intf.RedisObject;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 17:16
 */
public abstract class AbstractCommand<T> implements ICommand {


    protected T internalExecute(List redisObject, String value) {
        return null;
    }

    protected RedisObject check(RedisCommand command) {
        //1. 参数格式是否合理
        String[] args = command.getArgs();
        validateArgs(args);
        //2. 判断key是否存在
        RedisObject redisObject = command.getDataBase().getRedisObject(args[1]);
        if (redisObject == null) {
            throw new IllegalArgumentException("redis object not exist:" + args[1]);
        }
        return redisObject;
    }

    private void validateArgs(String[] args) {
    }
}
