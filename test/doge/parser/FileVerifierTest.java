package doge.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import doge.data.Function;
import doge.data.Modification;
import doge.data.ModificationList;
import doge.data.Patch;
import doge.patcher.FilePatcher;
import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import org.junit.Test;

public class FileVerifierTest {

    @Test
    public void testIsFileAndModListMatched() throws Exception {
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
        Function func2 = Function.parseFunction("test2(); // 0x2");
        hm.put("test1()", func1);
        hm.put("test2()", func2);
        ModificationList listOfMods = ModificationList.parseListOfMods("#\n"
                + "Test\n"
                + "test1(); // 0x0\n"
                + "01\t0x1\t12 \tFF\n"
                + "#\n"
                + "TestTest\n"
                + "test2(); // 0x1111\n"
                + "001114\t0x1\t16 \tFE \n"
                + "001115\t0x2\t17 18 \tFD FC ");
        RandomAccessFile fh = new RandomAccessFile(f, "rw");

        assertTrue(FileVerifier.isFileAndModListMatched(fh, hm, listOfMods));

        fh.close();

        f.delete();
    }

    @Test
    public void testIsFileAndModMatched() throws Exception {
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

        assertTrue(FileVerifier.isFileAndOldModMatched(fh, hm, mod));
        assertFalse(FileVerifier.isFileAndNewModMatched(fh, hm, mod));

        FilePatcher.writeAllPatchesInOneMod(fh, hm, mod);

        assertFalse(FileVerifier.isFileAndOldModMatched(fh, hm, mod));
        assertTrue(FileVerifier.isFileAndNewModMatched(fh, hm, mod));

        fh.close();

        f.delete();
    }

    @Test
    public void testIsFileAndPatchMatched() throws Exception {
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
        Patch invalidPatch = Patch.parsePatch("00000001\t0x2\t00 00 \tFF 69 ", function);

        RandomAccessFile fh = new RandomAccessFile(f, "rw");

        assertTrue(FileVerifier.isFileAndOldPatchMatched(fh, hm, validPatch));
        assertFalse(FileVerifier.isFileAndOldPatchMatched(fh, hm, invalidPatch));

        FilePatcher.writePatch(fh, hm, validPatch);

        assertTrue(FileVerifier.isFileAndNewPatchMatched(fh, hm, validPatch));
        assertFalse(FileVerifier.isFileAndOldPatchMatched(fh, hm, validPatch));

        fh.close();

        f.delete();
    }

    @Test
    public void testIsFileAndBytesMatched() throws Exception {
        File f = new File("tempEmptyTestFile");
        f.createNewFile();

        FileOutputStream fos = new FileOutputStream(f);
        byte[] bytes = {(byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15,
                (byte) 0x16, (byte) 0x17, (byte) 0x18};
        fos.write(bytes);
        fos.flush();

        byte[] originalBytes = {(byte) 0x12, (byte) 0x13, (byte) 0x14};
        RandomAccessFile fh = new RandomAccessFile(f, "rw");

        assertTrue(FileVerifier.isFileAndBytesMatched(fh, 1, 3, originalBytes));
        assertFalse(FileVerifier.isFileAndBytesMatched(fh, 3, 3, originalBytes));

        fh.close();
        fos.close();
        f.delete();
    }
}