package doge.data;

import static org.junit.Assert.*;

import org.junit.Test;

public class PatchTest {

	@Test
	public void testParsePatch() {
		// Normal function
		Function testFunction1 = new Function("public int get_ActivateLimit()", 0xA58BDC);
		assertEquals(
				new Patch(testFunction1, 184, 5, new byte[] { 31, -41, -1, -21, 16 },
						new byte[] { 0, 10, -76, -18, 0 }),
				Patch.parsePatch("00A58C94	0x5	1F D7 FF EB 10 	00 0A B4 EE 00 ", testFunction1));

		// Another function
		Function testFunction2 = Function.parseFunction(
				"public void Shot(UnitBase sender, UnitActionLockOnOwnerType lockOnOwnerType, int shotIdx, int commandIdx, float limitAngle, bool deviation, Vector3 offsetPosition, Quaternion offsetRotation, float effectScale); // 0x115ED90");
		assertEquals(
				new Patch(testFunction2, 0, 4, new byte[] { (byte) 0xF0, (byte) 0x4F, (byte) 0x2D, (byte) 0xE9 },
						new byte[] { (byte) 0x1E, (byte) 0xFF, (byte) 0x2F, (byte) 0xE1 }),
				Patch.parsePatch("0115ED90	0x4	F0 4F 2D E9 	1E FF 2F E1 ", testFunction2));
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testParsePatchLengthLargerThanBytesAvailable() {
		Function testFunction = new Function("public int get_ActivateLimit()", 0xA58BDC);
		Patch patch = Patch.parsePatch("00A58C94	0x7	1F D7 FF EB 10 	00 0A B4 EE 00 ", testFunction);
	}

	@Test(expected = NumberFormatException.class)
	public void testParsePatchInvalidNumberFormat() {
		Function testFunction = new Function("public int get_ActivateLimit()", 0xA58BDC);
		Patch patch = Patch.parsePatch("00A58C94	0x5	KF D7 FF EB 10 	00 0A B4 EE 00 ", testFunction);
	}

}
