package command.template;

import command.model.IRedisResult;
import command.model.RedisCommand;
import container.DataBase;
import operating.intf.IRedisObject;
import utils.ArrayOperator;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-02-27
 * Time: 11:56
 */
public abstract class KeyCommand<T extends IRedisObject> extends AbstractCommand implements IKeyCommand {
    @Override
    final public boolean key() {
        return true;
    }


    @Override
    final public IRedisResult execute(RedisCommand command) {
        super.execute(command);
        DataBase db     = command.getDb();
        String[] args   = command.getArgs();
        String   keyStr = command.getArgs()[1];

        IRedisResult result = null;
        IRedisObject r      = db.getRedisObject(keyStr);
        if (r != null) {
            checkSupport(r);
        }
        if (haveArgs()) {
            result = innerExecute(db, keyStr, r == null ? null : (T) r, new ArrayOperator<>(args, 2));
        } else {
            result = innerExecute(db, keyStr, r == null ? null : (T) r);
        }
        return result;
    }

    protected boolean haveArgs() {
        return true;
    }

    protected IRedisResult innerExecute(DataBase db, String keyStr, T t, ArrayOperator<String> args) {
        return null;
    }

    protected IRedisResult innerExecute(DataBase db, String keyStr, T t) {
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
