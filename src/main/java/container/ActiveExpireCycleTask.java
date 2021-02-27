package container;

import java.util.Map;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-02-27
 * Time: 21:21
 */
public class ActiveExpireCycleTask implements Runnable {
    private DataBase db;

    public ActiveExpireCycleTask(DataBase db) {
        this.db = db;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();

        while (System.currentTimeMillis() - start <= 10) {
            //执行超出10ms就结束
            Map.Entry<String, Long> entry = db.randomExpireKey();
            if (entry != null && entry.getValue() < System.currentTimeMillis()) {
                db.del(entry.getKey());
                System.out.println("定时清理过期key:" + entry.getKey());
            }
        }
    }
}
