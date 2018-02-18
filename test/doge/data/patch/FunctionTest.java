package doge.data.patch;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FunctionTest {

    @Test
    public void testParseFunction() {
        // Normal function
        assertEquals(
                new Function("private static void overrideResistIcons(EnemyParams targetParams)",
                        0x117BC04),
                Function.parseFunction(
                        "private static void overrideResistIcons(EnemyParams targetParams); // 0x117BC04"));

        // Not a function
        assertEquals(null, Function.parseFunction("public const ShieldType Leftward = 5; // 0x0"));

        // Another function
        assertEquals(new Function("private static bool assignIfValid(string result, string value)",
                        0x117DD5C),
                Function.parseFunction(
                        "private static bool assignIfValid(string result, string value); // 0x117DD5C"));

        // Function with some weird chars
        assertEquals(new Function("public List`1<UnitBase> get_childUnits()", 0x2065DE8),
                Function.parseFunction("public List`1<UnitBase> get_childUnits(); // 0x2065DE8"));

        // Very weird function and has some extra spaces at the start
        assertEquals(new Function(
                        "private static float getListValue(List`1<float> values, int index, optional float defaultValue)",
                        0x1F741DC),
                Function.parseFunction(
                        "	private static float getListValue(List`1<float> values, int index, optional float defaultValue); // 0x1F741DC\r\n"));
    }

    @Test
    public void testParseFunctionName() {
        // Normal function
        assertEquals("private static void overrideResistIcons(EnemyParams targetParams)", Function
                .parseFunctionName(
                        "private static void overrideResistIcons(EnemyParams targetParams); // 0x117BC04"));

        // Not a function
        assertEquals("",
                Function.parseFunctionName("public const ShieldType Leftward = 5; // 0x0"));

        // Another function
        assertEquals("private static bool assignIfValid(string result, string value)", Function
                .parseFunctionName(
                        "private static bool assignIfValid(string result, string value); // 0x117DD5C"));

        // Function with some weird chars
        assertEquals("public List`1<UnitBase> get_childUnits()",
                Function.parseFunctionName(
                        "public List`1<UnitBase> get_childUnits(); // 0x2065DE8"));

        // Very weird function and has some extra spaces at the start
        assertEquals(
                "private static float getListValue(List`1<float> values, int index, optional float defaultValue)",
                Function.parseFunctionName(
                        "	private static float getListValue(List`1<float> values, int index, optional float defaultValue); // 0x1F741DC\r\n"));
    }

    @Test
    public void testParseFunctionAddress() {
        // Normal function
        assertEquals(0x117BC04, Function.parseFunctionAddress(
                "private static void overrideResistIcons(EnemyParams targetParams); // 0x117BC04"));

        // Not a function
        assertEquals(0x2,
                Function.parseFunctionAddress("public const ShieldType Leftward = 5; // 0x2"));

        // Another function
        assertEquals(0x117DD5C, Function
                .parseFunctionAddress(
                        "private static bool assignIfValid(string result, string value); // 0x117DD5C"));

        // Function with some weird chars
        assertEquals(0x2065DE8,
                Function.parseFunctionAddress(
                        "public List`1<UnitBase> get_childUnits(); // 0x2065DE8"));

        // Very weird function and has some extra spaces at the start
        assertEquals(0x1F741DC, Function.parseFunctionAddress(
                "	private static float getListValue(List`1<float> values, int index, optional float defaultValue); // 0x1F741DC\r\n"));
    }

}
