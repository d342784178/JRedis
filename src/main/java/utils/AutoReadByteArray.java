package utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-03-06
 * Time: 16:12
 */
public class AutoReadByteArray extends ByteArray {

    private FileChannel channel;
    private ByteBuffer  byteBuffer;

    protected int readerIndex;


    public AutoReadByteArray(FileChannel channel, int bufferSize) {
        super(new byte[0]);
        this.channel = channel;
        this.byteBuffer = ByteBuffer.allocate(512);
        this.readerIndex = 0;
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
            try {
                byteBuffer.clear();
                //自动读取后续内容
                int read = channel.read(byteBuffer);
                if (read == 0) {
                    //读不出内容,说明已到末尾
                    throw new IndexOutOfBoundsException(String.format(
                            "readerIndex(%d) + length(%d) exceeds Limit(%d)",
                            readerIndex, minimumReadableBytes, startI + length));
                } else {
                    //读到内容,则重置当前数组
                    int    readLength   = byteBuffer.position();
                    int    oldArrayLeft = length - (readerIndex - startI);
                    byte[] newArray     = new byte[readLength + oldArrayLeft];
                    System.arraycopy(array, readerIndex, newArray, 0, oldArrayLeft);
                    System.arraycopy(byteBuffer.array(), 0, newArray,
                            oldArrayLeft, readLength);
                    this.array = newArray;
                    this.length = newArray.length;
                    this.readerIndex = 0;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String toReadableString(Charset charset) {
        return toString(readerIndex - startI, length - (readerIndex - startI), charset);

    }
}
