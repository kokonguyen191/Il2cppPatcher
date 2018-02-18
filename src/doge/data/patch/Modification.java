package doge.data.patch;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contain a list of Patches
 */
public class Modification {

    public static final String LF = "\n";
    public static final int MOD_NAME_INDEX = 0;
    public static final int FUNCTION_HEADER_INDEX = 0;
    public static final Pattern FUNCTION_PATCH_BLOCK_REGEX = Pattern
            .compile(".*\\/{2}.*[\\s\\S]*?(?=\\n.*?\\/{2}|$)");

    private String modName;
    private LinkedList<Patch> patches;

    /**
     * Constructor
     *
     * @param modName the name of the modification
     * @param patches the list of Patch objects that the modification contains
     */
    Modification(String modName, LinkedList<Patch> patches) {
        this.modName = modName;
        this.patches = patches;
    }

    /**
     * Return a Modification object from given string
     *
     * @param block a string that contains the modification
     * @return the Modification object
     */
    public static Modification parseMod(String block) {
        String[] lines = block.split(LF);
        String parsedMdName = lines[MOD_NAME_INDEX];

        LinkedList<Patch> patches = new LinkedList<Patch>();
        Matcher functionBlockMatcher = FUNCTION_PATCH_BLOCK_REGEX.matcher(block);
        while (functionBlockMatcher.find()) {
            patches.addAll(Modification.parsePatches(functionBlockMatcher.group(0)));
        }
        if (!patches.isEmpty()) {
            return new Modification(parsedMdName, patches);
        } else {
            return null;
        }
    }

    /**
     * Return a list of Patch objects from given string
     *
     * @param block a string that contains multiple patches
     * @return a LinkedList of Patch objects
     */
    public static LinkedList<Patch> parsePatches(String block) {
        if (block.matches(FUNCTION_PATCH_BLOCK_REGEX.pattern())) {
            String[] lines = block.split(LF);

            Function function = Function.parseFunction(lines[FUNCTION_HEADER_INDEX]);
            LinkedList<Patch> patches = new LinkedList<Patch>();
            for (int i = FUNCTION_HEADER_INDEX + 1; i < lines.length; i++) {
                patches.add(Patch.parsePatch(lines[i], function));
            }
            return patches;
        } else {
            return null;
        }
    }

    public String getModName() {
        return modName;
    }

    public LinkedList<Patch> getPatches() {
        return patches;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Modification // instanceof handles nulls
                && this.modName.equals(((Modification) other).modName)
                && this.patches.equals(((Modification) other).patches));
    }
}
