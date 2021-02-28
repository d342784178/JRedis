package command;

import command.model.ArrayRedisResult;
import command.model.IRedisResult;
import command.template.CommandConstants;
import command.template.NoKeyCommand;
import container.DataBase;
import io.netty.channel.ChannelHandlerContext;
import org.testng.collections.Lists;
import subscribe.Event;
import subscribe.Subscriber;
import utils.ArrayOperator;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 12:11
 */
public class SubscribeCommand extends NoKeyCommand {
    @Override
    public String name() {
        return CommandConstants.SUBSCRIBE;
    }

    @Override
    public int argsLength() {
        return 2;
    }

    @Override
    protected IRedisResult innerExecute(ChannelHandlerContext ctx, DataBase db, ArrayOperator<String> args) {
        super.innerExecute(ctx, db, args);

        int oneIndex = args.get(0).indexOf("@");
        int twoIndex = args.get(0).indexOf(":");
        String eventTypeStr = args.get(0).substring(0, oneIndex).toUpperCase()
                                  .replace("__", "");
        String dbNum = args.get(0).substring(oneIndex + 1, twoIndex).replace("__", "");
        String param = args.get(0).substring(twoIndex + 1);

        Event.EventType eventType  = Event.EventType.valueOf(eventTypeStr);
        Subscriber      subscriber = new Subscriber(ctx, param, args.get(0));
        //注册订阅者
        db.register(eventType, subscriber);
        //监听渠道关闭事件注销订阅者
        ctx.channel().closeFuture().addListener(future -> db.unregister(eventType, subscriber));
        return new ArrayRedisResult(Lists.newArrayList("subscribe", args.get(0), "1"));
    }
}
