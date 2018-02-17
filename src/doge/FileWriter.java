package doge;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

public class FileWriter {

	private static final String DEFAULT_IL2CPP_FILE_PATH = "libil2cpp.so";

	private String filePath;
	private File f;

	public static void main(String[] args) {
		RandomAccessFile fh;
		try {
			fh = new RandomAccessFile(new File("libil2cpp.so"), "rw");
			fh.seek(0x117BC04);
			System.out.println(Integer.toHexString(fh.read()));
			System.out.println(Integer.toHexString(fh.read()));
			System.out.println(Integer.toHexString(fh.read()));
			System.out.println(Integer.toHexString(fh.read()));
			fh.seek(0x117BC04);
			fh.write(0xF0);
			fh.seek(0x117BC04);
			System.out.println(Integer.toHexString(fh.read()));
			fh.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writePatch(RandomAccessFile fh, int location, byte[] patch) throws IOException {
		fh.seek(location);
		fh.write(patch);
	}
}
