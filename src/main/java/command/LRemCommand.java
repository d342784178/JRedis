package command;

import command.model.IRedisResult;
import command.template.CommandConstants;
import command.template.KeyCommand;
import container.DataBase;
import operating.intf.List;
import utils.ArrayOperator;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 12:11
 */
public class LRemCommand extends KeyCommand<List> {
    @Override
    public String name() {
        return CommandConstants.LREM;
    }

    @Override
    public int argsLength() {
        return 0;
    }


    @Override
    public Class[] support() {
        return new Class[]{List.class};
    }

    @Override
    protected IRedisResult innerExecute(DataBase db, String keyStr, List list, ArrayOperator<String> args) {
        return null;
    }
}
