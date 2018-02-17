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
		assertEquals(
				DumpParser.parseFunctionName(
						"private static void overrideResistIcons(EnemyParams targetParams); // 0x117BC04"),
				"overrideResistIcons");

		// Not a function
		assertEquals(DumpParser.parseFunctionName("public const ShieldType Leftward = 5; // 0x0"), "");

		// Another function
		assertEquals(
				DumpParser.parseFunctionName(
						"private static bool assignIfValid(string result, string value); // 0x117DD5C"),
				"assignIfValid");

		// Function with some weird chars
		assertEquals(DumpParser.parseFunctionName("public List`1<UnitBase> get_childUnits(); // 0x2065DE8"),
				"get_childUnits");

		// Very weird function and has some extra spaces at the start
		assertEquals(DumpParser.parseFunctionName(
				"	private static float getListValue(List`1<float> values, int index, optional float defaultValue); // 0x1F741DC\r\n"),
				"getListValue");
	}

	@Test
	public void testParseFunctionAddress() {
		// Normal function
		assertEquals(DumpParser.parseFunctionAddress(
				"private static void overrideResistIcons(EnemyParams targetParams); // 0x117BC04"), 0x117BC04);

		// Not a function
		assertEquals(DumpParser.parseFunctionAddress("public const ShieldType Leftward = 5; // 0x0"), 0x0);

		// Another function
		assertEquals(DumpParser.parseFunctionAddress(
				"private static bool assignIfValid(string result, string value); // 0x117DD5C"), 0x117DD5C);

		// Function with some weird chars
		assertEquals(DumpParser.parseFunctionAddress("public List`1<UnitBase> get_childUnits(); // 0x2065DE8"),
				0x2065DE8);

		// Very weird function and has some extra spaces at the start
		assertEquals(DumpParser.parseFunctionAddress(
				"	private static float getListValue(List`1<float> values, int index, optional float defaultValue); // 0x1F741DC\r\n"),
				0x1F741DC);
	}

	@Test
	public void testAddFunction() throws IOException {
		File f = new File("tempEmptyTestFile");
		f.createNewFile();

		DumpParser dp = new DumpParser("tempEmptyTestFile");
		HashMap<Integer, String> hm = new HashMap<Integer, String>();
		dp.functionsNeeded.add("overrideResistIcons");
		dp.functionsNeeded.add("get_childUnits");
		dp.functionsNeeded.add("getListValue");

		// Normal function
		dp.addFunction("private static void overrideResistIcons(EnemyParams targetParams); // 0x117BC04");
		hm.put(0x117BC04, "overrideResistIcons");
		assertEquals(dp.map, hm);

		// Not a function
		dp.addFunction("public const ShieldType Leftward = 5; // 0x0");
		assertEquals(dp.map, hm);

		// Function not needed
		dp.addFunction("private static bool assignIfValid(string result, string value); // 0x117DD5C");
		assertEquals(dp.map, hm);

		// Another function
		dp.addFunction("public List`1<UnitBase> get_childUnits(); // 0x2065DE8");
		hm.put(0x2065DE8, "get_childUnits");
		assertEquals(dp.map, hm);

		// A weird formatted function
		dp.addFunction(
				"        private static float getListValue(List`1<float> values, int index, optional float defaultValue); // 0x1F741DC\\r\\n");
		hm.put(0x1F741DC, "getListValue");
		assertEquals(dp.map, hm);

		f.delete();
	}

}
