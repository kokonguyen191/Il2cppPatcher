package doge.data.patch;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class Modification {

    public static final String LF = "\n";
    public static final int MOD_OPTION_NAME_INDEX = 0;
    public static final int FUNCTION_HEADER_INDEX = 0;
    public static final Pattern FUNCTION_PATCH_BLOCK_REGEX = Pattern
            .compile(".*\\/{2}.*[\\s\\S]*?(?=\\n.*?\\/{2}|$)");

    private String modName;
    private Patch[] patches;

    public Modification(String modName, List<Patch> patches) {
        this.modName = modName;
        this.patches = patches.toArray(new Patch[patches.size()]);
    }

    public static Patch[] parsePatches(String block) {
        if (block.matches(FUNCTION_PATCH_BLOCK_REGEX.pattern())) {
            String[] lines = block.split(LF);

            Function function = Function.parseFunction(lines[FUNCTION_HEADER_INDEX]);
            LinkedList<Patch> patches = new LinkedList<Patch>();
            for (int i = FUNCTION_HEADER_INDEX + 1; i < lines.length; i++) {
                patches.add(Patch.parsePatch(lines[i], function));
            }
            return patches.toArray(new Patch[patches.size()]);
        } else {
            return null;
        }
    }

    public String getModName() {
        return modName;
    }

    public Patch[] getPatches() {
        return patches;
    }

}
