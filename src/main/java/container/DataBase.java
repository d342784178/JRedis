package container;

import operating.intf.RedisObject;
import org.testng.collections.Maps;

import java.util.Map;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 17:08
 */
public class DataBase {
    Map<String, RedisObject> redisObjectMap = Maps.newHashMap();

    public RedisObject getRedisObject(String key) {
        return redisObjectMap.get(key);
    }

    public RedisObject getRedisObjectOrDefault(String key, RedisObject redisObject) {
        RedisObject orDefault = redisObjectMap.getOrDefault(key, redisObject);
        redisObjectMap.put(key, orDefault);
        return orDefault;
    }

}
