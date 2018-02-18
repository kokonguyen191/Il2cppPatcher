package doge.patcher;

import doge.data.Function;
import doge.data.Modification;
import doge.data.Patch;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.LinkedList;

public class FilePatcher {

    /**
     * Enable a modification
     *
     * @param fh RandomAccessFile for better performance
     * @param listOfNewFunctions can be gotten from a DumpParser
     * @param mod a modification
     */
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

    /**
     * Disable a modification
     *
     * @param fh RandomAccessFile for better performance
     * @param listOfNewFunctions can be gotten from a DumpParser
     * @param mod a modification
     */
    public static void revertAllPatchesInOneMod(RandomAccessFile fh,
            HashMap<String, Function> listOfNewFunctions, Modification mod)
            throws IOException {
        LinkedList<Patch> patches = mod.getPatches();
        for (Patch patch : patches) {
            int newOffset = FilePatcher.getNewOffset(listOfNewFunctions, patch);
            FilePatcher.writePatch(fh, newOffset, patch.getOriginalBytes());
        }
    }

    /**
     * Given a list of functions which we can retrieve from a DumpParser and a patch, return the new start offset to patch
     *
     * @param listOfNewFunctions can be gotten from a DumpParser
     * @param patch the patch
     * @return the new start offset, we can start patching from here
     */
    public static int getNewOffset(HashMap<String, Function> listOfNewFunctions, Patch patch) {
        String functionName = patch.getFunction().getFunctionName();
        Function newFunction = listOfNewFunctions.get(functionName);
        return newFunction.getFunctionOffset() + patch.getOffset();
    }

    /**
     * Check if the file matches a given series of bytes
     *
     * @param fh RandomAccessFile for better performance
     * @param location starting location of the check
     * @param length the length of the check
     * @param originalBytes the series of byte to check
     * @return true of they match
     * @throws IllegalArgumentException if they don't match
     */
    public static boolean matchesOriginal(RandomAccessFile fh, int location, int length,
            byte[] originalBytes)
            throws IOException {
        fh.seek(location);
        for (int i = 0; i < length; i++) {
            byte nextByte = fh.readByte();
            if (nextByte != originalBytes[i]) {
                throw new IllegalArgumentException(String.format(
                        "Patch and libil2cpp binary does not match! At 0x%08X, patch has %02X while file has %02X",
                        location + i, originalBytes[i], nextByte));
            }
        }
        return true;
    }

    /**
     * Overwrite a series of bytes starting from location
     *
     * @param fh RandomAccessFile for better performance
     * @param location starting location of the patch
     * @param patch series of bytes
     */
    public static void writePatch(RandomAccessFile fh, int location, byte[] patch)
            throws IOException {
        fh.seek(location);
        fh.write(patch);
    }

}
