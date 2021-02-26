package command.executor;

import command.model.IntRedisResult;
import command.model.RedisCommand;
import command.model.IRedisResult;
import operating.intf.IRedisObject;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 17:45
 */
public class SingleTargetExecutor extends WriteExecutor {

    @Override
    public IRedisResult execute(RedisCommand command, ExecuteCallback callback) {
        String[]    args        = command.getArgs();
        String       key          = args[1];
        IRedisObject IRedisObject = callback.find(key);
        int          num          = 0;
        for (int i = 2; i < args.length; i++) {
            callback.execute(IRedisObject, args[i]);
            num += 1;
        }
        return new IntRedisResult(num);
    }
}
