package object;

import datastructure.ZipList;
import object.intf.List;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2020-12-12
 * Time: 16:36
 */
public class ZipListList implements List {

    ZipList zipList = new ZipList();

    @Override
    public void lpush(byte[] content) {
        zipList.zipListPush(content, true);
    }

    @Override
    public void rpush(byte[] content) {
        zipList.zipListPush(content, false);
    }

    @Override
    public byte[] lpop() {
        ZipList.Entry entry = zipList.zipListIndex(0);
        byte[]        array = entry._content().array();
        zipList.ziplistDelete(entry);
        //执行delete之后,内存已经被覆盖不能再使用entry对象
        return array;
    }

    @Override
    public byte[] rpop() {
        ZipList.Entry entry = zipList.zipListIndex(zipList._zllen() - 1);
        byte[]        array = entry._content().array();
        zipList.ziplistDelete(entry);
        return array;
    }


    @Override
    public byte[] lindex(int index) {
        return zipList.zipListIndex(index)._content().array();
    }

    @Override
    public int llen() {
        return zipList.ziplistLen();
    }

    @Override
    public int linsert(int index, byte[] content) {
        if (index == 0 || index == zipList.ziplistLen()) {
            zipList.zipListPush(content, index == 0);
        } else {
            ZipList.Entry entry = zipList.zipListIndex(index);
            zipList.zipListInsert(entry.getOffset(), content);
        }
        return 1;
    }

    @Override
    public int lrem(int index) {
        ZipList.Entry entry = zipList.zipListIndex(index);
        return zipList.ziplistDelete(entry);
    }

    @Override
    public int ltrim(int start, int end) {
        return zipList.ziplistDeleteRange(start, end - start);
    }

    @Override
    public int lset(int index, byte[] content) {
        ZipList.Entry entry = zipList.zipListIndex(index);
        zipList.ziplistDelete(entry);
        zipList.zipListInsert(entry.getOffset(), content);
        return 1;
    }
}
