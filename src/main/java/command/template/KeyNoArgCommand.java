package command.template;

import operating.intf.IRedisObject;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-02-27
 * Time: 12:02
 */
public abstract class KeyNoArgCommand<T extends IRedisObject> extends KeyCommand<T> implements IKeyNoArgsCommand {
    @Override
    protected boolean haveArgs() {
        return false;
    }
}
