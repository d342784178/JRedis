package command.executor;

import command.model.RedisCommand;
import command.model.IRedisResult;
import operating.intf.IRedisObject;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 17:49
 */
public interface IExecutor {
    IRedisResult execute(RedisCommand command, ExecuteCallback callback);


    interface ExecuteCallback<T extends IRedisObject> {
        IRedisResult execute(T redisObject, String value);

        T find(String key);
    }
}
