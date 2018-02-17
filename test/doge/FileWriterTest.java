package doge;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

import org.junit.Test;

public class FileWriterTest {

	@Test
	public void testWritePatch() throws IOException {
		File f = new File("tempEmptyTestFile");
		f.createNewFile();
		
		FileOutputStream fos = new FileOutputStream(f);
		byte[] bytes = {(byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15, (byte) 0x16, (byte) 0x17, (byte) 0x18};
		fos.write(bytes);
		fos.flush();
		
		byte[] changeBytes = {(byte) 0xf0, (byte) 0xf1, (byte) 0xf2};
		FileWriter.writePatch(new RandomAccessFile(f, "rw"), 4, changeBytes);
		
		FileInputStream fis = new FileInputStream(f);
		fis.read(bytes);
		
		byte[] newBytes = {(byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0xf0, (byte) 0xf1, (byte) 0xf2, (byte) 0x18};
		
		fis.close();
		fos.close();
		f.delete();
		
		assertArrayEquals(newBytes, bytes);
	}

}
