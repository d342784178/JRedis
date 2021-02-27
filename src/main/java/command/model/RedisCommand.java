package command.model;

import command.CommandManager;
import command.template.ICommand;
import container.DataBase;
import exception.UnkonwCommandException;
import io.netty.channel.ChannelHandlerContext;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 12:07
 */
public class RedisCommand {
    private String[]              args;
    private ChannelHandlerContext ctx;
    private DataBase              db;

    public RedisCommand(String[] args, ChannelHandlerContext ctx, DataBase db) {
        this.args = args;
        this.ctx = ctx;
        this.db = db;
    }

    public Object exec() {
        ICommand iCommand = CommandManager.obtainCommand(args[0]);
        if (iCommand == null) {
            throw new UnkonwCommandException(args[0]);
        }
        return iCommand.execute(this);
    }

    public DataBase getDb() {
        return db;
    }

    public String[] getArgs() {
        return args;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }
}
