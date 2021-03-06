package serialize;

import com.google.common.collect.Maps;
import container.DataBase;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import operating.ZipListList;
import operating.intf.IRedisObject;
import org.testng.Assert;
import org.testng.collections.Lists;
import utils.AutoReadByteArray;
import utils.ByteArray;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static serialize.RdbEnum.*;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-02-28
 * Time: 14:22
 */
public class RdbHelper {

    private final DataBase  db;
    private final RdbSaver  rdbSaver;
    private final RdbLoader rdbLoader;

    public RdbHelper(DataBase db) {
        this.db = db;
        rdbSaver = new RdbSaver();
        rdbLoader = new RdbLoader();
    }


    public void save() {
        rdbSaver.save(db.redisObjects());
    }

    public void load() {
        rdbLoader.load();
    }

    private static class RdbSaver {

        public void save(List<Map.Entry<String, IRedisObject>> entries) {
            Path filePath = Paths.get("rdb");
            File file     = filePath.toFile();
            if (file.exists()) {
                file.delete();
            }
            try (FileChannel rdb = FileChannel.open(filePath,
                    new StandardOpenOption[]{StandardOpenOption.CREATE_NEW,
                            StandardOpenOption.APPEND})) {
                System.out.println("RDB SAVE START");
                ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(1024);
                buffer.writeBytes("REDIS".getBytes(StandardCharsets.UTF_8));
                buffer.writeInt(1);
                {//DB部分
                    buffer.writeByte(REDIS_SELECTDB.code());
                    buffer.writeByte(0);
                    for (Map.Entry<String, IRedisObject> entry : entries) {
                        //REDIS_EXPIRETIME_MS:expire:type:len:key
                        String       key        = entry.getKey();
                        IRedisObject value      = entry.getValue();
                        long         expireTime = value.expire();
                        if (expireTime > 0) {
                            buffer.writeByte(REDIS_EXPIRETIME_MS.code());
                            buffer.writeLong(expireTime);
                        }
                        buffer.writeByte(value.type().code());
                        buffer.writeInt(key.length());
                        buffer.writeBytes(key.getBytes(StandardCharsets.UTF_8));

                        byte[] array = ((ZipListList) value.origin()).getZipList().getContent().array();
                        buffer.writeInt(array.length);
                        buffer.writeBytes(array);


                        //写缓冲区超过一半,先写到文件中,防止缓冲区爆
                        if (buffer.readableBytes() > 500) {
                            ByteBuffer byteBuffer = buffer.nioBuffer();
                            while (byteBuffer.hasRemaining()) {
                                rdb.write(byteBuffer);
                            }
                            buffer.readerIndex(buffer.writerIndex());
                            buffer.discardReadBytes();
                        }
                    }
                }
                buffer.writeByte(REDIS_EOF.code());
                while (buffer.readableBytes() > 0) {
                    ByteBuffer byteBuffer = buffer.nioBuffer();
                    while (byteBuffer.hasRemaining()) {
                        rdb.write(byteBuffer);
                    }
                    buffer.readerIndex(buffer.writerIndex());
                    buffer.discardReadBytes();
                }
                System.out.println("RDB SAVE END");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("RDB SAVE FAILED");
            }
        }
    }

    private static class RdbLoader {
        Map<String, IRedisObject> load() {
            HashMap<String, IRedisObject> map = Maps.newHashMap();
            try (FileChannel rdb = FileChannel.open(Paths.get("rdb"), StandardOpenOption.READ)) {
                System.out.println("RDB LOAD START");
                AutoReadByteArray byteArray = new AutoReadByteArray(rdb, ByteBuffer.allocate(512));
                //手动读取文件头
                String redisTag = byteArray.readBytes(5).toString(StandardCharsets.UTF_8);
                Assert.assertEquals(redisTag, "REDIS");
                int dbVersion = byteArray.readInt();


                RdbEnum rdbEnum;
                long    expireTime = -1;
                boolean end        = false;
                while (!end && (rdbEnum = RdbEnum.codeOf(byteArray.readByte())) != null) {
                    switch (rdbEnum) {
                        case REDIS_SELECTDB:
                            int dbNum = byteArray.readByte();
                            break;
                        case REDIS_EXPIRETIME_MS:
                            expireTime = byteArray.readLong();
                            break;
                        case REDIS_LIST_ZIPLIST:
                            int keyLength = byteArray.readInt();
                            String key = byteArray.readBytes(keyLength).toString(StandardCharsets.UTF_8);
                            int valueLength = byteArray.readInt();
                            ByteArray valueByteArray = byteArray.readBytes(valueLength);
                            ZipListList value = new ZipListList(valueByteArray);
                            if (expireTime < 0 || System.currentTimeMillis() < expireTime) {
                                value.expire(expireTime);
                                map.put(key, value);
                                expireTime = -1;
                            }
                            break;
                        case REDIS_EOF:
                            end = true;
                            break;
                        default:
                            break;
                    }
                }
                System.out.println("RDB LOAD END");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("RBD LOAD FAILED...");
            }
            return map;
        }
    }

    public static void main(String args[]) throws Exception {
        List<Map.Entry<String, IRedisObject>> entries = Lists.newArrayList();
        int                                   num     = 100;
        for (int i = 0; i < num; i++) {
            ZipListList zipListList = new ZipListList();
            zipListList.lpush("2".getBytes(StandardCharsets.UTF_8));
            zipListList.lpush("3".getBytes(StandardCharsets.UTF_8));
            zipListList.expire(System.currentTimeMillis() + 1000 * 100);
            entries.add(new AbstractMap.SimpleEntry<>(i + "", zipListList));
        }
        new RdbSaver().save(entries);

        Map<String, IRedisObject> load = new RdbLoader().load();
        Assert.assertEquals(load.size(), num);
    }

}
