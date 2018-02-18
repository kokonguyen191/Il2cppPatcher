package doge;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;

public class DumpParserTest {

	@Test
	public void testParseFunctionName() {
		// Normal function
		assertEquals("private static void overrideResistIcons(EnemyParams targetParams)", DumpParser
				.parseFunctionName("private static void overrideResistIcons(EnemyParams targetParams); // 0x117BC04"));

		// Not a function
		assertEquals("", DumpParser.parseFunctionName("public const ShieldType Leftward = 5; // 0x0"));

		// Another function
		assertEquals("private static bool assignIfValid(string result, string value)", DumpParser
				.parseFunctionName("private static bool assignIfValid(string result, string value); // 0x117DD5C"));

		// Function with some weird chars
		assertEquals("public List`1<UnitBase> get_childUnits()",
				DumpParser.parseFunctionName("public List`1<UnitBase> get_childUnits(); // 0x2065DE8"));

		// Very weird function and has some extra spaces at the start
		assertEquals("private static float getListValue(List`1<float> values, int index, optional float defaultValue)",
				DumpParser.parseFunctionName(
						"	private static float getListValue(List`1<float> values, int index, optional float defaultValue); // 0x1F741DC\r\n"));
	}

	@Test
	public void testParseFunctionAddress() {
		// Normal function
		assertEquals(0x117BC04, DumpParser.parseFunctionAddress(
				"private static void overrideResistIcons(EnemyParams targetParams); // 0x117BC04"));

		// Not a function
		assertEquals(0x2, DumpParser.parseFunctionAddress("public const ShieldType Leftward = 5; // 0x2"));

		// Another function
		assertEquals(0x117DD5C, DumpParser
				.parseFunctionAddress("private static bool assignIfValid(string result, string value); // 0x117DD5C"));

		// Function with some weird chars
		assertEquals(0x2065DE8,
				DumpParser.parseFunctionAddress("public List`1<UnitBase> get_childUnits(); // 0x2065DE8"));

		// Very weird function and has some extra spaces at the start
		assertEquals(0x1F741DC, DumpParser.parseFunctionAddress(
				"	private static float getListValue(List`1<float> values, int index, optional float defaultValue); // 0x1F741DC\r\n"));
	}

	@Test
	public void testAddFunction() throws IOException {
		File f = new File("tempEmptyTestFile");
		f.createNewFile();

		DumpParser dp = new DumpParser("tempEmptyTestFile");
		HashMap<Integer, String> hm = new HashMap<Integer, String>();
		dp.functionsNeeded.add("private static void overrideResistIcons(EnemyParams targetParams)");
		dp.functionsNeeded.add("public List`1<UnitBase> get_childUnits()");
		dp.functionsNeeded
				.add("private static float getListValue(List`1<float> values, int index, optional float defaultValue)");

		// Normal function
		dp.addFunction("private static void overrideResistIcons(EnemyParams targetParams); // 0x117BC04");
		hm.put(0x117BC04, "private static void overrideResistIcons(EnemyParams targetParams)");
		assertEquals(hm, dp.getMap());

		// Not a function
		dp.addFunction("public const ShieldType Leftward = 5; // 0x0");
		assertEquals(hm, dp.getMap());

		// Function not needed
		dp.addFunction("private static bool assignIfValid(string result, string value); // 0x117DD5C");
		assertEquals(hm, dp.getMap());

		// Another function
		dp.addFunction("public List`1<UnitBase> get_childUnits(); // 0x2065DE8");
		hm.put(0x2065DE8, "public List`1<UnitBase> get_childUnits()");
		assertEquals(hm, dp.getMap());

		// A weird formatted function
		dp.addFunction(
				"        private static float getListValue(List`1<float> values, int index, optional float defaultValue); // 0x1F741DC\\r\\n");
		hm.put(0x1F741DC,
				"private static float getListValue(List`1<float> values, int index, optional float defaultValue)");
		assertEquals(hm, dp.getMap());

		f.delete();
	}

}
