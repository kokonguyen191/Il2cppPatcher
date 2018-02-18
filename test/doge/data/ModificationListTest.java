package doge.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        LinkedList<Modification> mods = new LinkedList<Modification>();
        mods.add(mod1);
        mods.add(mod2);
        ModificationList expected = new ModificationList(mods);

        assertEquals(expected, modList);
    }

    @Test
    public void testContainsFunction() {
        ModificationList modList = ModificationList.parseListOfMods("#\n"
                + "Infinite skill usage\n"
                + "public int get_ActivateLimit(); // 0xA58E80\n"
                + "00A58F27\t0x1\t0A \tEA\n"
                + "#\n"
                + "No skill cool down\n"
                + "public float get_CoolTime(); // 0xA57688\n"
                + "00A57744\t0x1\t10 \t00 \n"
                + "00A57746\t0x1\t00 \tB4 \n"
                + "00A57749\t0x2\t1A BF \t0A 20 \n"
                + "#\n"
                + "No skill activation time\n"
                + "public float get_EffectTime(); // 0xA58BDC\n"
                + "00A58C94\t0x5\t1F D7 FF EB 10 \t00 0A B4 EE 00 \n"
                + "00A58C9A\t0x1\t00 \t20 \n"
                + "#\n"
                + "No SP required\n"
                + "public float get_SkillPointNormalizedRatio(); // 0x97AC94\n"
                + "0097AD34\t0x1\t08 \t00\n"
                + "0097AD36\t0x1\t89 \tB7\n"
                + "#\n"
                + "Godmode\n"
                + "public void Shot(UnitBase sender, UnitActionLockOnOwnerType lockOnOwnerType, int shotIdx, int commandIdx, float limitAngle, bool deviation); // 0x11630C8\n"
                + "011630C8\t0x4\tF0 4F 2D E9 \t1E FF 2F E1 \n"
                + "public void Shot(UnitBase sender, UnitActionLockOnOwnerType lockOnOwnerType, int shotIdx, int commandIdx, float limitAngle, bool deviation, Vector3 offsetPosition, Quaternion offsetRotation, float effectScale); // 0x115ED90\n"
                + "0115ED90\t0x4\tF0 4F 2D E9 \t1E FF 2F E1 \n"
                + "#\n"
                + "Big damage\n"
                + "public float CalcDamage(BulletBase bullet); // 0x11725F4\n"
                + "011725F4\t0x8\tF0 48 2D E9 10 B0 8D E2 \t7F 04 B0 E3 1E FF 2F E1 ");

        // Contains
        assertTrue(modList.containsFunction("public float get_CoolTime()"));

        // Does not contain
        assertFalse(modList.containsFunction("Rand0m stuFF"));

        // Contains
        assertTrue(modList.containsFunction("public void Shot(UnitBase sender, UnitActionLockOnOwnerType lockOnOwnerType, int shotIdx, int commandIdx, float limitAngle, bool deviation, Vector3 offsetPosition, Quaternion offsetRotation, float effectScale)"));
    }
}