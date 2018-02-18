package doge.patcher;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import doge.data.Function;
import doge.data.Modification;
import doge.data.Patch;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import org.junit.Test;

public class FilePatcherTest {

    @Test
    public void testWriteAndRevertAllPatchesInOneMod() throws Exception {
        File f = new File("tempEmptyTestFile");
        f.createNewFile();

        FileOutputStream fos = new FileOutputStream(f);
        byte[] bytes = {(byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15,
                (byte) 0x16, (byte) 0x17, (byte) 0x18};
        fos.write(bytes);
        fos.flush();
        fos.close();

        HashMap<String, Function> hm = new HashMap<String, Function>();
        Function func1 = Function.parseFunction("test1(); // 0x0");
        Function func2 = Function.parseFunction("test2(); // 0x4");
        hm.put("test1()", func1);
        hm.put("test2()", func2);
        Modification mod = Modification.parseMod("Test\n"
                + "test1(); // 0x0\n"
                + "01\t0x1\t12 \tFF\n"
                + "test2(); // 0x5\n"
                + "07\t0x2\t17 18\tFE FD");
        RandomAccessFile fh = new RandomAccessFile(f, "rw");
        FilePatcher.writeAllPatchesInOneMod(fh, hm, mod);
        fh.close();

        FileInputStream fis = new FileInputStream(f);
        fis.read(bytes);
        fis.close();

        assertArrayEquals(
                new byte[]{(byte) 0x11, (byte) 0xFF, (byte) 0x13, (byte) 0x14, (byte) 0x15,
                        (byte) 0x16, (byte) 0xFE, (byte) 0xFD}, bytes);

        fh = new RandomAccessFile(f, "rw");
        FilePatcher.revertAllPatchesInOneMod(fh, hm, mod);
        fh.close();

        fis = new FileInputStream(f);
        fis.read(bytes);
        fis.close();

        assertArrayEquals(
                new byte[]{(byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15,
                        (byte) 0x16, (byte) 0x17, (byte) 0x18}, bytes);

        f.delete();
    }

    @Test
    public void testWriteAndRevertPatch() throws Exception {
        File f = new File("tempEmptyTestFile");
        f.createNewFile();

        FileOutputStream fos = new FileOutputStream(f);
        byte[] bytes = {(byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15,
                (byte) 0x16, (byte) 0x17, (byte) 0x18};
        fos.write(bytes);
        fos.flush();
        fos.close();

        HashMap<String, Function> hm = new HashMap<String, Function>();
        Function function = Function.parseFunction("test(); // 0x0");
        hm.put("test()", function);
        Patch validPatch = Patch.parsePatch("00000001\t0x2\t12 13 \tFF FE ", function);
        RandomAccessFile fh = new RandomAccessFile(f, "rw");
        FilePatcher.writePatch(fh, hm, validPatch);
        fh.close();

        FileInputStream fis = new FileInputStream(f);
        fis.read(bytes);
        fis.close();

        assertArrayEquals(
                new byte[]{(byte) 0x11, (byte) 0xFF, (byte) 0xFE, (byte) 0x14, (byte) 0x15,
                        (byte) 0x16, (byte) 0x17, (byte) 0x18}, bytes);

        fh = new RandomAccessFile(f, "rw");
        FilePatcher.revertPatch(fh, hm, validPatch);
        fh.close();

        fis = new FileInputStream(f);
        fis.read(bytes);
        fis.close();

        assertArrayEquals(
                new byte[]{(byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15,
                        (byte) 0x16, (byte) 0x17, (byte) 0x18}, bytes);

        System.out.println("=== EXPECTED OUTPUT ===");
        System.out.println(
                "Patch and libil2cpp binary does not match! At 0x00000001, patch has FF while file has 12\n"
                        + "Patch and libil2cpp binary does not match! At 0x00000002, patch has 69 while file has 13");
        System.out.println("=== ACTUAL OUTPUT ===");
        Patch invalidPatch = Patch.parsePatch("00000001\t0x2\t00 00 \tFF 69 ", function);
        fh = new RandomAccessFile(f, "rw");
        FilePatcher.writePatch(fh, hm, invalidPatch);
        fh.close();

        fis = new FileInputStream(f);
        fis.read(bytes);
        fis.close();

        assertArrayEquals(
                new byte[]{(byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15,
                        (byte) 0x16, (byte) 0x17, (byte) 0x18}, bytes);

        f.delete();
    }

    @Test
    public void testWriteBytes() throws Exception {
        File f = new File("tempEmptyTestFile");
        f.createNewFile();

        FileOutputStream fos = new FileOutputStream(f);
        byte[] bytes = {(byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15,
                (byte) 0x16, (byte) 0x17, (byte) 0x18};
        fos.write(bytes);
        fos.flush();
        fos.close();

        byte[] newBytes = {(byte) 0xF2, (byte) 0xF3, (byte) 0xF4};
        RandomAccessFile fh = new RandomAccessFile(f, "rw");
        FilePatcher.writeBytes(fh, 2, newBytes);
        fh.close();

        FileInputStream fis = new FileInputStream(f);
        fis.read(bytes);
        fis.close();

        assertArrayEquals(
                new byte[]{(byte) 0x11, (byte) 0x12, (byte) 0xF2, (byte) 0xF3, (byte) 0xF4,
                        (byte) 0x16, (byte) 0x17, (byte) 0x18}, bytes);

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
