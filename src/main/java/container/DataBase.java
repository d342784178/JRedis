package container;

import command.model.IntRedisResult;
import operating.intf.IRedisObject;
import org.testng.collections.Maps;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 17:08
 */
public class DataBase {
    Map<String, IRedisObject> redisObjectMap = Maps.newHashMap();

    public IRedisObject getRedisObject(String key) {
        return redisObjectMap.get(key);
    }

    public IRedisObject getRedisObjectOrDefault(String key, IRedisObject IRedisObject) {
        IRedisObject orDefault = redisObjectMap.getOrDefault(key, IRedisObject);
        redisObjectMap.put(key, orDefault);
        return orDefault;
    }

    public IntRedisResult del(String key) {
        IRedisObject remove = redisObjectMap.remove(key);
        return new IntRedisResult(remove != null ? 1 : 0);
    }

    public IRedisObject add(String key, IRedisObject IRedisObject) {
        redisObjectMap.put(key, IRedisObject);
        return IRedisObject;
    }

    public List<String> keys(String regex) {
        return redisObjectMap.keySet().stream().filter(s -> s.matches(regex)).collect(Collectors.toList());
    }

}
