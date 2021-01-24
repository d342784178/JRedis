package command;

import command.model.RedisCommand;

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
    public Object execute(RedisCommand command) {
        return true;
    }
}
