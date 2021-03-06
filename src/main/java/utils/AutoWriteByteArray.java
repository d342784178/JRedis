package utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static io.netty.util.internal.ObjectUtil.checkPositiveOrZero;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-03-06
 * Time: 17:15
 */
public class AutoWriteByteArray extends ByteArray {
    private FileChannel channel;
    private ByteBuffer  byteBuffer;

    private int writerIndex;

    public AutoWriteByteArray(FileChannel channel, int bufferSize) {
        super(new byte[bufferSize]);
        this.channel = channel;
        this.byteBuffer = ByteBuffer.allocate(bufferSize);
    }


    public AutoWriteByteArray writeBoolean(boolean value) {
        writeByte(value ? 1 : 0);
        return this;
    }


    public AutoWriteByteArray writeByte(int value) {
        ensureWritable0(1);
        setByte(writerIndex++, value);
        return this;
    }


    public AutoWriteByteArray writeShort(int value) {
        ensureWritable0(2);
        setShort(writerIndex, value);
        writerIndex += 2;
        return this;
    }


    public AutoWriteByteArray writeShortLE(int value) {
        ensureWritable0(2);
        setShortLE(writerIndex, value);
        writerIndex += 2;
        return this;
    }


    public AutoWriteByteArray writeInt(int value) {
        ensureWritable0(4);
        setInt(writerIndex, value);
        writerIndex += 4;
        return this;
    }


    public AutoWriteByteArray writeIntLE(int value) {
        ensureWritable0(4);
        setIntLE(writerIndex, value);
        writerIndex += 4;
        return this;
    }


    public AutoWriteByteArray writeLong(long value) {
        ensureWritable0(8);
        setLong(writerIndex, value);
        writerIndex += 8;
        return this;
    }


    public AutoWriteByteArray writeLongLE(long value) {
        ensureWritable0(8);
        setLongLE(writerIndex, value);
        writerIndex += 8;
        return this;
    }


    public AutoWriteByteArray writeChar(int value) {
        writeShort(value);
        return this;
    }


    public AutoWriteByteArray writeFloat(float value) {
        writeInt(Float.floatToRawIntBits(value));
        return this;
    }


    public AutoWriteByteArray writeDouble(double value) {
        writeLong(Double.doubleToRawLongBits(value));
        return this;
    }


    public AutoWriteByteArray writeBytes(byte[] src, int srcIndex, int length) {
        ensureWritable(length);
        setBytes(writerIndex, src, srcIndex, length);
        writerIndex += length;
        return this;
    }


    public AutoWriteByteArray writeBytes(byte[] src) {
        writeBytes(src, 0, src.length);
        return this;
    }


    public AutoWriteByteArray writeBytes(AutoWriteByteArray src, int srcIndex, int length) {
        ensureWritable(length);
        setBytes(writerIndex, src, srcIndex, length);
        writerIndex += length;
        return this;
    }


    public AutoWriteByteArray writeZero(int length) {
        if (length == 0) {
            return this;
        }

        ensureWritable(length);
        int wIndex = writerIndex;

        int nLong  = length >>> 3;
        int nBytes = length & 7;
        for (int i = nLong; i > 0; i--) {
            setLong(wIndex, 0);
            wIndex += 8;
        }
        if (nBytes == 4) {
            setInt(wIndex, 0);
            wIndex += 4;
        } else if (nBytes < 4) {
            for (int i = nBytes; i > 0; i--) {
                setByte(wIndex, (byte) 0);
                wIndex++;
            }
        } else {
            setInt(wIndex, 0);
            wIndex += 4;
            for (int i = nBytes - 4; i > 0; i--) {
                setByte(wIndex, (byte) 0);
                wIndex++;
            }
        }
        writerIndex = wIndex;
        return this;
    }

    public AutoWriteByteArray ensureWritable(int minWritableBytes) {
        checkPositiveOrZero(minWritableBytes, "minWritableBytes");
        ensureWritable0(minWritableBytes);
        return this;
    }


    final void ensureWritable0(int minWritableBytes) {
        if (minWritableBytes > length - writerIndex) {
            flush();
        }
    }

    public void flush() {
        try {
            //自动写入后续内容
            byteBuffer.clear();
            byteBuffer.put(array, 0, writerIndex);
            byteBuffer.flip();
            int write = 0;
            while (write < writerIndex) {
                write += channel.write(byteBuffer);
            }
            this.writerIndex = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
