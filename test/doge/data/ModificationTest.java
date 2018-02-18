package doge.data;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import org.junit.Test;

public class ModificationTest {

    @Test
    public void testParsePatches() {
        Function function = Function
                .parseFunction("public float get_SkillPointNormalizedRatio(); // 0x97AC94");
        Patch patch1 = Patch.parsePatch("0097AD34\t0x1\t08 \t00", function);
        Patch patch2 = Patch.parsePatch("0097AD36\t0x1\t89 \tB7", function);
        ArrayList<Patch> patches = new ArrayList<Patch>();
        patches.add(patch1);
        patches.add(patch2);

        assertEquals(patches, Modification
                .parsePatches("public float get_SkillPointNormalizedRatio(); // 0x97AC94\n"
                        + "0097AD34\t0x1\t08 \t00\n"
                        + "0097AD36\t0x1\t89 \tB7"));
    }

    @Test
    public void testParseMod() {

        Function function1 = Function.parseFunction(
                "public void Shot(UnitBase sender, UnitActionLockOnOwnerType lockOnOwnerType, int shotIdx, int commandIdx, float limitAngle, bool deviation); // 0x11630C8");
        Patch patch1 = Patch.parsePatch("011630C8\t0x4\tF0 4F 2D E9 \t1E FF 2F E1 ", function1);
        Function function2 = Function.parseFunction(
                "public void Shot(UnitBase sender, UnitActionLockOnOwnerType lockOnOwnerType, int shotIdx, int commandIdx, float limitAngle, bool deviation, Vector3 offsetPosition, Quaternion offsetRotation, float effectScale); // 0x115ED90");
        Patch patch2 = Patch.parsePatch("0115ED90\t0x4\tF0 4F 2D E9 \t1E FF 2F E1 ", function2);
        ArrayList<Patch> patches = new ArrayList<Patch>();
        patches.add(patch1);
        patches.add(patch2);

        assertEquals(new Modification("Godmode", patches),
                Modification.parseMod("Godmode\n"
                        + "public void Shot(UnitBase sender, UnitActionLockOnOwnerType lockOnOwnerType, int shotIdx, int commandIdx, float limitAngle, bool deviation); // 0x11630C8\n"
                        + "011630C8\t0x4\tF0 4F 2D E9 \t1E FF 2F E1 \n"
                        + "public void Shot(UnitBase sender, UnitActionLockOnOwnerType lockOnOwnerType, int shotIdx, int commandIdx, float limitAngle, bool deviation, Vector3 offsetPosition, Quaternion offsetRotation, float effectScale); // 0x115ED90\n"
                        + "0115ED90\t0x4\tF0 4F 2D E9 \t1E FF 2F E1 "));
    }

}