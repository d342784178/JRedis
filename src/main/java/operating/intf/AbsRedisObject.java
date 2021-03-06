package operating.intf;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-02-27
 * Time: 15:34
 */
public abstract class AbsRedisObject implements IRedisObject {
    private long lru;
    private long expireTime = -1;

    public void access() {
        lru = System.currentTimeMillis();
    }

    @Override
    public long idletime() {
        return System.currentTimeMillis() - lru;
    }

    @Override
    public long expire() {
        return expireTime;
    }

    @Override
    public void expire(long expireTime) {
        this.expireTime = expireTime;
    }
}
