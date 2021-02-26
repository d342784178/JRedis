package command.executor;

import command.model.RedisCommand;
import command.model.IRedisResult;
import operating.intf.IRedisObject;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 21:06
 */
public class ReadExecutor extends AbstractExecutor {
    @Override
    public IRedisResult execute(RedisCommand command, ExecuteCallback callback) {
        String[]    args        = command.getArgs();
        String       key          = args[1];
        IRedisObject IRedisObject = callback.find(key);
        if (IRedisObject == null) {
            return null;
        }
        return callback.execute(IRedisObject, null);

    }
}
