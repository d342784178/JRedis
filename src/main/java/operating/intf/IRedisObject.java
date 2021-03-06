package operating.intf;

import serialize.RdbEnum;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 17:09
 */
public interface IRedisObject {

    long idletime();

    void access();

    RdbEnum type();

    //设置过期时间
    void expire(long expireTime);

    long expire();

    default IRedisObject origin() {
        return this;
    }

}
