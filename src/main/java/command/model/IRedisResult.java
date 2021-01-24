package command.model;

import io.netty.buffer.ByteBuf;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 21:18
 */
public interface IRedisResult {

    ByteBuf encode();

}
