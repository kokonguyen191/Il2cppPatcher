package doge.patcher;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import doge.data.Function;
import doge.data.Patch;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import org.junit.Test;

public class FilePatcherTest {

    @Test
    public void testWritePatch() throws IOException {
        File f = new File("tempEmptyTestFile");
        f.createNewFile();

        FileOutputStream fos = new FileOutputStream(f);
        byte[] bytes = {(byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15,
                (byte) 0x16, (byte) 0x17, (byte) 0x18};
        fos.write(bytes);
        fos.flush();

        byte[] changeBytes = {(byte) 0xf0, (byte) 0xf1, (byte) 0xf2};
        FilePatcher.writePatch(new RandomAccessFile(f, "rw"), 4, changeBytes);

        FileInputStream fis = new FileInputStream(f);
        fis.read(bytes);

        byte[] newBytes = {(byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0xf0,
                (byte) 0xf1, (byte) 0xf2, (byte) 0x18};

        fis.close();
        fos.close();
        f.delete();

        assertArrayEquals(newBytes, bytes);
    }

    @Test
    public void testMatchesOriginal() throws IOException {
        File f = new File("tempEmptyTestFile");
        f.createNewFile();

        FileOutputStream fos = new FileOutputStream(f);
        byte[] bytes = {(byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15,
                (byte) 0x16, (byte) 0x17, (byte) 0x18};
        fos.write(bytes);
        fos.flush();

        byte[] originalBytes = {(byte) 0x12, (byte) 0x13, (byte) 0x14};
        RandomAccessFile fh = new RandomAccessFile(f, "rw");

        assertTrue(FilePatcher.matchesOriginal(fh, 1, 3, originalBytes));
        assertFalse(FilePatcher.matchesOriginal(fh, 4, 3, originalBytes));

        fos.close();
        f.delete();
    }

    @Test
    public void testGetNewOffset() {
        HashMap<String, Function> hm = new HashMap<String, Function>();
        hm.put("int test1()", Function.parseFunction("int test(); // 0x00112233"));
        hm.put("long test2()", Function.parseFunction("long test2(); // 0x11111111"));

        Patch patch1 = Patch.parsePatch("00000012 0x1 00 AA",
                Function.parseFunction("int test1(); // 0x00000011"));
        Patch patch2 = Patch.parsePatch("0EDDDDFF 0x1 11 BB",
                Function.parseFunction("long test2(); // 0x0EDDDDDD"));

        assertEquals(0x00112234, FilePatcher.getNewOffset(hm, patch1));
        assertEquals(0x11111133, FilePatcher.getNewOffset(hm, patch2));
    }

}
