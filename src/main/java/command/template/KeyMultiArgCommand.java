package command.template;

import command.model.IRedisResult;
import command.model.IntRedisResult;
import container.DataBase;
import operating.intf.IRedisObject;
import utils.ArrayOperator;

import java.util.Iterator;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-02-27
 * Time: 12:02
 */
public abstract class KeyMultiArgCommand<T extends IRedisObject> extends KeyCommand<T> implements IKeyMultiArgsCommand {
    @Override
    public int argsStep() {
        return 1;
    }

    @Override
    final protected IRedisResult innerExecute(DataBase db, String keyStr, T t, ArrayOperator<String> args) {
        int                             argsStep = argsStep();
        Iterator<ArrayOperator<String>> iterable = args.iterable(argsStep);
        int                             num      = 0;
        while (iterable.hasNext()) {
            ArrayOperator<String> sliceArgs = iterable.next();
            IRedisResult          result    = multiArgsExecute(db, keyStr, t, args, sliceArgs);
            if (result != null) {
                num += 1;
            }
        }
        return new IntRedisResult(num);

    }

    abstract protected IRedisResult multiArgsExecute(DataBase db, String keyStr, T t, ArrayOperator<String> originArgs,
                                                     ArrayOperator<String> sliceArgs);

}
