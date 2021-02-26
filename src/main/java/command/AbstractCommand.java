package command;

import command.model.ErrRedisResult;
import command.model.IRedisResult;
import command.model.NilRedisResult;
import command.model.RedisCommand;
import container.DataBase;
import operating.intf.IRedisObject;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 17:16
 */
public abstract class AbstractCommand<T extends IRedisObject> implements ICommand {


    @Override
    public IRedisResult execute(RedisCommand command) {
        DataBase db     = command.getDataBase();
        String[] args   = command.getArgs();
        String   keyStr = command.getArgs()[1];

        IRedisResult result = null;
        IRedisObject r      = db.getRedisObject(keyStr);
        if (r != null) {
            if (!checkSupport(r)) {
                return new ErrRedisResult("WRONGTYPE Operation against a key holding the wrong kind of value");
            }
            result = execute(db, keyStr, (T) r, args);
        } else {
            r = newWhenNotExist();
            if (r != null) {
                db.build(keyStr, r);
                result = execute(db, keyStr, (T) r, args);
            } else {
                return new NilRedisResult();
            }
        }
        return result;
    }

    protected IRedisResult execute(DataBase db, String keyStr, T t, String[] args) {
        return null;
    }

    protected IRedisObject newWhenNotExist() {
        return null;
    }

    private boolean checkSupport(IRedisObject r) {
        Class[] support = support();
        for (Class cls : support) {
            if (cls.isInstance(r)) {
                return true;
            }
        }
        return false;
    }

}