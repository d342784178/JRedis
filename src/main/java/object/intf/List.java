package object.intf;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2020-12-12
 * Time: 16:28
 */
public interface List {
    /**
     * 左侧入队
     * O(1)
     * @param arg
     */
    void lpush(byte[] arg);

    /**
     * 右侧入队
     * O(1)
     * @param arg
     */
    void rpush(byte[] arg);

    /**
     * 左侧出队
     * O(1)
     * @return
     */
    byte[] lpop();

    /**
     * 右侧出队
     * O(1)
     * @return
     */
    byte[] rpop();

    /**
     * 返回index位置的内容
     * O(N)
     */
    byte[] lindex(int index);

    /**
     * 返回长度
     * O(1)
     * @return
     */
    int llen();

    /**
     * 插入新节点
     * O(N)
     * @return
     */
    int linsert(int index, byte[] arg);

    /**
     * 移除内容为arg的节点
     * O(N)
     * @param index
     * @return
     */
    int lrem(int index);

    /**
     * 删除[start,end]中的节点
     * O(N)
     * @return
     */
    int ltrim(int start, int end);

    /**
     * 对指定index的位置设值arg
     * O(N)
     * @return
     */
    int lset(int index, byte[] arg);

}
