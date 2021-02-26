package command;

import command.model.IRedisResult;
import command.model.IntRedisResult;
import command.model.RedisCommand;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 12:11
 */
public class LRemCommand implements ICommand {
    @Override
    public String name() {
        return CommandConstants.LREM;
    }

    @Override
    public IRedisResult execute(RedisCommand command) {
        return new IntRedisResult(1);
    }
}
