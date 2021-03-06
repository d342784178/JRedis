package utils;


import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2020-12-13
 * Time: 12:20
 */
public class ByteArray {
    protected int length;
    protected int startI;
    protected int readerIndex;

    protected byte[] array;


    public ByteArray(int length) {
        this.length = length;
        this.array = new byte[length];
        this.startI = 0;

        this.readerIndex = this.startI;
    }

    public ByteArray(byte[] array) {
        this.array = array;
        this.length = array.length;
        this.startI = 0;

        this.readerIndex = this.startI;
    }

    public ByteArray(byte[] array, int length) {
        this.array = array;
        this.startI = 0;
        this.length = length;

        this.readerIndex = this.startI;
    }

    public ByteArray(int startI, byte[] array) {
        this.startI = startI;
        this.array = array;
        this.length = array.length - startI;

        this.readerIndex = this.startI;
    }

    public ByteArray(byte[] array, int startI, int length) {
        this.array = array;
        this.startI = startI;
        this.length = length;

        this.readerIndex = this.startI;
    }


    /**
     * Returns the number of bytes (octets) this buffer can contain.
     */
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


    /**
     * Gets a boolean at the specified absolute (@code index) in this buffer.
     * This method does not modify the {@code readerIndex} or {@code writerIndex}
     * of this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                      {@code index + 1} is greater than {@code this.capacity}
     */
    public boolean getBoolean(int index) {
        return ByteUtil.getByte(array, indexOffset(index)) != 0;
    }

    /**
     * Gets a byte at the specified absolute {@code index} in this buffer.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                      {@code index + 1} is greater than {@code this.capacity}
     */
    public byte getByte(int index) {
        return ByteUtil.getByte(array, indexOffset(index));
    }

    /**
     * Gets an unsigned byte at the specified absolute {@code index} in this
     * buffer.  This method does not modify {@code readerIndex} or
     * {@code writerIndex} of this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                      {@code index + 1} is greater than {@code this.capacity}
     */
    public short getUnsignedByte(int index) {
        return ByteUtil.getByte(array, indexOffset(index));
    }

    /**
     * Gets a 16-bit short integer at the specified absolute {@code index} in
     * this buffer.  This method does not modify {@code readerIndex} or
     * {@code writerIndex} of this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                      {@code index + 2} is greater than {@code this.capacity}
     */
    public short getShort(int index) {
        return ByteUtil.getShort(array, indexOffset(index));
    }

    /**
     * Gets a 16-bit short integer at the specified absolute {@code index} in
     * this buffer in Little Endian Byte Order. This method does not modify
     * {@code readerIndex} or {@code writerIndex} of this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                      {@code index + 2} is greater than {@code this.capacity}
     */
    public short getShortLE(int index) {
        return ByteUtil.getShortLE(array, indexOffset(index));
    }

    /**
     * Gets an unsigned 16-bit short integer at the specified absolute
     * {@code index} in this buffer.  This method does not modify
     * {@code readerIndex} or {@code writerIndex} of this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                      {@code index + 2} is greater than {@code this.capacity}
     */
    public int getUnsignedShort(int index) {
        return ByteUtil.getShort(array, indexOffset(index));
    }

    /**
     * Gets an unsigned 16-bit short integer at the specified absolute
     * {@code index} in this buffer in Little Endian Byte Order.
     * This method does not modify {@code readerIndex} or
     * {@code writerIndex} of this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                      {@code index + 2} is greater than {@code this.capacity}
     */
    public int getUnsignedShortLE(int index) {
        return ByteUtil.getShortLE(array, indexOffset(index));
    }


    /**
     * Gets a 32-bit integer at the specified absolute {@code index} in
     * this buffer.  This method does not modify {@code readerIndex} or
     * {@code writerIndex} of this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                      {@code index + 4} is greater than {@code this.capacity}
     */
    public int getInt(int index) {
        return ByteUtil.getInt(array, indexOffset(index));
    }

    /**
     * Gets a 32-bit integer at the specified absolute {@code index} in
     * this buffer with Little Endian Byte Order. This method does not
     * modify {@code readerIndex} or {@code writerIndex} of this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                      {@code index + 4} is greater than {@code this.capacity}
     */
    public int getIntLE(int index) {
        return ByteUtil.getIntLE(array, indexOffset(index));
    }

    /**
     * Gets an unsigned 32-bit integer at the specified absolute {@code index}
     * in this buffer.  This method does not modify {@code readerIndex} or
     * {@code writerIndex} of this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                      {@code index + 4} is greater than {@code this.capacity}
     */
    public long getUnsignedInt(int index) {
        return ByteUtil.getInt(array, indexOffset(index));
    }

    /**
     * Gets an unsigned 32-bit integer at the specified absolute {@code index}
     * in this buffer in Little Endian Byte Order. This method does not
     * modify {@code readerIndex} or {@code writerIndex} of this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                      {@code index + 4} is greater than {@code this.capacity}
     */
    public long getUnsignedIntLE(int index) {
        return ByteUtil.getIntLE(array, indexOffset(index));
    }

    /**
     * Gets a 64-bit long integer at the specified absolute {@code index} in
     * this buffer.  This method does not modify {@code readerIndex} or
     * {@code writerIndex} of this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                      {@code index + 8} is greater than {@code this.capacity}
     */
    public long getLong(int index) {
        return ByteUtil.getLong(array, indexOffset(index));
    }

    /**
     * Gets a 64-bit long integer at the specified absolute {@code index} in
     * this buffer in Little Endian Byte Order. This method does not
     * modify {@code readerIndex} or {@code writerIndex} of this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                      {@code index + 8} is greater than {@code this.capacity}
     */
    public long getLongLE(int index) {
        return ByteUtil.getLongLE(array, indexOffset(index));
    }

    /**
     * Gets a 2-byte UTF-16 character at the specified absolute
     * {@code index} in this buffer.  This method does not modify
     * {@code readerIndex} or {@code writerIndex} of this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                      {@code index + 2} is greater than {@code this.capacity}
     */
    public char getChar(int index) {
        return (char) ByteUtil.getShort(array, indexOffset(index));
    }

    /**
     * Gets a 32-bit floating point number at the specified absolute
     * {@code index} in this buffer.  This method does not modify
     * {@code readerIndex} or {@code writerIndex} of this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                      {@code index + 4} is greater than {@code this.capacity}
     */
    public float getFloat(int index) {
        return Float.intBitsToFloat(getInt(index));
    }

    /**
     * Gets a 32-bit floating point number at the specified absolute
     * {@code index} in this buffer in Little Endian Byte Order.
     * This method does not modify {@code readerIndex} or
     * {@code writerIndex} of this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                      {@code index + 4} is greater than {@code this.capacity}
     */
    public float getFloatLE(int index) {
        return Float.intBitsToFloat(getIntLE(index));
    }

    /**
     * Gets a 64-bit floating point number at the specified absolute
     * {@code index} in this buffer.  This method does not modify
     * {@code readerIndex} or {@code writerIndex} of this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                      {@code index + 8} is greater than {@code this.capacity}
     */
    public double getDouble(int index) {
        return Double.longBitsToDouble(getLong(index));
    }

    /**
     * Gets a 64-bit floating point number at the specified absolute
     * {@code index} in this buffer in Little Endian Byte Order.
     * This method does not modify {@code readerIndex} or
     * {@code writerIndex} of this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                      {@code index + 8} is greater than {@code this.capacity}
     */
    public double getDoubleLE(int index) {
        return Double.longBitsToDouble(getLongLE(index));
    }


    /**
     * Sets the specified boolean at the specified absolute {@code index} in this
     * buffer.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                      {@code index + 1} is greater than {@code this.capacity}
     */
    public ByteArray setBoolean(int index, boolean value) {
        ByteUtil.setByte(array, indexOffset(index), value ? 1 : 0);
        return this;
    }

    /**
     * Sets the specified byte at the specified absolute {@code index} in this
     * buffer.  The 24 high-order bits of the specified value are ignored.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                      {@code index + 1} is greater than {@code this.capacity}
     */
    public ByteArray setByte(int index, int value) {
        ByteUtil.setByte(array, indexOffset(index), value);
        return this;
    }

    /**
     * Sets the specified 16-bit short integer at the specified absolute
     * {@code index} in this buffer.  The 16 high-order bits of the specified
     * value are ignored.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                      {@code index + 2} is greater than {@code this.capacity}
     */
    public ByteArray setShort(int index, int value) {
        ByteUtil.setShort(array, indexOffset(index), value);
        return this;
    }

    /**
     * Sets the specified 16-bit short integer at the specified absolute
     * {@code index} in this buffer with the Little Endian Byte Order.
     * The 16 high-order bits of the specified value are ignored.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                      {@code index + 2} is greater than {@code this.capacity}
     */
    public ByteArray setShortLE(int index, int value) {
        ByteUtil.setShortLE(array, indexOffset(index), value);
        return this;
    }


    /**
     * Sets the specified 32-bit integer at the specified absolute
     * {@code index} in this buffer.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                      {@code index + 4} is greater than {@code this.capacity}
     */
    public ByteArray setInt(int index, int value) {
        ByteUtil.setInt(array, indexOffset(index), value);
        return this;
    }

    /**
     * Sets the specified 32-bit integer at the specified absolute
     * {@code index} in this buffer with Little Endian byte order
     * .
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                      {@code index + 4} is greater than {@code this.capacity}
     */
    public ByteArray setIntLE(int index, int value) {
        ByteUtil.setIntLE(array, indexOffset(index), value);
        return this;
    }

    /**
     * Sets the specified 64-bit long integer at the specified absolute
     * {@code index} in this buffer.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                      {@code index + 8} is greater than {@code this.capacity}
     */
    public ByteArray setLong(int index, long value) {
        ByteUtil.setLong(array, indexOffset(index), value);
        return this;
    }

    /**
     * Sets the specified 64-bit long integer at the specified absolute
     * {@code index} in this buffer in Little Endian Byte Order.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                      {@code index + 8} is greater than {@code this.capacity}
     */
    public ByteArray setLongLE(int index, long value) {
        ByteUtil.setLongLE(array, indexOffset(index), value);
        return this;
    }

    /**
     * Sets the specified 2-byte UTF-16 character at the specified absolute
     * {@code index} in this buffer.
     * The 16 high-order bits of the specified value are ignored.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                      {@code index + 2} is greater than {@code this.capacity}
     */
    public ByteArray setChar(int index, int value) {
        ByteUtil.setShort(array, indexOffset(index), value);
        return this;
    }

    /**
     * Sets the specified 32-bit floating-point number at the specified
     * absolute {@code index} in this buffer.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                      {@code index + 4} is greater than {@code this.capacity}
     */
    public ByteArray setFloat(int index, float value) {
        ByteUtil.setInt(array, indexOffset(index), Float.floatToRawIntBits(value));
        return this;
    }

    /**
     * Sets the specified 32-bit floating-point number at the specified
     * absolute {@code index} in this buffer in Little Endian Byte Order.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                      {@code index + 4} is greater than {@code this.capacity}
     */
    public ByteArray setFloatLE(int index, float value) {
        return setIntLE(index, Float.floatToRawIntBits(value));
    }

    /**
     * Sets the specified 64-bit floating-point number at the specified
     * absolute {@code index} in this buffer.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                      {@code index + 8} is greater than {@code this.capacity}
     */
    public ByteArray setDouble(int index, double value) {
        ByteUtil.setLong(array, indexOffset(index), Double.doubleToRawLongBits(value));
        return this;
    }

    /**
     * Sets the specified 64-bit floating-point number at the specified
     * absolute {@code index} in this buffer in Little Endian Byte Order.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or
     *                                      {@code index + 8} is greater than {@code this.capacity}
     */
    public ByteArray setDoubleLE(int index, double value) {
        return setLongLE(index, Double.doubleToRawLongBits(value));
    }


    /**
     * Transfers the specified source array's data to this buffer starting at
     * the specified absolute {@code index}.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0},
     *                                      if the specified {@code srcIndex} is less than {@code 0},
     *                                      if {@code index + length} is greater than
     *                                      {@code this.capacity}, or
     *                                      if {@code srcIndex + length} is greater than {@code src.length}
     */
    public ByteArray setBytes(int index, byte[] src, int srcIndex, int length) {
        System.arraycopy(src, srcIndex, array, indexOffset(index), length);
        return this;
    }

    /**
     * Transfers the specified source array's data to this buffer starting at
     * the specified absolute {@code index}.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     * @exception IndexOutOfBoundsException if the specified {@code index} is less than {@code 0},
     *                                      if the specified {@code srcIndex} is less than {@code 0},
     *                                      if {@code index + length} is greater than
     *                                      {@code this.capacity}, or
     *                                      if {@code srcIndex + length} is greater than {@code src.length}
     */
    public ByteArray setBytes(int index, ByteArray src, int srcIndex, int length) {
        System.arraycopy(src.array, src.indexOffset(srcIndex), array, indexOffset(index), length);
        return this;
    }

    public ByteArray readBytes(int length) {
        checkReadableBytes0(length);
        ByteArray slice = slice(readerIndex - startI, length);
        readerIndex += length;
        return slice;
    }

    public void readBytes(byte[] bytes) {
        checkReadableBytes0(1);
        System.arraycopy(array, indexOffset(readerIndex), bytes, 0, bytes.length);
        readerIndex += bytes.length;
    }

    public byte readByte() {
        checkReadableBytes0(1);
        int  i = readerIndex;
        byte b = getByte(i);
        readerIndex = i + 1;
        return b;
    }


    public boolean readBoolean() {
        return readByte() != 0;
    }


    public short readUnsignedByte() {
        return (short) (readByte() & 0xFF);
    }


    public short readShort() {
        checkReadableBytes0(2);
        short v = getShort(readerIndex);
        readerIndex += 2;
        return v;
    }


    public short readShortLE() {
        checkReadableBytes0(2);
        short v = getShortLE(readerIndex);
        readerIndex += 2;
        return v;
    }


    public int readUnsignedShort() {
        return readShort() & 0xFFFF;
    }


    public int readUnsignedShortLE() {
        return readShortLE() & 0xFFFF;
    }

    public int readInt() {
        checkReadableBytes0(4);
        int v = getInt(readerIndex);
        readerIndex += 4;
        return v;
    }

    public int readIntLE() {
        checkReadableBytes0(4);
        int v = getIntLE(readerIndex);
        readerIndex += 4;
        return v;
    }

    public long readUnsignedInt() {
        return readInt() & 0xFFFFFFFFL;
    }

    public long readUnsignedIntLE() {
        return readIntLE() & 0xFFFFFFFFL;
    }

    public long readLong() {
        checkReadableBytes0(8);
        long v = getLong(readerIndex);
        readerIndex += 8;
        return v;
    }

    public long readLongLE() {
        checkReadableBytes0(8);
        long v = getLongLE(readerIndex);
        readerIndex += 8;
        return v;
    }

    public char readChar() {
        return (char) readShort();
    }

    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    protected void checkReadableBytes0(int minimumReadableBytes) {
        if (readerIndex + minimumReadableBytes > startI + length) {
            throw new IndexOutOfBoundsException(String.format(
                    "readerIndex(%d) + length(%d) exceeds Limit(%d)",
                    readerIndex, minimumReadableBytes, startI + length));
        }
    }

    /**
     * 根据startI计算实际偏移量
     * @param index
     * @return
     */
    private int indexOffset(int index) {
        int i = startI + index;
        return i;
    }

    /**
     * Returns a copy of this buffer's sub-region.  Modifying the content of
     * the returned buffer or this buffer does not affect each other at all.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     */
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

    /**
     * Returns the backing byte array of this buffer.
     * @exception UnsupportedOperationException if there no accessible backing byte array
     */
    public byte[] array() {
        byte[] dst = new byte[length];
        System.arraycopy(array, indexOffset(0), dst, 0, length);
        return dst;
    }

    /**
     * Returns a hash code which was calculated from the content of this
     * buffer.  If there's a byte array which is
     * {@linkplain #equals(Object) equal to} this array, both arrays should
     * return the same value.
     */
    @Override
    public int hashCode() {
        return ByteUtil.hashCode(this);
    }

    /**
     * Determines if the content of the specified buffer is identical to the
     * content of this array.  'Identical' here means:
     * <ul>
     * <li>the size of the contents of the two buffers are same and</li>
     * <li>every single byte of the content of the two buffers are same.</li>
     * </ul>
     * {@code null} and an object which is not an instance of
     * {@link ByteArray} type.
     */
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

    public String toReadableString(Charset charset) {
        return toString(readerIndex - startI, length - (readerIndex - startI), charset);

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
