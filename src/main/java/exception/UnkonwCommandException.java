package exception;

import command.model.ErrRedisResult;
import command.model.IRedisResult;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-02-27
 * Time: 10:52
 */
public class UnkonwCommandException extends AbsRedisException {

    private String commandStr;

    public UnkonwCommandException(String commandStr) {
        this.commandStr = commandStr;
    }

    @Override
    public IRedisResult buildRedisResult() {
        return new ErrRedisResult(String.format("ERR unknown command `%s`, with args beginning with:", commandStr));
    }
}
