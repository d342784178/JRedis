package utils;


import java.nio.charset.Charset;
import java.util.Arrays;


public class ByteArray {
    protected int length;
    protected int startI;


    protected byte[] array;


    public ByteArray(int length) {
        this.length = length;
        this.array = new byte[length];
        this.startI = 0;
    }

    public ByteArray(byte[] array) {
        this.array = array;
        this.length = array.length;
        this.startI = 0;
    }

    public ByteArray(byte[] array, int length) {
        this.array = array;
        this.startI = 0;
        this.length = length;
    }

    public ByteArray(int startI, byte[] array) {
        this.startI = startI;
        this.array = array;
        this.length = array.length - startI;
    }

    public ByteArray(byte[] array, int startI, int length) {
        this.array = array;
        this.startI = startI;
        this.length = length;
    }


    public int length() {
        return length;
    }

    public void newCapacity(int newCapacity) {
        //申请新数组
        byte[] newArray = new byte[newCapacity];
        //进行内容拷贝,区分扩容/缩容
        System.arraycopy(array, startI, newArray, 0, newCapacity > length ? length : newCapacity);
        //更新bytearray参数
        array = newArray;
        length = newCapacity;
        startI = 0;
    }


    public boolean getBoolean(int index) {
        return ByteUtil.getByte(array, indexOffset(index)) != 0;
    }


    public byte getByte(int index) {
        return ByteUtil.getByte(array, indexOffset(index));
    }


    public short getUnsignedByte(int index) {
        return ByteUtil.getByte(array, indexOffset(index));
    }


    public short getShort(int index) {
        return ByteUtil.getShort(array, indexOffset(index));
    }


    public short getShortLE(int index) {
        return ByteUtil.getShortLE(array, indexOffset(index));
    }


    public int getUnsignedShort(int index) {
        return ByteUtil.getShort(array, indexOffset(index));
    }


    public int getUnsignedShortLE(int index) {
        return ByteUtil.getShortLE(array, indexOffset(index));
    }


    public int getInt(int index) {
        return ByteUtil.getInt(array, indexOffset(index));
    }


    public int getIntLE(int index) {
        return ByteUtil.getIntLE(array, indexOffset(index));
    }


    public long getUnsignedInt(int index) {
        return ByteUtil.getInt(array, indexOffset(index));
    }


    public long getUnsignedIntLE(int index) {
        return ByteUtil.getIntLE(array, indexOffset(index));
    }


    public long getLong(int index) {
        return ByteUtil.getLong(array, indexOffset(index));
    }


    public long getLongLE(int index) {
        return ByteUtil.getLongLE(array, indexOffset(index));
    }


    public char getChar(int index) {
        return (char) ByteUtil.getShort(array, indexOffset(index));
    }


    public float getFloat(int index) {
        return Float.intBitsToFloat(getInt(index));
    }


    public float getFloatLE(int index) {
        return Float.intBitsToFloat(getIntLE(index));
    }


    public double getDouble(int index) {
        return Double.longBitsToDouble(getLong(index));
    }


    public double getDoubleLE(int index) {
        return Double.longBitsToDouble(getLongLE(index));
    }


    public ByteArray setBoolean(int index, boolean value) {
        ByteUtil.setByte(array, indexOffset(index), value ? 1 : 0);
        return this;
    }


    public ByteArray setByte(int index, int value) {
        ByteUtil.setByte(array, indexOffset(index), value);
        return this;
    }


    public ByteArray setShort(int index, int value) {
        ByteUtil.setShort(array, indexOffset(index), value);
        return this;
    }


    public ByteArray setShortLE(int index, int value) {
        ByteUtil.setShortLE(array, indexOffset(index), value);
        return this;
    }


    public ByteArray setInt(int index, int value) {
        ByteUtil.setInt(array, indexOffset(index), value);
        return this;
    }


    public ByteArray setIntLE(int index, int value) {
        ByteUtil.setIntLE(array, indexOffset(index), value);
        return this;
    }


    public ByteArray setLong(int index, long value) {
        ByteUtil.setLong(array, indexOffset(index), value);
        return this;
    }


    public ByteArray setLongLE(int index, long value) {
        ByteUtil.setLongLE(array, indexOffset(index), value);
        return this;
    }


    public ByteArray setChar(int index, int value) {
        ByteUtil.setShort(array, indexOffset(index), value);
        return this;
    }


    public ByteArray setFloat(int index, float value) {
        ByteUtil.setInt(array, indexOffset(index), Float.floatToRawIntBits(value));
        return this;
    }


    public ByteArray setFloatLE(int index, float value) {
        return setIntLE(index, Float.floatToRawIntBits(value));
    }


    public ByteArray setDouble(int index, double value) {
        ByteUtil.setLong(array, indexOffset(index), Double.doubleToRawLongBits(value));
        return this;
    }


    public ByteArray setDoubleLE(int index, double value) {
        return setLongLE(index, Double.doubleToRawLongBits(value));
    }


    public ByteArray setBytes(int index, byte[] src, int srcIndex, int length) {
        System.arraycopy(src, srcIndex, array, indexOffset(index), length);
        return this;
    }


    public ByteArray setBytes(int index, ByteArray src, int srcIndex, int length) {
        System.arraycopy(src.array, src.indexOffset(srcIndex), array, indexOffset(index), length);
        return this;
    }


    protected int indexOffset(int index) {
        int i = startI + index;
        return i;
    }


    public ByteArray copy(int index, int length) {
        byte[] dst = new byte[length];
        System.arraycopy(array, indexOffset(index), dst, 0, length);
        return new ByteArray(dst);
    }

    public ByteArray slice(int index, int length) {
        if (index + length > array.length) {
            throw new IllegalArgumentException(String.format("index+length>array.length:%d", array.length));
        }
        return new ByteArray(array, indexOffset(index), length);
    }

    public void transfer(int index, int len, int dstIndex) {
        System.arraycopy(array, indexOffset(index), array, indexOffset(dstIndex), len);
        Arrays.fill(array, indexOffset(index), indexOffset(index + len), (byte) 0);
    }


    public byte[] array() {
        byte[] dst = new byte[length];
        System.arraycopy(array, indexOffset(0), dst, 0, length);
        return dst;
    }


    @Override
    public int hashCode() {
        return ByteUtil.hashCode(this);
    }


    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof ByteArray && ByteUtil.equals(this, (ByteArray) o));
    }

    @Override
    public String toString() {
        return Arrays.toString(array());
    }

    public String toString(Charset charset) {
        return toString(0, charset);
    }

    public String toString(int index, Charset charset) {
        return toString(index, length, charset);
    }

    public String toString(int index, int length, Charset charset) {
        return ByteUtil.decodeString(array, indexOffset(index), length, charset);
    }


    public static void main(String args[]) {
        ByteArray byteArray = new ByteArray(5);
        byteArray.setByte(0, 0);
        byteArray.setByte(1, 1);
        byteArray.setByte(2, 2);
        byteArray.setByte(3, 3);
        byteArray.setByte(4, 4);

        System.out.println(byteArray.toString());

        ByteArray slice = byteArray.slice(1, 3);
        System.out.println(slice);

        slice = slice.slice(1, 2);
        System.out.println(slice);
    }
}
