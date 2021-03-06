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
 * Date: 2021-03-06
 * Time: 17:53
 */
public class RedisObjectManager {
    private final Map<String, IRedisObject> dict    = Maps.newHashMap();
    private final Map<String, Long>         expires = Maps.newHashMap();

    private final DataBase db;

    public RedisObjectManager(DataBase db) {
        this.db = db;
    }

    /**
     * 根据key查找redis对象(对象过期时不会返回)
     * @param key
     * @return
     */
    public IRedisObject redisObject(String key) {
        Long expire = expires.get(key);
        if (expire != null && System.currentTimeMillis() - expire > 0) {
            //过期
            del(key);
            return null;
        } else {
            //未过期
            return dict.get(key);
        }
    }

    public List<Map.Entry<String, IRedisObject>> redisObjects() {
        return dict.entrySet().stream()
                   .filter(s -> {
                       long expireTime = s.getValue().expire();
                       return expireTime < 0 || expireTime > System.currentTimeMillis();
                   }).collect(Collectors.toList());
    }


    /**
     * 根据key删除redis对象
     * @param key
     * @return
     */
    public IntRedisResult del(String key) {
        IRedisObject remove = dict.remove(key);
        expires.remove(key);
        return remove != null ? new IntRedisResult(1) : new IntRedisResult(0);
    }

    /**
     * 新增redis对象
     * @param key
     * @param builder
     * @return
     */
    public <T extends IRedisObject> T add(String key, Builder<T> builder) {
        IRedisObject iRedisObject = Accessor.accessor(db, key, builder.build());
        dict.put(key, iRedisObject);
        if (iRedisObject.expire() > 0) {
            expires.put(key, iRedisObject.expire());
        }
        return (T) iRedisObject;
    }

    /**
     * 根据regex匹配查找redis对象
     * @param regex
     * @return
     */
    public List<String> keys(String regex) {
        return dict.entrySet().stream()
                   .filter(s -> s.getKey().matches(regex))
                   .filter(s -> {
                       long expireTime = s.getValue().expire();
                       return expireTime < 0 || expireTime > System.currentTimeMillis();
                   }).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    /**
     * 给key设置过期时间
     * @param key
     * @param expireTime
     * @return
     */
    public int expire(String key, long expireTime) {
        IRedisObject iRedisObject = dict.get(key);
        if (iRedisObject != null) {
            long value = System.currentTimeMillis() + expireTime * 1000;
            iRedisObject.expire(value);
            expires.put(key, value);
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 返回剩余过期时间
     * @param key
     * @return
     */
    public long ttl(String key) {
        IRedisObject ro = dict.get(key);
        if (ro == null) {
            //不存在=>-2
            return -2;
        } else if (ro.expire() < 0) {
            //存在,无过期时间=>-1
            return -1;
        } else {
            long leftTime = (ro.expire() - System.currentTimeMillis());
            if (leftTime > 0) {
                //存在,未过期
                return leftTime / 1000;
            } else {
                //存在,已过期
                del(key);
                return -2;
            }
        }
    }

    public Map.Entry<String, Long> randomExpireKey() {
        if (expires.size() > 0) {
            return expires.entrySet().stream().findFirst().get();
        } else {
            return null;
        }
    }

    public static interface Builder<T extends IRedisObject> {
        T build();
    }
}
