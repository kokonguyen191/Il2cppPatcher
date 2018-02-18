package doge.parser;

import doge.data.Function;
import doge.data.Modification;
import doge.data.ModificationList;
import doge.data.Patch;
import doge.patcher.FilePatcher;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

public class FileVerifier {

    /**
     * Check if the file matches the original state of ALL MODS
     *
     * @param fh RandomAccessFile for better performance
     * @param listOfNewFunctions can be gotten from DumpParser
     * @param listOfMods a ModificationList object
     * @return true if they match, else false
     */
    public static boolean isFileAndModListMatched(RandomAccessFile fh,
            HashMap<String, Function> listOfNewFunctions,
            ModificationList listOfMods) throws IOException {
        boolean result = true;

        ArrayList<Modification> listOfModificationObjects = listOfMods.getListOfMods();
        for (Modification mod : listOfModificationObjects) {
            boolean fileAndModMatch = FileVerifier
                    .isFileAndOldModMatched(fh, listOfNewFunctions, mod);
            result = result && fileAndModMatch;
        }

        return result;
    }

    /**
     * Check if the file matches the original state of a mod before applying a mod
     * MUST ENSURE THIS TO BE TRUE, IF NOT, PATCHES WILL BE APPLIED INCORRECTLY
     *
     * @param fh RandomAccessFile for better performance
     * @param listOfNewFunctions can be gotten from DumpParser
     * @param mod a Modification object
     * @return true if they match, else false
     */
    public static boolean isFileAndOldModMatched(RandomAccessFile fh,
            HashMap<String, Function> listOfNewFunctions, Modification mod) throws IOException {
        for (Patch patch : mod.getPatches()) {
            boolean fileAndPatchMatch = FileVerifier
                    .isFileAndOldPatchMatched(fh, listOfNewFunctions, patch);
            if (!fileAndPatchMatch) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if the file matches the desired state after applying a mod
     *
     * @param fh RandomAccessFile for better performance
     * @param listOfNewFunctions can be gotten from DumpParser
     * @param mod a Modification object
     * @return true if they match, else false
     */
    public static boolean isFileAndNewModMatched(RandomAccessFile fh,
            HashMap<String, Function> listOfNewFunctions, Modification mod) throws IOException {
        for (Patch patch : mod.getPatches()) {
            boolean fileAndPatchMatch = FileVerifier
                    .isFileAndNewPatchMatched(fh, listOfNewFunctions, patch);
            if (!fileAndPatchMatch) {
                return false;
            }
        }
        return true;
    }

    /**
     * Print the mismatch message for mod
     *
     * @param mod a Modification mod that went wrong
     */
    public static void printModMismatch(Modification mod) {
        System.out.println("Mismatch at mod option: " + mod.getModName());
    }

    /**
     * Check if the file matches the original state of a patch before applying a mod
     *
     * @param fh RandomAccessFile for better performance
     * @param listOfNewFunctions can be gotten from DumpParser
     * @param patch a Patch object
     * @return true if they match, else false
     */
    public static boolean isFileAndOldPatchMatched(RandomAccessFile fh,
            HashMap<String, Function> listOfNewFunctions, Patch patch) throws IOException {
        int newOffset = FilePatcher.getNewOffset(listOfNewFunctions, patch);
        return FileVerifier
                .isFileAndBytesMatched(fh, newOffset, patch.getLength(), patch.getOriginalBytes());
    }

    /**
     * Check if the file matches a mod after it has been applied
     *
     * @param fh RandomAccessFile for better performance
     * @param listOfNewFunctions can be gotten from DumpParser
     * @param patch a Patch object
     * @return true if they match, else false
     */
    public static boolean isFileAndNewPatchMatched(RandomAccessFile fh,
            HashMap<String, Function> listOfNewFunctions, Patch patch) throws IOException {
        int newOffset = FilePatcher.getNewOffset(listOfNewFunctions, patch);
        return FileVerifier
                .isFileAndBytesMatched(fh, newOffset, patch.getLength(), patch.getNewBytes());
    }

    /**
     * Check if the file matches a given series of bytes
     *
     * @param fh RandomAccessFile for better performance
     * @param location starting location of the check
     * @param length the length of the check
     * @param bytes the series of byte to check
     * @return true if they match, else false
     */
    public static boolean isFileAndBytesMatched(RandomAccessFile fh, int location, int length,
            byte[] bytes)
            throws IOException {
        fh.seek(location);
        for (int i = 0; i < length; i++) {
            byte nextByte = fh.readByte();
            if (nextByte != bytes[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Print the mismatch message for bytes
     *
     * @param fh RandomAccessFile for better performance
     * @param location starting location of the check
     * @param length the length of the check
     * @param bytes the series of byte to check
     * @return true if they match, else false
     */
    public static void printMismatchBytes(RandomAccessFile fh, int location, int length,
            byte[] bytes)
            throws IOException {
        fh.seek(location);
        for (int i = 0; i < length; i++) {
            byte nextByte = fh.readByte();
            if (nextByte != bytes[i]) {
                System.out.println(String.format(
                        "Patch and libil2cpp binary does not match! At 0x%08X, patch has %02X while file has %02X",
                        location + i, bytes[i], nextByte));
            }
        }
    }
}
