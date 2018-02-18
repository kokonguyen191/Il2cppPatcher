package doge.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Contain all Modification. The user will provide this to the application.
 */
public class ModificationList {

    public static final String LR = "\n";
    public static final String MOD_OPTION_DELIMITER_REGEX = "#[\\s]+";

    private ArrayList<Modification> listOfMods;
    private HashSet<String> listOfFunctions;

    /**
     * Constructor
     *
     * @param listOfMods a LinkedList of Modification objects
     */
    public ModificationList(ArrayList<Modification> listOfMods) {
        this.listOfMods = listOfMods;
    }

    /**
     * Constructor
     *
     * @param filePath file path of a txt file that contains all the modifications
     */
    public ModificationList(String filePath) {
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(filePath))));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(LR);
            }
            br.close();
            this.listOfMods = ModificationList.parseListOfMods(sb.toString()).getListOfMods();
        } catch (IOException ioe) {
            System.err.println(
                    "Something unexpected happened while trying to read from the mod file");
            ioe.printStackTrace();
        }
    }

    /**
     * Return a ModificationList object from an input
     *
     * @param input input by user
     * @return the ModificationList object
     */
    public static ModificationList parseListOfMods(String input) {
        if (!input.trim().equals("")) {
            ArrayList<Modification> listToAdd = new ArrayList<Modification>();
            HashSet<String> listOfFunctions = new HashSet<String>();

            String[] blocks = input.split(MOD_OPTION_DELIMITER_REGEX);
            for (String block : blocks) {
                Modification mod = Modification.parseMod(block);
                if (mod != null) {
                    listToAdd.add(mod);
                    ArrayList<Patch> patches = mod.getPatches();
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

    /**
     * Return the modification at given index
     *
     * @param index index of Modification object
     * @return the Modification object
     */
    public Modification getModAt(int index) {
        return listOfMods.get(index);
    }

    public ArrayList<Modification> getListOfMods() {
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
