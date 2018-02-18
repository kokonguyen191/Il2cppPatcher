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

    public static boolean isFileAndModListMatched(RandomAccessFile fh,
            HashMap<String, Function> listOfNewFunctions,
            ModificationList listOfMods) throws IOException {
        boolean result = true;

        ArrayList<Modification> listOfModificationObjects = listOfMods.getListOfMods();
        for (Modification mod : listOfModificationObjects) {
            boolean fileAndModMatch = FileVerifier.isFileAndModMatched(fh, listOfNewFunctions, mod);
            result = result && fileAndModMatch;
        }

        return result;
    }

    public static boolean isFileAndModMatched(RandomAccessFile fh,
            HashMap<String, Function> listOfNewFunctions, Modification mod) throws IOException {
        for (Patch patch : mod.getPatches()) {
            boolean fileAndPatchMatch = FileVerifier
                    .isFileAndPatchMatched(fh, listOfNewFunctions, patch);
            if (!fileAndPatchMatch) {
                System.out.println("Mismatch at mod option: " + mod.getModName());
                return false;
            }
        }
        return true;
    }

    public static boolean isFileAndPatchMatched(RandomAccessFile fh,
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
     * @return true of they match, else false
     */
    public static boolean isFileAndBytesMatched(RandomAccessFile fh, int location, int length,
            byte[] bytes)
            throws IOException {
        fh.seek(location);
        for (int i = 0; i < length; i++) {
            byte nextByte = fh.readByte();
            if (nextByte != bytes[i]) {
                System.out.println(String.format(
                        "Patch and libil2cpp binary does not match! At 0x%08X, patch has %02X while file has %02X",
                        location + i, bytes[i], nextByte));
                return false;
            }
        }
        return true;
    }
}
