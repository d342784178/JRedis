package command;

import command.executor.Executors;
import command.executor.IExecutor;
import command.executor.SingleTargetExecutor;
import command.model.RedisCommand;
import command.model.StrRedisResult;
import operating.ZipListList;
import operating.intf.List;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 12:11
 */
public class LPushCommand extends AbstractCommand {
    @Override
    public String name() {
        return CommandConstants.LPUSH;
    }

    @Override
    public Object execute(RedisCommand command) {
        return Executors.build(SingleTargetExecutor.class)
                        .execute(command, new IExecutor.ExecuteCallback<List>() {
                            @Override
                            public StrRedisResult execute(List redisObject, String value) {
                                redisObject.lpush(value.getBytes());
                                return null;
                            }

                            @Override
                            public List find(String key) {
                                return (List) command.getDataBase().getRedisObjectOrDefault(key, new ZipListList());
                            }
                        });
    }

}
