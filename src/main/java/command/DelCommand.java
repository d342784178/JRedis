package command;

import command.model.IRedisResult;
import command.template.CommandConstants;
import command.template.KeyNoArgCommand;
import container.DataBase;
import operating.intf.IRedisObject;
import operating.intf.List;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 12:11
 */
public class DelCommand extends KeyNoArgCommand {
    @Override
    public String name() {
        return CommandConstants.DEL;
    }

    @Override
    public int argsLength() {
        return -2;
    }


    @Override
    public Class<?>[] support() {
        return new Class[]{List.class};
    }

    @Override
    protected IRedisResult innerExecute(DataBase db, String keyStr, IRedisObject t) {
        return db.del(keyStr);
    }
}
