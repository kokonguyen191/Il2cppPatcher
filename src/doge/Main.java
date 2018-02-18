package doge;

import doge.data.Modification;
import doge.data.ModificationList;
import doge.parser.DumpParser;
import doge.patcher.FilePatcher;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Main {

    private DumpParser dp;
    private ModificationList listOfMods;

    private static final int IL2CPP_INDEX = 0;
    private static final int DUMP_INDEX = 1;
    private static final int MOD_INDEX = 2;

    public static void main(String... launchArgs) {
        new Main().run(new String[]{"E:/temp/Android_Fuckers/Workbench/AGA/mod/libil2cpp.so",
                "E:/temp/Android_Fuckers/Workbench/dump.cs",
                "E:/temp/Android_Fuckers/AGA/modding_guide_new.txt"});
//        new Main().run(launchArgs);
    }

    public void run(String[] launchArgs) {
        if (launchArgs.length != 3) {
            System.out.println("Wrong number of arguments!");
            System.out.println(
                    "java Main <FULL_PATH_TO_IL2CPP.SO_FILE> <FULL_PATH_TO_DUMP.CS_FILE> <FULL_PATH_TO_MOD_FILE>");
        } else {
            String il2cppPath = launchArgs[IL2CPP_INDEX];
            String dumpPath = launchArgs[DUMP_INDEX];
            String modPath = launchArgs[MOD_INDEX];

            listOfMods = ModificationList.parseFromFile(modPath);
            dp = new DumpParser(dumpPath, listOfMods);

            try {
                for (Modification mod : listOfMods.getListOfMods()) {
                    RandomAccessFile fh = new RandomAccessFile(il2cppPath, "rw");
                    FilePatcher.writeAllPatchesInOneMod(fh, dp.getMap(), mod);
                }
            } catch (IOException e) {
                System.err.println("Something unexpected happened while trying to write to file!");
                e.printStackTrace();
            }
        }
    }
}
