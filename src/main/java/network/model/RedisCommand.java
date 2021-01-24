package network.model;

import command.CommandManager;
import command.ICommand;
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
    private String[]              command;
    @Getter
    private ChannelHandlerContext ctx;

    public RedisCommand(String[] command, ChannelHandlerContext ctx) {
        this.command = command;
        this.ctx = ctx;
    }

    public Object exec() {
        ICommand iCommand = CommandManager.obtainCommand(command[0]);
        return iCommand.execute(this);
    }
}
