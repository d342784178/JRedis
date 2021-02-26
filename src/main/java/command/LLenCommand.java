package command;

import command.model.IRedisResult;
import command.model.IntRedisResult;
import container.DataBase;
import operating.intf.List;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 12:11
 */
public class LLenCommand extends AbstractCommand<List> {
    @Override
    public String name() {
        return CommandConstants.LLEN;
    }

    @Override
    protected IRedisResult execute(DataBase db, String keyStr, List list, String[] args) {
        return new IntRedisResult(list.llen());
    }

    @Override
    public Class<?>[] support() {
        return new Class[]{List.class};
    }
}
