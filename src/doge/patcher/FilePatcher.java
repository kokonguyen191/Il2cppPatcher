package doge.patcher;

import doge.data.Function;
import doge.data.Modification;
import doge.data.ModificationList;
import doge.data.Patch;
import doge.parser.DumpParser;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.LinkedList;

public class FilePatcher {

    private String filePath;
    private File f;
    private DumpParser dp;

    public static void writeAllPatchesInOneMod(RandomAccessFile fh,
            HashMap<String, Function> listOfNewFunctions, Modification mod)
            throws IOException {
        LinkedList<Patch> patches = mod.getPatches();
        for (Patch patch : patches) {
            int newOffset = FilePatcher.getNewOffset(listOfNewFunctions, patch);
            if (matchesOriginal(fh, newOffset, patch.getLength(), patch.getOriginalBytes())) {
                FilePatcher.writePatch(fh, newOffset, patch.getNewBytes());
            }
        }
    }

    public static void revertAllPatchesInOneMod(RandomAccessFile fh,
            HashMap<String, Function> listOfNewFunctions, Modification mod)
            throws IOException {
        LinkedList<Patch> patches = mod.getPatches();
        for (Patch patch : patches) {
            int newOffset = FilePatcher.getNewOffset(listOfNewFunctions, patch);
            FilePatcher.writePatch(fh, newOffset, patch.getOriginalBytes());
        }
    }

    public static int getNewOffset(HashMap<String, Function> listOfNewFunctions, Patch patch) {
        String functionName = patch.getFunction().getFunctionName();
        Function newFunction = listOfNewFunctions.get(functionName);
        int newOffset = newFunction.getFunctionOffset() + patch.getOffset();
        return newOffset;
    }

    public static boolean matchesOriginal(RandomAccessFile fh, int location, int length,
            byte[] originalBytes)
            throws IOException {
        fh.seek(location);
        for (int i = 0; i < length; i++) {
            if (fh.read() != originalBytes[i]) {
                return false;
            }
        }
        return true;
    }

    public static void writePatch(RandomAccessFile fh, int location, byte[] patch)
            throws IOException {
        fh.seek(location);
        fh.write(patch);
    }
}
