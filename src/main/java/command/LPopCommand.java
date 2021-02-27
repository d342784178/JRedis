package command;

import command.model.IRedisResult;
import command.model.StrRedisResult;
import command.template.CommandConstants;
import command.template.KeyNoArgCommand;
import container.DataBase;
import operating.intf.List;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 12:11
 */
public class LPopCommand extends KeyNoArgCommand<List> {
    @Override
    public String name() {
        return CommandConstants.LPOP;
    }

    @Override
    public int argsLength() {
        return 2;
    }

    @Override
    public Class<?>[] support() {
        return new Class[]{List.class};
    }

    @Override
    protected IRedisResult innerExecute(DataBase db, String keyStr, List t) {
        byte[] lpop = t.lpop();
        if (t.llen() == 0) {
            db.del(keyStr);
        }
        return new StrRedisResult(lpop);
    }


}
