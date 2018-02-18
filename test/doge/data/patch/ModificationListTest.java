package doge.data.patch;

import static org.junit.Assert.assertEquals;

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
}