package container;

import org.testng.collections.Maps;

import java.util.Map;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-02-27
 * Time: 16:51
 */
public class RedisObjectExpireMap {
    private Map<String, Long> expiresMap = Maps.newHashMap();

    private RedisObjectMap redisObjectMap;

    public RedisObjectExpireMap(RedisObjectMap redisObjectMap) {
        this.redisObjectMap = redisObjectMap;
    }


    public int expire(String key, long expireTime) {
        if (redisObjectMap.getRedisObject(key) != null) {
            expiresMap.put(key, System.currentTimeMillis() + expireTime * 1000);
            return 1;
        } else {
            return 0;
        }
    }

    public long ttl(String key) {
        if (redisObjectMap.getRedisObject(key) != null) {
            return -1;
        } else if (!expiresMap.containsKey(key)) {
            return -2;
        } else {
            return (expiresMap.get(key) - System.currentTimeMillis()) / 1000;
        }
    }
}
