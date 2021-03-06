package serialize;

import container.DataBase;
import lombok.Getter;
import lombok.ToString;
import operating.intf.IRedisObject;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-02-28
 * Time: 13:30
 */
public class SerializeManager {

    private final AtomicInteger dirty      = new AtomicInteger(0);
    private final SaveParam[]   saveParams = new SaveParam[4];
    private final DataBase      db;
    private final RdbHelper     rdbHelper;
    private       long          lastsave   = System.currentTimeMillis();

    {
        saveParams[0] = new SaveParam(900, 1);
        saveParams[1] = new SaveParam(300, 10);
        saveParams[2] = new SaveParam(60, 10000);
        saveParams[3] = new SaveParam(10, 2);
    }

    public SerializeManager(DataBase db) {
        this.db = db;
        rdbHelper = new RdbHelper(db);
    }

    public int increase(int increasement) {
        while (true) {
            int     i  = dirty.get();
            boolean ok = dirty.compareAndSet(i, i + increasement);
            if (ok) {
                return dirty.get();
            }
        }
    }

    public boolean saveCheck() {
        long currentTimeMillis = System.currentTimeMillis();
        long timeDuration      = currentTimeMillis - lastsave;
        int  dirty             = this.dirty.get();
        for (SaveParam saveParam : saveParams) {
            boolean checkResult = check(saveParam, timeDuration, dirty);
            if (checkResult) {
                System.out.println("满足" + saveParam + ",执行rdb");
                return checkResult;
            }
        }
        return false;
    }

    public void save() {
        rdbHelper.save();
        lastsave = System.currentTimeMillis();
        dirty.updateAndGet(operand -> 0);
    }

    public Map<String, IRedisObject> load() {
        return rdbHelper.load();
    }

    private static boolean check(SaveParam saveParam, long timeDuration, int dirty) {
        return timeDuration / 1000 >= saveParam.getSeconds() && dirty >= saveParam.getChanges();
    }


    @ToString
    private class SaveParam {
        @Getter
        private int seconds;
        @Getter
        private int changes;

        public SaveParam(int seconds, int changes) {
            this.seconds = seconds;
            this.changes = changes;
        }
    }
}
