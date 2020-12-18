package datastructure;

import utils.ByteArray;
import utils.ByteUtil;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2020-12-11
 * Time: 21:02
 */
public class ZipList {
    public static final int INITIAL_CAPACITY = 11;

    private ByteArray content;

    private int zlbytesI = 0;
    private int zltailI  = 4;
    private int zllenI   = 8;
    private int zlentryI = 10;

    /**
     * zipListNew
     */
    public ZipList() {
        this.content = new ByteArray(INITIAL_CAPACITY);
        content.setInt(zlbytesI, INITIAL_CAPACITY);
        content.setInt(zltailI, content.capacity() - 1);
        content.setShort(zllenI, 0);
    }

    private int _zlbytes() {
        return content.getInt(zlbytesI);
    }

    private void _zlbytes(int zlbytes) {
        content.setInt(zlbytesI, zlbytes);
    }

    private Entry _zltail() {
        //尾节点偏移量
        int tailEntryOffset = content.getInt(zltailI);
        //尾节点长度=ziplist总长度-1-尾节点偏移量
        return _entry(tailEntryOffset);
    }

    private void _zltail(int zltail) {
        content.setInt(zltailI, zltail);
    }

    private int _zlend() {
        return _zlbytes() - 1;
    }

    public int _zllen() {
        return content.getShort(zllenI);
    }

    private void _zllen(int zllen) {
        content.setShort(zllenI, zllen);
    }

    private Entry _zlentry() {
        return _entry(zlentryI);
    }

    private void _resize(int newCapacity) {
        content.newCapacity(newCapacity);
    }

    private Entry _entry(int offset) {
        return _entry(offset, true);
    }

    private Entry _entry(int offset, boolean check) {
        //已经指向队尾,返回空
        if (check && offset == _zlend()) {
            return null;
        }
        return new Entry(offset, content.slice(offset, content.capacity() - offset));
    }

    public static class Entry {
        private ByteArray content;
        //当前结点在ziplist.content中的偏移量
        private int       offset;


        private int preEntryLengthI = 0;
        private int encodeI         = 1;

        /**
         * 在ziplist上的offset位置上构造新节点
         * @param zipList
         * @param offset
         * @param content
         * @param offset
         * @param next
         * @return
         */
        public static Entry init(ZipList zipList, int offset, byte[] content, int preEntryLength, Entry next) {
            //计算节点大小
            byte[] encode    = encode(content);
            int    entrySize = 1 + encode.length + content.length;
            //resize扩容
            zipList._resize(zipList._zlbytes() + entrySize);
            //节点迁移
            if (next != null) {
                zipList.moveTo(next, next.offset + entrySize);
            }
            //写入新节点
            Entry entry = zipList._entry(offset, false);
            entry._preEntryLengthI(preEntryLength);
            entry._encodeI(encode);
            entry._content(content);
            return entry;
        }

        private static byte[] encode(byte[] content) {
            byte[] encode;
            if (content.length % 16 == 0) {//整数编码
                if (content.length == 16) {//1100 0000 16位
                    encode = new byte[1];
                    ByteUtil.setByte(encode, 0, 0xc0);
                } else if (content.length == 32) {//1101 0000 32位
                    encode = new byte[1];
                    ByteUtil.setByte(encode, 0, 0xd0);
                } else {//1110 0000 64位
                    encode = new byte[1];
                    ByteUtil.setByte(encode, 0, 0xe0);
                }
            } else {//字符编码
                if (content.length < (1 << 6)) {
                    encode = new byte[1];
                    ByteUtil.setByte(encode, 0, 0x00 | content.length);
                } else if (content.length < (1 << 14)) {
                    encode = new byte[2];
                    ByteUtil.setShort(encode, 0, 0x4000 | content.length);
                } else {
                    encode = new byte[5];
                    ByteUtil.setByte(encode, 0, 0x0000);
                    ByteUtil.setInt(encode, 1, content.length);

                }
            }
            return encode;
        }

        /**
         * 编码解析
         * @param values [0]:编码长度 [1]:内容长度
         */
        private void _encodeDecode(int[] values) {
            byte encode        = content.getByte(encodeI);
            int  contentLength = 0;
            int  encodeLength  = 0;


            int encodeType = encode >> 6;
            if (encodeType < 2) {//存放字符串,编码类型为00/01/10
                if (encodeType == 0) {//编码长度为1字节,内容长度为低1*8-2=6字节
                    encodeLength = 1;
                    contentLength = encode & 0x3f;
                } else if (encodeType == 1) {//编码长度为2字节,内容长度为低2*8-2=14字节
                    encodeLength = 2;
                    contentLength = ((encode & 0x3f) << 8) | content.getByte(encodeI + 1);

                } else if (encodeType == 2) {//编码长度为5字节,内容长度为5*8-2=38字节
                    encodeLength = 5;
                    contentLength = (content.getByte(encodeI + 1) << 24) |
                            (content.getByte(encodeI + 2) << 16) |
                            (content.getByte(encodeI + 3) << 8) |
                            (content.getByte(encodeI + 4));
                } else {
                    throw new RuntimeException(String.format("encodeType:%d error",
                            Integer.toBinaryString(encodeType)));
                }
            } else {//存放整数,编码类型为11.
                //编码长度为1字节,内容长度
                encodeLength = 1;
                switch (encode) {
                    case (byte) (0xc0 | 0 << 4):
                        //1100 0000 16位
                        contentLength = 2;
                    case (byte) (0xc0 | 1 << 4):
                        //1101 0000 32位
                        contentLength = 4;
                    case (byte) (0xc0 | 2 << 4):
                        //1110 0000 64位
                        contentLength = 16;
                    default:
                        throw new IllegalArgumentException("编码异常:" + encode);
                }
            }
            values[0] = encodeLength;
            values[1] = contentLength;
        }


        /**
         * @param offset
         * @param content 可读范围是大于entry的
         */
        public Entry(int offset, ByteArray content) {
            this.content = content;
            this.offset = offset;
        }


        /**
         * @return 前置节点长度
         */
        private int _preEntryLengthI() {
            return content.getByte(preEntryLengthI);
        }

        /**
         * 更新前置节点长度
         * @param preEntryLength
         */
        private void _preEntryLengthI(int preEntryLength) {
            content.setByte(preEntryLengthI, preEntryLength);
        }

        /**
         * 更新编码
         * @param encode
         */
        private void _encodeI(byte[] encode) {
            content.setBytes(encodeI, encode, 0, encode.length);
        }

        /**
         * @return 内容
         */
        public ByteArray _content() {
            int[] encodeValues = new int[2];
            _encodeDecode(encodeValues);
            return content.slice(encodeI + encodeValues[0], encodeValues[1]);
        }

        /**
         * 更新内容
         * @param s
         */
        private void _content(byte[] s) {
            int[] encodeValues = new int[2];
            _encodeDecode(encodeValues);
            content.setBytes(encodeI + encodeValues[0], s, 0, s.length);
        }


        private Entry _preEntry(ZipList zl) {
            byte preLength = content.getByte(preEntryLengthI);
            return zl._entry(offset - preLength);
        }

        private Entry _nextEntry(ZipList zl) {
            int[] encodeValues = new int[2];
            _encodeDecode(encodeValues);
            return zl._entry(offset + 1 + encodeValues[0] + encodeValues[1]);
        }

        public int getOffset() {
            return offset;
        }

        private int size() {
            //因为content为ziplist的切片,只有头没有尾,不能直接通过capacity获取长度
            int[] values = new int[2];
            _encodeDecode(values);
            return 1 + values[0] + values[1];
        }


        public void updatePreEntryLength(int preEntryLength) {
            _preEntryLengthI(preEntryLength);
            //TODO 假定preEntryLength占用1字节,忽略连锁更新
        }
    }


    private void moveTo(Entry entry, int dstOffset) {
        Entry next = entry._nextEntry(this);
        if (dstOffset > entry.offset) {//向后挪,后面的先挪
            if (next != null) {
                moveTo(next, dstOffset - entry.offset + next.offset);
            }
            content.transfer(entry.offset, entry.size(), dstOffset);
        } else {//向前挪,前面的先挪
            content.transfer(entry.offset, entry.size(), dstOffset);
            if (next != null) {
                moveTo(next, dstOffset - entry.offset + next.offset);
            }
        }
    }


    /**
     * 将长度为 slen 的字符串 s 推入到 zl 中。
     * where 参数的值决定了推入的方向：
     * - 值为 ZIPLIST_HEAD 时，将新值推入到表头。
     * - 否则，将新值推入到表末端。
     * 函数的返回值为添加新值后的 ziplist 。
     * T = O(N^2)
     * @param s         待插入字符串
     * @param direction true:left插入 false:right插入
     * @return 返回插入位置
     */
    public int zipListPush(byte[] s, boolean direction) {
        int   offset = direction ? zlentryI : _zlend();
        Entry entry  = zipListInsert(offset, s);
        return entry.offset;
    }

    /**
     * 返回第index个entry
     * O(N)
     * @param index entry序号
     * @return
     */
    public Entry zipListIndex(int index) {
        //TODO 超出队列长度判断
        if (index < 0) {//从队尾向队头遍历
            //队尾无entry,队列为空
            Entry result = _zltail();
            if (result == null) {
                throw new IllegalArgumentException(String.format("index:%d超出范围,数组长度:%d", index, _zllen()));
            }
            for (; index < 0; index++) {
                //找到前置节点
                result = result._preEntry(this);
                if (result == null) {
                    throw new IllegalArgumentException(String.format("index:%d超出范围,数组长度:%d", index, _zllen()));
                }
            }
            return result;
        } else {//从队头向队尾进行遍历
            Entry result = _entry(zlentryI);
            if (result == null) {
                throw new IllegalArgumentException(String.format("index:%d超出范围,数组长度:%d", index, _zllen()));
            }
            for (int i = 0; i < index; i++) {
                result = result._nextEntry(this);
                if (result == null) {
                    throw new IllegalArgumentException(String.format("index:%d超出范围,数组长度:%d", index, _zllen()));
                }
            }
            return result;
        }
    }

    /**
     * 返回 offset 所指向节点的后置节点。
     * 如果 offset 为表末端，或者 offset 已经是表尾节点，那么返回 NULL 。
     * T = O(1)
     * @param offset
     * @return
     */
    public Entry zipListNext(int offset) {
        Entry entry = _entry(offset);
        return entry == null ? null : entry._nextEntry(this);
    }

    /**
     * 返回 offset 所指向节点的前置节点。
     * 如果 offset 所指向为空列表，或者 offset 已经指向表头节点，那么返回 NULL 。
     * @param offset
     * @return
     */
    public Entry zipListPre(int offset) {
        Entry entry = _entry(offset);
        return entry == null ? null : entry._preEntry(this);
    }

    /**
     * 取出 offset 所指向节点的值：
     * - 如果节点保存的是字符串，那么将字符串值指针保存到 *sstr 中，字符串长度保存到 *slen
     * - 如果节点保存的是整数，那么将整数保存到 *sval
     * 程序可以通过检查 *sstr 是否为 NULL 来检查值是字符串还是整数。
     * 提取值成功返回 1 ，
     * 如果 offset 为空，或者 offset 指向的是列表末端，那么返回 0 ，提取值失败。
     * T = O(1)
     * @param offset
     * @return
     */
    public Entry zipListGet(int offset) {
        return _entry(offset);
    }


    /**
     * Insert an entry at "offset".
     * 将包含给定值 s 的新节点插入到给定的位置 offset 中。
     * 如果 offset 指向一个节点，那么新节点将放在原有节点的前面。
     * T = O(N^2)
     * @param offset
     * @param content
     * @return 插入位置
     */
    public Entry zipListInsert(int offset, byte[] content) {
        //解析当前offset节点
        Entry entry    = _entry(offset);
        Entry newEntry = null;
        if (entry != null) {
            //当前offset节点不为空
            newEntry = Entry.init(this, offset, content, entry._preEntryLengthI(), entry);
        } else {
            //当前offset节点为空
            Entry tailEntry = _zltail();
            newEntry = Entry.init(this, offset, content, tailEntry != null ? tailEntry.size() : 0, null);
        }
        //ziplist信息更新
        _zlbytes(_zlbytes() + newEntry.size());
        _zllen(_zllen() + 1);
        if (entry == null) {//当前offset为空说明在队尾,需要更新zltail
            _zltail(newEntry.offset);
        }
        //更新后置节点
        if (entry != null) {
            entry.updatePreEntryLength(newEntry.size());
        }
        return newEntry;
    }

    /**
     * 从 zl 中删除 *offset 所指向的节点，
     * 并且原地更新 *offset 所指向的位置，使得可以在迭代列表的过程中对节点进行删除。
     * T = O(N^2)
     * @param entry
     * @return
     */
    public int ziplistDelete(Entry entry) {
        //由于entry.content是对ziplist.content的切片.
        //当后面进行moveTO操作后,当前entry.content已经被覆盖,因此先读取信息后后面使用
        int offset         = entry.offset;
        int preEntryLength = entry._preEntryLengthI();
        int size           = entry.size();
        //获取后置节点
        Entry next = entry._nextEntry(this);
        if (next != null) {
            moveTo(next, offset);
        }
        //moveTo完成后entry.content此时已经被覆盖,不能再进行读取

        //进行resize缩容
        _resize(_zlbytes() - size);
        //更新ziplist表头信息
        _zlbytes(_zlbytes() - size);
        if (next == null) {
            _zltail(offset - preEntryLength);
        }
        _zllen(_zllen() - 1);

        return 1;
    }

    /**
     * 从 index 索引指定的节点开始，连续地从 zl 中删除 num 个节点。
     * T = O(N^2)
     * @param index
     * @param num
     * @return
     */
    public int ziplistDeleteRange(int index, int num) {
        Entry entry = zipListIndex(index);
        for (int i = 0; i < num; i++) {
            ziplistDelete(entry);
            entry = _entry(entry.offset);

        }
        return num;
    }

    /**
     * 将 offset 所指向的节点的值和 sstr 进行对比。
     * 如果节点值和 sstr 的值相等，返回 1 ，不相等则返回 0 。
     * T = O(N)
     * @return
     */
    public int ziplistCompare(int offset, char[] sstr, int length) {
        return 1;
    }

    /**
     * 寻找节点值和 vstr 相等的列表节点，并返回该节点的指针。
     * Skip 'skip' entries between every comparison.
     * 每次比对之前都跳过 skip 个节点。
     * Returns NULL when the field could not be found.
     * 如果找不到相应的节点，则返回 NULL 。
     * T = O(N^2)
     * @param offset
     * @return
     */
    public Entry ziplistFind(int offset, char[] vstr, int vlen, int skip) {
        return null;
    }

    /**
     * 返回 ziplist 中的节点个数
     * T = O(N)
     * @return
     */
    public int ziplistLen() {
        return _zllen();
    }

    /**
     * 返回整个 ziplist 占用的内存字节数
     * T = O(1)
     * @return
     */
    public int ziplistBlobLen() {
        return _zlbytes();
    }
}
