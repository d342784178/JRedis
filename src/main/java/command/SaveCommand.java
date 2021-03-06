package command;

import command.template.CommandConstants;
import command.template.NoKeyNoArgCommand;
import container.DataBase;
import io.netty.channel.ChannelHandlerContext;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-03-06
 * Time: 20:18
 */
public class SaveCommand extends NoKeyNoArgCommand {
    @Override
    public String name() {
        return CommandConstants.SAVE;
    }

    @Override
    public int argsLength() {
        return 1;
    }

    @Override
    protected void innerExecute(ChannelHandlerContext ctx, DataBase db) {
        db.rdbSave();
    }
}
