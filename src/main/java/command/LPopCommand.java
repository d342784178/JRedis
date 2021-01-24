package command;

import network.model.RedisCommand;

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
        return true;
    }
}
