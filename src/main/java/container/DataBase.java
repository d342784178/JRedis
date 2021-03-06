package container;

import command.model.IntRedisResult;
import operating.intf.IRedisObject;
import serialize.RdbManager;
import subscribe.*;

import java.util.List;
import java.util.Map;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 17:08
 */
public class DataBase {

    private RedisObjectManager redisObjectManager = new RedisObjectManager(this);
    private SubscribeManager   subscribeManager   = new SubscribeManager();
    private RdbManager         rdbManager         = new RdbManager(this);


    /**
     * 根据key查找redis对象(对象过期时不会返回)
     * @param key
     * @return
     */
    public IRedisObject redisObject(String key) {
        return redisObjectManager.redisObject(key);
    }

    public List<Map.Entry<String, IRedisObject>> redisObjects() {
        return redisObjectManager.redisObjects();
    }


    /**
     * 根据key删除redis对象
     * @param key
     * @return
     */
    public IntRedisResult del(String key) {
        return redisObjectManager.del(key);
    }

    /**
     * 新增redis对象
     * @param key
     * @param builder
     * @return
     */
    public IRedisObject add(String key, RedisObjectManager.Builder builder) {
        return redisObjectManager.add(key, builder);
    }

    /**
     * 根据regex匹配查找redis对象
     * @param regex
     * @return
     */
    public List<String> keys(String regex) {
        return redisObjectManager.keys(regex);
    }

    //=======================过期

    /**
     * 给key设置过期时间
     * @param key
     * @param expireTime
     * @return
     */
    public int expire(String key, long expireTime) {
        return redisObjectManager.expire(key, expireTime);
    }

    /**
     * 返回剩余过期时间
     * @param key
     * @return
     */
    public long ttl(String key) {
        return redisObjectManager.ttl(key);
    }

    public Map.Entry<String, Long> randomExpireKey() {
        return redisObjectManager.randomExpireKey();
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

    //================RDB
    public void rdbCheck() {
        if (rdbManager.saveCheck()) {
            rdbSave();
        }
    }

    public void rdbSave() {
        rdbManager.save();
    }

    public void rdbLoad() {
        Map<String, IRedisObject> load = rdbManager.load();
        for (Map.Entry<String, IRedisObject> entry : load.entrySet()) {
            add(entry.getKey(), entry::getValue);
        }
    }

    public void increaseDirty() {
        rdbManager.increaseDirty(1);
    }

}
