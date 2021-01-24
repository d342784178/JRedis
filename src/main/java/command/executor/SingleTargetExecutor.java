package command.executor;

import network.model.RedisCommand;
import operating.intf.RedisObject;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 17:45
 */
public class SingleTargetExecutor extends AbstractExecutor {

    @Override
    public <T> T execute(RedisCommand command, ExecuteCallback callback) {
        String[]    args        = command.getArgs();
        String      key         = args[1];
        RedisObject redisObject = callback.find(key);
        if (redisObject == null) {
            return null;
        }
        for (int i = 2; i < args.length; i++) {
            Object execute = callback.execute(redisObject, args[i]);
        }
        return null;
    }
}
