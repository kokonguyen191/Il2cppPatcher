package doge.data.patch;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class ModificationTest {

    @Test
    public void testParsePatches() throws Exception {
        Function function = Function
                .parseFunction("public float get_SkillPointNormalizedRatio(); // 0x97AC94");
        Patch patch1 = Patch.parsePatch("0097AD34\t0x1\t08 \t00", function);
        Patch patch2 = Patch.parsePatch("0097AD36\t0x1\t89 \tB7", function);

        assertArrayEquals(new Patch[]{patch1, patch2}, Modification
                .parsePatches("public float get_SkillPointNormalizedRatio(); // 0x97AC94\n"
                        + "0097AD34\t0x1\t08 \t00\n"
                        + "0097AD36\t0x1\t89 \tB7"));
    }

}