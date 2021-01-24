package command;

import command.executor.Executors;
import command.executor.IExecutor;
import command.executor.ReadExecutor;
import command.executor.SingleTargetExecutor;
import command.model.RedisCommand;
import operating.ZipListList;
import operating.intf.List;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 12:11
 */
public class LPopCommand implements ICommand {
    @Override
    public String name() {
        return CommandConstants.LPOP;
    }

    @Override
    public Object execute(RedisCommand command) {
        return Executors.build(ReadExecutor.class)
                        .execute(command, new IExecutor.ExecuteCallback<List>() {
                            @Override
                            public <T> T execute(List redisObject, String value) {
                                return (T) redisObject.lpop();
                            }

                            @Override
                            public List find(String key) {
                                return (List) command.getDataBase().getRedisObject(key);
                            }
                        });
    }
}
