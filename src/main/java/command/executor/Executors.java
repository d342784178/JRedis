package command.executor;

import network.model.RedisCommand;
import org.testng.collections.Maps;

import java.util.Map;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 17:45
 */
public class Executors {
    private static Map<Class, AbstractExecutor> map = Maps.newHashMap();

    static {
        map.put(SingleTargetExecutor.class, new SingleTargetExecutor());
        map.put(MultiTargetExecutor.class, new MultiTargetExecutor());
    }

    private AbstractExecutor executor;

    public static Executors build(Class cls) {
        return new Executors(map.get(cls));
    }

    private Executors(AbstractExecutor executor) {
        this.executor = executor;
    }

    public <T> T execute(RedisCommand command, IExecutor.ExecuteCallback callback) {
        return executor.execute(command, callback);
    }
}
