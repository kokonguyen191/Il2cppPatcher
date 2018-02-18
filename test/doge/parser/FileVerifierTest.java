package doge.parser;

import static org.junit.Assert.*;

import doge.data.Function;
import doge.data.Patch;
import doge.patcher.FilePatcher;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import org.junit.Test;

public class FileVerifierTest {

    @Test
    public void testIsFileAndModListMatched() throws Exception {
    }

    @Test
    public void testIsFileAndModMatched() throws Exception {
    }

    @Test
    public void testIsFileAndPatchMatched() throws Exception {
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