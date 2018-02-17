package doge;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class EditFile {
	public static void main(String[] args) {
		DumpParser dp = new DumpParser("dump.cs");
		dp.initialize();
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("test.txt")));
			for (int key: dp.map.keySet()) {
				bw.write(dp.map.get(key) + "\t0x" + Integer.toHexString(key) + "\n");
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
