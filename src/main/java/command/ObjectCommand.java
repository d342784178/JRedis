package command;

import command.model.IRedisResult;
import command.model.IntRedisResult;
import command.model.NilRedisResult;
import command.model.RedisCommand;
import command.template.AbstractCommand;
import command.template.CommandConstants;
import container.DataBase;
import exception.WrongArgsException;
import operating.intf.IRedisObject;
import utils.ArrayOperator;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 12:11
 */
public class ObjectCommand extends AbstractCommand {
    @Override
    public String name() {
        return CommandConstants.OBJECT;
    }

    @Override
    public int argsLength() {
        return -3;
    }

    @Override
    public IRedisResult execute(RedisCommand command) {
        super.execute(command);
        DataBase db = command.getDb();
        //idletime key
        ArrayOperator<String> args = new ArrayOperator<String>(command.getArgs(), 1);

        //idletime
        String operateStr = args.get(0);
        if ("idletime".equalsIgnoreCase(operateStr)) {
            //key...
            ArrayOperator<String> operateArgs = args.slice(1);
            if (operateArgs.length() < 1) {
                throw new WrongArgsException(operateArgs.get(0));
            }
            IRedisObject redisObject = db.redisObject(operateArgs.get(0));
            if (redisObject == null) {
                return new NilRedisResult();
            }
            return new IntRedisResult(redisObject.idletime());
        } else {
            return new NilRedisResult();
        }

    }
}
