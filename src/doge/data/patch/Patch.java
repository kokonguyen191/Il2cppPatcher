package doge.data.patch;

import java.util.Arrays;

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

	Patch(Function function, int offset, int length, byte[] originalBytes, byte[] newBytes) {
		this.function = function;
		this.offset = offset;
		this.length = length;
		this.originalBytes = originalBytes;
		this.newBytes = newBytes;
	}

	public static Patch parsePatch(String line, Function function) {
		String[] splitted = line.split(PATCH_DELIMITER_REGEX);

		int parsedOffset = Integer.parseInt(splitted[PATCH_OFFSET_INDEX], 16) - function.getFunctionOffset();
		String trimmedLengthString = splitted[PATCH_LENGTH_INDEX].substring(2);
		int parsedLength = Integer.parseInt(trimmedLengthString, 16);

		byte[] parsedOriginalBytes = new byte[parsedLength];
		byte[] parsedNewBytes = new byte[parsedLength];

		for (int i = 0; i < parsedLength; i++) {
			parsedOriginalBytes[i] = (byte) Integer.parseInt(splitted[BYTES_START_INDEX + i], 16);
			parsedNewBytes[i] = (byte) Integer.parseInt(splitted[BYTES_START_INDEX + i + parsedLength], 16);
		}

		return new Patch(function, parsedOffset, parsedLength, parsedOriginalBytes, parsedNewBytes);
	}

	public static void main(String[] args) {
		Patch p = Patch.parsePatch("00A58C94 0x6 KF D7 FF EB 10 00 0A B4 EE 00 ",
				new Function("public int get_ActivateLimit()", 0xA58BDC));
		System.out.println("Length: " + p.getLength());
		System.out.println("Offset: " + p.getOffset());
		System.out.print("Ori:");
		for (int i = 0; i < p.getLength(); i++) {
			System.out.print(" " + p.getOriginalBytes()[i]);
		}
		System.out.println();
		System.out.print("New:");
		for (int i = 0; i < p.getLength(); i++) {
			System.out.print(" " + p.getNewBytes()[i]);
		}
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
