package object;

import object.intf.List;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ZipListListTest {

    @Test
    public void test() {
        List list = new ZipListList();
        assertEquals(list.llen(), 0);

        list.lpush("lpush1".getBytes());
        assertEquals(list.llen(), 1);

        list.rpush("rpush1".getBytes());
        assertEquals(list.llen(), 2);

        list.lpush("lpush2".getBytes());
        assertEquals(list.llen(), 3);

        list.rpush("rpush2".getBytes());
        assertEquals(list.llen(), 4);

        assertEquals(new String(list.lpop()), "lpush2");
        assertEquals(new String(list.rpop()), "rpush2");
        assertEquals(new String(list.lpop()), "lpush1");
        assertEquals(new String(list.rpop()), "rpush1");


        list.lpush("lpush1".getBytes());
        list.lpush("lpush2".getBytes());
        list.rpush("rpush1".getBytes());
        list.rpush("rpush2".getBytes());
        int lrem = list.lrem(1);
        assertEquals(lrem, 1);

        int linsert = list.linsert(1, "lpush3".getBytes());
        assertEquals(linsert, 1);

        byte[] lindex = list.lindex(1);
        assertEquals(new String(lindex), "lpush3");

        linsert = list.linsert(0, "lpush4".getBytes());
        assertEquals(linsert, 1);

        lindex = list.lindex(0);
        assertEquals(new String(lindex), "lpush4");

        int lset = list.lset(0, "lpush0".getBytes());
        assertEquals(lset, 1);
        assertEquals(new String(list.lindex(0)), "lpush0");

        int ltrim = list.ltrim(0, 4);
        assertEquals(ltrim, 4);

        assertEquals(list.llen(), 1);
    }

}