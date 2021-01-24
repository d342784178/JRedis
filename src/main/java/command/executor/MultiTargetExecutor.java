package command.executor;

import command.model.RedisCommand;
import command.model.IRedisResult;
import operating.intf.RedisObject;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 17:46
 */
public class MultiTargetExecutor extends WriteExecutor {
    @Override
    public IRedisResult execute(RedisCommand command, ExecuteCallback callback) {
        String[] args = command.getArgs();

        int i = 1;
        while (true) {
            if (i + 2 < args.length) {
                String      key         = args[i++];
                RedisObject redisObject = callback.find(key);
                if (redisObject != null) {
                    callback.execute(redisObject, args[i++]);
                }
            } else {
                System.out.println("参数不完整");
                break;
            }
        }
        return null;
    }
}
