package serialize;

import container.DataBase;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-02-28
 * Time: 13:46
 */
public class RdbCheckTask implements Runnable {
    private DataBase db;

    public RdbCheckTask(DataBase db) {
        this.db = db;
    }

    @Override
    public void run() {
        System.out.println("RdbCheckTask执行");
        db.rdbCheck();
    }
}
