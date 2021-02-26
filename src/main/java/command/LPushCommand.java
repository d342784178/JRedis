package command;

import command.model.IRedisResult;
import command.model.IntRedisResult;
import container.DataBase;
import operating.ZipListList;
import operating.intf.IRedisObject;
import operating.intf.List;

import java.nio.charset.StandardCharsets;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 12:11
 */
public class LPushCommand extends AbstractCommand<List> {
    @Override
    public String name() {
        return CommandConstants.LPUSH;
    }

    @Override
    protected IRedisResult execute(DataBase db, String keyStr, List t, String[] args) {
        int num = 0;
        for (int i = 2; i < args.length; i++) {
            t.lpush(args[i].getBytes(StandardCharsets.UTF_8));
            num += 1;
        }
        return new IntRedisResult(num);
    }

    @Override
    protected IRedisObject newWhenNotExist() {
        return new ZipListList();
    }

    @Override
    public Class<?>[] support() {
        return new Class[]{List.class};
    }

}
