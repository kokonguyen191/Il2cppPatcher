package doge;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.Test;

public class DumpParserTest {

	@Test
	public void testParseFunctionName() {
		// Normal function
		assertEquals("overrideResistIcons", DumpParser
				.parseFunctionName("private static void overrideResistIcons(EnemyParams targetParams); // 0x117BC04"));

		// Not a function
		assertEquals("", DumpParser.parseFunctionName("public const ShieldType Leftward = 5; // 0x0"));

		// Another function
		assertEquals("assignIfValid", DumpParser
				.parseFunctionName("private static bool assignIfValid(string result, string value); // 0x117DD5C"));

		// Function with some weird chars
		assertEquals("get_childUnits",
				DumpParser.parseFunctionName("public List`1<UnitBase> get_childUnits(); // 0x2065DE8"));

		// Very weird function and has some extra spaces at the start
		assertEquals("getListValue", DumpParser.parseFunctionName(
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
		HashSet<Integer> hs = new HashSet<Integer>();
		dp.functionsNeeded.add("overrideResistIcons");
		dp.functionsNeeded.add("get_childUnits");
		dp.functionsNeeded.add("getListValue");

		// Normal function
		dp.addFunction("private static void overrideResistIcons(EnemyParams targetParams); // 0x117BC04");
		hs.add(0x117BC04);
		assertEquals(hs, dp.getFunctionAddresses());

		// Not a function
		dp.addFunction("public const ShieldType Leftward = 5; // 0x0");
		assertEquals(hs, dp.getFunctionAddresses());

		// Function not needed
		dp.addFunction("private static bool assignIfValid(string result, string value); // 0x117DD5C");
		assertEquals(hs, dp.getFunctionAddresses());

		// Another function
		dp.addFunction("public List`1<UnitBase> get_childUnits(); // 0x2065DE8");
		hs.add(0x2065DE8);
		assertEquals(hs, dp.getFunctionAddresses());

		// A weird formatted function
		dp.addFunction(
				"        private static float getListValue(List`1<float> values, int index, optional float defaultValue); // 0x1F741DC\\r\\n");
		hs.add(0x1F741DC);
		assertEquals(hs, dp.getFunctionAddresses());

		f.delete();
	}

}
