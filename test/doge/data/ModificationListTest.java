package doge.data;

import static doge.data.TestObjects.getTestModList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.LinkedList;
import org.junit.Test;

public class ModificationListTest {

    @Test
    public void testParseListOfMods() {
        ModificationList modList = ModificationList.parseListOfMods("#\n"
                + "Infinite skill usage\n"
                + "public int get_ActivateLimit(); // 0xA58E80\n"
                + "00A58F27\t0x1\t0A \tEA\n"
                + "#\n"
                + "No skill cool down\n"
                + "public float get_CoolTime(); // 0xA57688\n"
                + "00A57744\t0x1\t10 \t00 \n"
                + "00A57746\t0x1\t00 \tB4 \n"
                + "00A57749\t0x2\t1A BF \t0A 20 ");

        Modification mod1 = Modification.parseMod("Infinite skill usage\n"
                + "public int get_ActivateLimit(); // 0xA58E80\n"
                + "00A58F27\t0x1\t0A \tEA\n");
        Modification mod2 = Modification.parseMod("No skill cool down\n"
                + "public float get_CoolTime(); // 0xA57688\n"
                + "00A57744\t0x1\t10 \t00 \n"
                + "00A57746\t0x1\t00 \tB4 \n"
                + "00A57749\t0x2\t1A BF \t0A 20 ");
        ArrayList<Modification> mods = new ArrayList<Modification>();
        mods.add(mod1);
        mods.add(mod2);
        ModificationList expected = new ModificationList(mods);

        assertEquals(expected, modList);
    }

    @Test
    public void testContainsFunction() {
        ModificationList modList = getTestModList();

        // Contains
        assertTrue(modList.containsFunction("public float get_CoolTime()"));

        // Does not contain
        assertFalse(modList.containsFunction("Rand0m stuFF"));

        // Contains
        assertTrue(modList.containsFunction("public void Shot(UnitBase sender, UnitActionLockOnOwnerType lockOnOwnerType, int shotIdx, int commandIdx, float limitAngle, bool deviation, Vector3 offsetPosition, Quaternion offsetRotation, float effectScale)"));
    }
}