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
public class Accessor {

    public static <T extends IRedisObject> T accessor(T redisObject) {
        return (T) Proxy.newProxyInstance(redisObject.getClass()
                                                     .getClassLoader(), new Class[]{List.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Object result = method.invoke(redisObject, args);
                        if (!method.getName().equalsIgnoreCase("idletime")) {
                            redisObject.access();
                        }
                        return result;
                    }
                });
    }
}
