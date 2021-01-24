package command.executor;

import network.model.RedisCommand;
import operating.intf.RedisObject;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 17:49
 */
public interface IExecutor {
    <T> T execute(RedisCommand command, ExecuteCallback callback);


    interface ExecuteCallback<T extends RedisObject> {
        <R> R execute(T redisObject, String value);

        T find(String key);
    }
}
