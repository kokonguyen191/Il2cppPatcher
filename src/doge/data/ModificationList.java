package doge.data;

import java.util.HashSet;
import java.util.LinkedList;

/**
 * Contain all Modification. The user will provide this to the application.
 */
public class ModificationList {

    public static final String MOD_OPTION_DELIMITER_REGEX = "#[\\s]+";

    private LinkedList<Modification> listOfMods;
    private HashSet<String> listOfFunctions;

    /**
     * Constructor
     *
     * @param listOfMods a LinkedList of Modification objects
     */
    ModificationList(LinkedList<Modification> listOfMods) {
        this.listOfMods = listOfMods;
    }

    /**
     * Return a ModificationList object from an input
     *
     * @param input input by user
     * @return the ModificationList object
     */
    public static ModificationList parseListOfMods(String input) {
        if (!input.trim().equals("")) {
            LinkedList<Modification> listToAdd = new LinkedList<Modification>();
            HashSet<String> listOfFunctions = new HashSet<String>();

            String[] blocks = input.split(MOD_OPTION_DELIMITER_REGEX);
            for (String block : blocks) {
                Modification mod = Modification.parseMod(block);
                if (mod != null) {
                    listToAdd.add(mod);
                    LinkedList<Patch> patches = mod.getPatches();
                    for (Patch patch : patches) {
                        listOfFunctions.add(patch.getFunction().getFunctionName());
                    }
                }
            }
            ModificationList resultList = new ModificationList(listToAdd);
            resultList.setListOfFunctions(listOfFunctions);
            return resultList;
        } else {
            return null;
        }
    }

    /**
     * Check if a given function is in the mod list. Useful to check if you need to mod a function or not.
     *
     * @param functionName given function name
     * @return true if given function name needs to be modded
     */
    public boolean containsFunction(String functionName) {
        return listOfFunctions.contains(functionName);
    }

    public LinkedList<Modification> getListOfMods() {
        return listOfMods;
    }

    public HashSet<String> getListOfFunctions() {
        return listOfFunctions;
    }

    public void setListOfFunctions(HashSet<String> listOfFunctions) {
        this.listOfFunctions = listOfFunctions;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ModificationList // instanceof handles nulls
                && this.listOfMods.equals(((ModificationList) other).listOfMods));
    }
}
