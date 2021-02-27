package subscribe;

import command.model.ArrayRedisResult;
import org.testng.collections.Lists;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-02-27
 * Time: 21:58
 */
public class KeyEventEvent implements Event {
    @Override
    public EventType eventType() {
        return EventType.KEYEVENT;
    }

    private String key;
    private String operate;

    public KeyEventEvent(String key, String operate) {
        this.key = key;
        this.operate = operate;
    }

    @Override
    public void apply(Subscriber subscriber) {
        if (operate.equalsIgnoreCase(subscriber.getParam())) {
            subscriber.getCtx()
                      .writeAndFlush(new ArrayRedisResult(Lists.newArrayList("message", subscriber.getArg(), key)));
        }
    }
}
