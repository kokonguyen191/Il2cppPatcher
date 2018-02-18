package doge.parser;

import static doge.TestObjects.getTestModList;
import static org.junit.Assert.assertEquals;

import doge.data.Function;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import org.junit.Test;

public class DumpParserTest {

    @Test
    public void testInitialize() throws IOException {
        File f = new File("tempEmptyTestFile");
        f.createNewFile();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
        bw.write("public int get_ActivateLimit(); // 0x1170AAC\n"
                + "\tprivate bool get_isCutoffActive(); // 0x1171878\n"
                + "\tpublic float get_CoolTime(); // 0x1171100\n"
                + "\tpublic void SetActiveDissolve(float time, Vector3 scale); // 0x11663B8");
        bw.flush();
        bw.close();

        DumpParser dp = new DumpParser("tempEmptyTestFile", getTestModList());
        HashMap<String, Function> hm = new HashMap<String, Function>();

        hm.put("public int get_ActivateLimit()",
                Function.parseFunction("public int get_ActivateLimit(); // 0x1170AAC\n"));
        hm.put("public float get_CoolTime()",
                Function.parseFunction("\tpublic float get_CoolTime(); // 0x1171100\n"));

        assertEquals(hm, dp.getMap());

        f.delete();
    }


    @Test
    public void testAddFunction() throws IOException {
        File f = new File("tempEmptyTestFile");
        f.createNewFile();

        DumpParser dp = new DumpParser("tempEmptyTestFile", getTestModList());
        HashMap<String, Function> hm = new HashMap<String, Function>();

        // Add a function that needs to be modded
        dp.addFunction(
                "public void Shot(UnitBase sender, UnitActionLockOnOwnerType lockOnOwnerType, int shotIdx, int commandIdx, float limitAngle, bool deviation); // 0x11630C8");
        hm.put("public void Shot(UnitBase sender, UnitActionLockOnOwnerType lockOnOwnerType, int shotIdx, int commandIdx, float limitAngle, bool deviation)",
                Function.parseFunction(
                        "public void Shot(UnitBase sender, UnitActionLockOnOwnerType lockOnOwnerType, int shotIdx, int commandIdx, float limitAngle, bool deviation); // 0x11630C8"));
        assertEquals(hm, dp.getMap());

        // Add a function that does not need to be modded
        dp.addFunction(
                "public boid Shot(UnitBase sender, UnitActionLockOnOwnerType lockOnOwnerType, int shotIdx, int commandIdx, float limitAngle, bool deviation); // 0x11630C8");
        assertEquals(hm, dp.getMap());

        // Add something that's not even a function
        dp.addFunction(
                "bruhhhhhhhhhhhhhh8903-1273dn59-8125jm19823-5--mny---f81935tyder71809t325nfmc12895tmnders1782395tmnf7890123tmn59f7809tc23nm57890dtrmn738905tes7823956hj7es0");
        assertEquals(hm, dp.getMap());

        // Add another function that needs to be modded
        dp.addFunction(
                "public float get_CoolTime(); // 0xA57688\n");
        hm.put("public float get_CoolTime()",
                Function.parseFunction(
                        "public float get_CoolTime(); // 0xA57688\n"));
        assertEquals(hm, dp.getMap());

        f.delete();
    }

}
