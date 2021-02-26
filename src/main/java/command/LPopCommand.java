package command;

import command.executor.Executors;
import command.executor.IExecutor;
import command.executor.ReadExecutor;
import command.executor.SingleTargetExecutor;
import command.model.IRedisResult;
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
                            public StrRedisResult execute(List redisObject, String value) {
                                return new StrRedisResult(redisObject.lpop());
                            }

                            @Override
                            public List find(String key) {
                                return (List) command.getDataBase().getRedisObject(key);
                            }
                        });
    }
}
