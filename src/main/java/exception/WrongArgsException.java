package exception;

import command.model.ErrRedisResult;
import command.model.IRedisResult;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-02-27
 * Time: 10:52
 */
public class WrongArgsException extends AbsRedisException {

    private String commandStr;

    public WrongArgsException(String commandStr) {
        this.commandStr = commandStr;
    }

    @Override
    public IRedisResult buildRedisResult() {
        return new ErrRedisResult(String.format("ERR wrong number of arguments for '%s' command", commandStr));
    }
}
