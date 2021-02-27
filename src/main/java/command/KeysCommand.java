package command;

import command.model.ArrayRedisResult;
import command.model.IRedisResult;
import command.template.CommandConstants;
import command.template.NoKeyCommand;
import container.DataBase;
import utils.ArrayOperator;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 12:11
 */
public class KeysCommand extends NoKeyCommand {
    @Override
    public String name() {
        return CommandConstants.KEYS;
    }


    @Override
    protected IRedisResult innerExecute(DataBase db, ArrayOperator<String> args) {
        return new ArrayRedisResult(db.keys(args.get(0)));
    }

    @Override
    public int argsLength() {
        return 2;
    }

}
