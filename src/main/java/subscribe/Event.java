package subscribe;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-02-27
 * Time: 21:57
 */
public interface Event {
    enum EventType {
        KEYSPACE(), KEYEVENT();

        EventType() {
        }
    }

    EventType eventType();

    void apply(Subscriber subscriber);
}
