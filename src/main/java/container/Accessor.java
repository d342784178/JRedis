package container;

import operating.intf.IRedisObject;
import operating.intf.List;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-02-27
 * Time: 16:14
 */
public class Accessor<T extends IRedisObject> implements InvocationHandler {

    public static <T extends IRedisObject> T accessor(DataBase db, String key, T redisObject) {
        return (T) Proxy.newProxyInstance(
                redisObject.getClass().getClassLoader(),
                new Class[]{List.class},
                new Accessor<T>(db, key, redisObject));
    }

    private DataBase db;
    private String   key;
    private T        redisObject;

    public Accessor(DataBase db, String key, T redisObject) {
        this.db = db;
        this.key = key;
        this.redisObject = redisObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result  = method.invoke(redisObject, args);
        String operate = method.getName();
        if (!operate.equalsIgnoreCase("idletime")) {
            redisObject.access();
        }
        return result;
    }
}
