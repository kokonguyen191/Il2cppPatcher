package doge.data.patch;

import java.util.Arrays;

/**
 * Contain hex changes that can be made to a C++ function
 */
public class Patch {

    public static final String LF = "\n";
    public static final String PATCH_DELIMITER_REGEX = "[\\s]+";
    public static final int PATCH_OFFSET_INDEX = 0;
    public static final int PATCH_LENGTH_INDEX = 1;
    public static final int BYTES_START_INDEX = 2;

    private Function function;
    private int offset;
    private int length;
    private byte[] originalBytes;
    private byte[] newBytes;

    /**
     * Constructor
     *
     * @param function a Function object containing the function that the patch will be made to
     * @param offset offset of the first byte of the patch, counting from the start address of the function
     * @param length number of consecutive bytes that the patch contains
     * @param originalBytes the original byte sequence, used for validity checking
     * @param newBytes the new byte sequence
     */
    Patch(Function function, int offset, int length, byte[] originalBytes, byte[] newBytes) {
        this.function = function;
        this.offset = offset;
        this.length = length;
        this.originalBytes = originalBytes;
        this.newBytes = newBytes;
    }

    /**
     * Return a Patch object from a patch string (line)
     *
     * @param line the patch string
     * @param function the Function object containing the function that will be applied the patch
     * @return the Patch object
     */
    public static Patch parsePatch(String line, Function function) {
        String[] splitted = line.split(PATCH_DELIMITER_REGEX);

        int parsedOffset =
                Integer.parseInt(splitted[PATCH_OFFSET_INDEX], 16) - function.getFunctionOffset();
        String trimmedLengthString = splitted[PATCH_LENGTH_INDEX].substring(2);
        int parsedLength = Integer.parseInt(trimmedLengthString, 16);

        byte[] parsedOriginalBytes = new byte[parsedLength];
        byte[] parsedNewBytes = new byte[parsedLength];

        for (int i = 0; i < parsedLength; i++) {
            parsedOriginalBytes[i] = (byte) Integer.parseInt(splitted[BYTES_START_INDEX + i], 16);
            parsedNewBytes[i] = (byte) Integer
                    .parseInt(splitted[BYTES_START_INDEX + i + parsedLength], 16);
        }

        return new Patch(function, parsedOffset, parsedLength, parsedOriginalBytes, parsedNewBytes);
    }

    public Function getFunction() {
        return function;
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }

    public byte[] getOriginalBytes() {
        return originalBytes;
    }

    public byte[] getNewBytes() {
        return newBytes;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Patch // instanceof handles nulls
                && this.function.equals(((Patch) other).function)
                && Arrays.equals(this.originalBytes, ((Patch) other).originalBytes)
                && Arrays.equals(this.newBytes, ((Patch) other).newBytes)
                && this.offset == ((Patch) other).offset && this.length == ((Patch) other).length);
    }
}
