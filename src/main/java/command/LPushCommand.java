package command;

import command.model.IRedisResult;
import command.template.CommandConstants;
import command.template.KeyMultiArgCommand;
import container.DataBase;
import operating.ZipListList;
import operating.intf.List;
import utils.ArrayOperator;

import java.nio.charset.StandardCharsets;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 12:11
 */
public class LPushCommand extends KeyMultiArgCommand<List> {
    @Override
    public String name() {
        return CommandConstants.LPUSH;
    }

    @Override
    public int argsLength() {
        return -3;
    }

    @Override
    public Class<?>[] support() {
        return new Class[]{List.class};
    }


    @Override
    protected IRedisResult multiArgsExecute(DataBase db, String keyStr, List list, ArrayOperator<String> originArgs,
                                            ArrayOperator<String> sliceArgs) {
        list.lpush(sliceArgs.get(0).getBytes(StandardCharsets.UTF_8));
        return null;
    }

    @Override
    protected List newIfNotExist() {
        return new ZipListList();
    }
}
