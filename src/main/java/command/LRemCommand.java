package command;

import command.model.IRedisResult;
import container.DataBase;
import operating.intf.List;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 12:11
 */
public class LRemCommand extends AbstractCommand<List> {
    @Override
    public String name() {
        return CommandConstants.LREM;
    }


    @Override
    protected IRedisResult execute(DataBase db, String keyStr, List list, String[] args) {
        return null;
    }

    @Override
    public Class[] support() {
        return new Class[]{List.class};
    }
}
