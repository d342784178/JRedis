package exception;

import command.model.IRedisResult;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-02-27
 * Time: 10:50
 */
public interface IRedisException {

    IRedisResult buildRedisResult();
}
