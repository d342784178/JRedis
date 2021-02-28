package container;

import command.model.IntRedisResult;
import operating.intf.IRedisObject;
import org.testng.collections.Maps;
import subscribe.*;

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
    private Map<String, IRedisObject> redisObjectMap   = Maps.newHashMap();
    private Map<String, Long>         expiresMap       = Maps.newHashMap();
    private SubscribeManager          subscribeManager = new SubscribeManager();


    /**
     * 根据key查找redis对象(对象过期时不会返回)
     * @param key
     * @return
     */
    public IRedisObject redisObject(String key) {
        Long expire = expiresMap.get(key);
        if (expire != null && System.currentTimeMillis() - expire > 0) {
            //过期
            del(key);
            return null;
        } else {
            //未过期
            return redisObjectMap.get(key);
        }
    }


    /**
     * 根据key删除redis对象
     * @param key
     * @return
     */
    public IntRedisResult del(String key) {
        IRedisObject remove = redisObjectMap.remove(key);
        expiresMap.remove(key);
        return remove != null ? new IntRedisResult(1) : null;
    }

    /**
     * 新增redis对象
     * @param key
     * @param builder
     * @return
     */
    public IRedisObject add(String key, Builder builder) {
        IRedisObject accessor = Accessor.accessor(this, key, builder.build());
        redisObjectMap.put(key, accessor);
        return accessor;
    }

    /**
     * 根据regex匹配查找redis对象
     * @param regex
     * @return
     */
    public List<String> keys(String regex) {
        return redisObjectMap.keySet().stream()
                             .filter(s -> s.matches(regex))
                             .filter(s -> {
                                 Long expireTime = expiresMap.get(s);
                                 return expireTime == null || expireTime > System.currentTimeMillis();
                             }).collect(Collectors.toList());
    }

//=======================过期

    /**
     * 给key设置过期时间
     * @param key
     * @param expireTime
     * @return
     */
    public int expire(String key, long expireTime) {
        if (redisObjectMap.containsKey(key)) {
            expiresMap.put(key, System.currentTimeMillis() + expireTime * 1000);
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
        if (!redisObjectMap.containsKey(key)) {
            //不存在=>-2
            return -2;
        } else if (!expiresMap.containsKey(key)) {
            //存在,无过期时间=>-1
            return -1;
        } else {
            long leftTime = (expiresMap.get(key) - System.currentTimeMillis());
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
        if (expiresMap.size() > 0) {
            return expiresMap.entrySet().stream().findFirst().get();
        } else {
            return null;
        }
    }
//==================发布订阅

    public void register(Event.EventType eventType, Subscriber subscriber) {
        subscribeManager.register(eventType, subscriber);
    }

    public void unregister(Event.EventType eventType, Subscriber subscriber) {
        subscribeManager.unregister(eventType, subscriber);
    }
    public void notify(String key, String operate) {
        subscribeManager.notify(new KeySpaceEvent(key, operate));
        subscribeManager.notify(new KeyEventEvent(key, operate));
    }

    public static interface Builder<T extends IRedisObject> {
        T build();
    }
}
