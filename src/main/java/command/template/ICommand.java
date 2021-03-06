package command.template;

import command.model.IRedisResult;
import command.model.RedisCommand;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 12:11
 */
public interface ICommand {
    String name();

    IRedisResult execute(RedisCommand command);

    /**
     * 参数长度
     * @return
     */
    int argsLength();
}
