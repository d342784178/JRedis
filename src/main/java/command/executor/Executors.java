package command.executor;

import command.model.IRedisResult;
import command.model.RedisCommand;
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
        map.put(ReadExecutor.class, new ReadExecutor());
    }

    private AbstractExecutor executor;

    public static Executors build(Class cls) {
        return new Executors(map.get(cls));
    }

    private Executors(AbstractExecutor executor) {
        this.executor = executor;
    }

    public IRedisResult execute(RedisCommand command, IExecutor.ExecuteCallback callback) {
        return executor.execute(command, callback);
    }
}
