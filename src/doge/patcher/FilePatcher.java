package doge.patcher;

import doge.data.Function;
import doge.data.Modification;
import doge.data.Patch;
import doge.parser.FileVerifier;
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
        boolean fileAndNewModMatched = FileVerifier
                .isFileAndNewModMatched(fh, listOfNewFunctions, mod);
        boolean fileAndOldModMatched = FileVerifier
                .isFileAndOldModMatched(fh, listOfNewFunctions, mod);

        if (fileAndNewModMatched || fileAndOldModMatched) {
            ArrayList<Patch> patches = mod.getPatches();
            for (Patch patch : patches) {
                FilePatcher.writePatch(fh, listOfNewFunctions, patch);
            }
        } else {
            FileVerifier.printModMismatch(mod);
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
        boolean fileAndNewModMatched = FileVerifier
                .isFileAndNewModMatched(fh, listOfNewFunctions, mod);
        boolean fileAndOldModMatched = FileVerifier
                .isFileAndOldModMatched(fh, listOfNewFunctions, mod);

        if (fileAndNewModMatched || fileAndOldModMatched) {
            ArrayList<Patch> patches = mod.getPatches();
            for (Patch patch : patches) {
                FilePatcher.revertPatch(fh, listOfNewFunctions, patch);
            }
        } else {
            FileVerifier.printModMismatch(mod);
        }
    }

    /**
     * Enable a patch
     *
     * @param fh RandomAccessFile for better performance
     * @param listOfNewFunctions can be gotten from a DumpParser
     * @param patch the Patch object
     */
    public static void writePatch(RandomAccessFile fh,
            HashMap<String, Function> listOfNewFunctions, Patch patch) throws IOException {
        boolean fileAndNewPatchMatched = FileVerifier
                .isFileAndNewPatchMatched(fh, listOfNewFunctions, patch);
        boolean fileAndOldPatchMatched = FileVerifier
                .isFileAndOldPatchMatched(fh, listOfNewFunctions, patch);

        int newOffset = FilePatcher.getNewOffset(listOfNewFunctions, patch);
        if (fileAndNewPatchMatched || fileAndOldPatchMatched) {
            FilePatcher.writeBytes(fh, newOffset, patch.getNewBytes());
        } else {
            FileVerifier.printMismatchBytes(fh, newOffset, patch.getLength(), patch.getNewBytes());
        }
    }

    /**
     * Disable a patch
     *
     * @param fh RandomAccessFile for better performance
     * @param listOfNewFunctions can be gotten from a DumpParser
     * @param patch the Patch object
     */
    public static void revertPatch(RandomAccessFile fh,
            HashMap<String, Function> listOfNewFunctions, Patch patch) throws IOException {
        boolean fileAndNewPatchMatched = FileVerifier
                .isFileAndNewPatchMatched(fh, listOfNewFunctions, patch);
        boolean fileAndOldPatchMatched = FileVerifier
                .isFileAndOldPatchMatched(fh, listOfNewFunctions, patch);

        int newOffset = FilePatcher.getNewOffset(listOfNewFunctions, patch);
        if (fileAndNewPatchMatched || fileAndOldPatchMatched) {
            FilePatcher.writeBytes(fh, newOffset, patch.getOriginalBytes());
        } else {
            FileVerifier.printMismatchBytes(fh, newOffset, patch.getLength(), patch.getNewBytes());
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
     * @param bytes series of bytes
     */
    public static void writeBytes(RandomAccessFile fh, int location, byte[] bytes)
            throws IOException {
        fh.seek(location);
        fh.write(bytes);
    }

}
