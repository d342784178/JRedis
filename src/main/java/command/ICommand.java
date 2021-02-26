package command;

import command.model.IRedisResult;
import command.model.RedisCommand;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 12:11
 */
public interface ICommand<T extends IRedisResult> {
    String name();

    T execute(RedisCommand command);


}
