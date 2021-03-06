package serialize;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-03-06
 * Time: 11:49
 */
public enum RdbEnum {
    REDIS_STRING(0),
    REDIS_LIST(1),
    REDIS_SET(2),
    REDIS_ZSET(3),
    REDIS_HASH(4),
    REDIS_HASH_ZIPMAP(9),
    REDIS_LIST_ZIPLIST(10),
    REDIS_SET_INTSET(11),
    REDIS_ZSET_ZIPLIST(12),
    REDIS_HASH_ZIPLIST(13),

    REDIS_EXPIRETIME_MS(252),
    REDIS_EXPIRETIME(253),
    REDIS_SELECTDB(254),
    REDIS_EOF(255),
    ;
    private byte code;

    RdbEnum(int code) {
        this.code = (byte) code;
    }

    public byte code() {
        return code;
    }

    public static RdbEnum codeOf(byte code) {
        for (RdbEnum value : values()) {
            if (code == value.code()) {
                return value;
            }
        }
        return null;
    }


    @Override
    public String toString() {
        return name();
    }
}
