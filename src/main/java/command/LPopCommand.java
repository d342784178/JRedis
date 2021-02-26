package command;

import command.model.IRedisResult;
import command.model.StrRedisResult;
import container.DataBase;
import operating.intf.List;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 12:11
 */
public class LPopCommand extends AbstractCommand<List> {
    @Override
    public String name() {
        return CommandConstants.LPOP;
    }

    @Override
    protected IRedisResult execute(DataBase db, String keyStr, List t, String[] args) {
        byte[] lpop = t.lpop();
        return new StrRedisResult(lpop);
    }

    @Override
    public Class<?>[] support() {
        return new Class[]{List.class};
    }
}
