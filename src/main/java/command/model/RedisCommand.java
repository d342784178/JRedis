package command.model;

import command.CommandManager;
import command.ICommand;
import container.DataBase;
import exception.UnkonwCommandException;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 12:07
 */
public class RedisCommand {
    @Getter
    private String[]              args;
    @Getter
    private ChannelHandlerContext ctx;

    @Getter
    private DataBase dataBase;

    public RedisCommand(String[] args, ChannelHandlerContext ctx, DataBase dataBase) {
        this.args = args;
        this.ctx = ctx;
        this.dataBase = dataBase;
    }

    public Object exec() {
        ICommand iCommand = CommandManager.obtainCommand(args[0]);
        if (iCommand == null) {
            throw new UnkonwCommandException(args[0]);
        }
        return iCommand.execute(this);
    }
}
