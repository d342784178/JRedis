package command;

import command.executor.Executors;
import command.executor.IExecutor;
import command.executor.ReadExecutor;
import command.model.IRedisResult;
import command.model.IntRedisResult;
import command.model.RedisCommand;
import operating.ZipListList;
import operating.intf.List;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 12:11
 */
public class LLenCommand implements ICommand {
    @Override
    public String name() {
        return CommandConstants.LLEN;
    }

    @Override
    public IRedisResult execute(RedisCommand command) {
        return Executors.build(ReadExecutor.class)
                        .execute(command, new IExecutor.ExecuteCallback<List>() {
                            @Override
                            public IntRedisResult execute(List redisObject, String value) {
                                return new IntRedisResult(redisObject.llen());
                            }

                            @Override
                            public List find(String key) {
                                return (List) command.getDataBase().getRedisObjectOrDefault(key, new ZipListList());
                            }
                        });
    }
}
