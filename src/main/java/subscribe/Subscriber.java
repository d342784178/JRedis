package subscribe;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-02-27
 * Time: 21:57
 */
public class Subscriber {
    @Getter
    private ChannelHandlerContext ctx;
    @Getter
    private String                param;
    @Getter
    private String                arg;

    public Subscriber(ChannelHandlerContext ctx, String param, String arg) {
        this.ctx = ctx;
        this.param = param;
        this.arg = arg;
    }

}
