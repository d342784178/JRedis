package utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-03-06
 * Time: 16:12
 */
public class AutoReadByteArray extends ByteArray {

    private FileChannel channel;
    private ByteBuffer  byteBuffer;

    public AutoReadByteArray(FileChannel channel, ByteBuffer byteBuffer) {
        super(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit() == byteBuffer.capacity() ?
                byteBuffer.position() : byteBuffer
                .limit());
        this.channel = channel;
        this.byteBuffer = byteBuffer;
    }

    @Override
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
                    this.startI = 0;
                    this.length = newArray.length;
                    this.readerIndex = 0;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
