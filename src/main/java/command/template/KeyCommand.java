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
        String   opearteStr = command.getArgs()[0];
        String   keyStr = command.getArgs()[1];

        IRedisResult result = null;
        IRedisObject r      = db.redisObject(keyStr);
        if (r != null) {
            //对象存在,则进行类型校验
            checkSupport(r);
        } else {
            //对象不存在,判断是否需要闯将
            DataBase.Builder<T> builder = newIfNotExist();
            if (builder != null) {
                //创建并加入到db中
                r = db.add(keyStr, builder);
            }
        }

        //执行指令
        if (haveArgs()) {
            result = innerExecute(db, keyStr, r == null ? null : (T) r, new ArrayOperator<>(args, 2));
        } else {
            result = innerExecute(db, keyStr, r == null ? null : (T) r);
        }
        //发送事件通知
        command.getDb().notify(keyStr,opearteStr);
        return result;
    }

    /**
     * 是否有参数
     * @return
     */
    protected boolean haveArgs() {
        return true;
    }

    /**
     * 不存在时是否自动创建
     * @return
     */
    protected DataBase.Builder<T> newIfNotExist() {
        return null;
    }

    /**
     * 指令执行(有参数)
     * @param db
     * @param keyStr
     * @param t
     * @param args
     * @return
     */
    protected IRedisResult innerExecute(DataBase db, String keyStr, T t, ArrayOperator<String> args) {
        return null;
    }

    /**
     * 指令执行(无参数)
     * @param db
     * @param keyStr
     * @param t
     * @return
     */
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
