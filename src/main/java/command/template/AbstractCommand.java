package command.template;

import command.model.IRedisResult;
import command.model.RedisCommand;
import exception.WrongArgsException;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 17:16
 */
public abstract class AbstractCommand implements ICommand {
    @Override
    public IRedisResult execute(RedisCommand command) {
        checkArgsLength(command.getArgs());
        return null;
    }


    private void checkArgsLength(String[] args) {
        int     argsLength = argsLength();
        boolean moreThan   = argsLength < 0;
        argsLength = Math.abs(argsLength);
        if (moreThan) {
            if (args.length < argsLength) {
                throw new WrongArgsException(args[0]);
            }
        } else {
            if (args.length != argsLength) {
                throw new WrongArgsException(args[0]);
            }
        }
    }
}