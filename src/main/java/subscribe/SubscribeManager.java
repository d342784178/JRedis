package subscribe;



import com.google.common.collect.Lists;

import java.util.List;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-02-27
 * Time: 21:56
 */
public class SubscribeManager {

    private List<Subscriber> keySpaceSubscriber = Lists.newCopyOnWriteArrayList();
    private List<Subscriber> keyEventSubscriber = Lists.newCopyOnWriteArrayList();

    public void register(Event.EventType eventType, Subscriber subscriber) {
        if (Event.EventType.KEYEVENT == eventType) {
            keyEventSubscriber.add(subscriber);
        } else {
            keySpaceSubscriber.add(subscriber);
        }
    }

    public void unregister(Event.EventType eventType, Subscriber subscriber) {
        if (Event.EventType.KEYEVENT == eventType) {
            keyEventSubscriber.remove(subscriber);
        } else {
            keySpaceSubscriber.remove(subscriber);
        }
    }

    public void notify(Event event) {
        Event.EventType eventType = event.eventType();
        if (Event.EventType.KEYEVENT == eventType) {
            for (Subscriber subscriber : keyEventSubscriber) {
                event.apply(subscriber);
            }
        } else {
            for (Subscriber subscriber : keySpaceSubscriber) {
                event.apply(subscriber);
            }
        }

    }


}
