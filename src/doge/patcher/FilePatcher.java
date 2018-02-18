package doge.patcher;

import doge.data.Function;
import doge.data.Modification;
import doge.data.Patch;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

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
        ArrayList<Patch> patches = mod.getPatches();
        for (Patch patch : patches) {
            int newOffset = FilePatcher.getNewOffset(listOfNewFunctions, patch);
            FilePatcher.writePatch(fh, newOffset, patch.getNewBytes());
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
        ArrayList<Patch> patches = mod.getPatches();
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
