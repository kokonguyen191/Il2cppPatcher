package doge.data.patch;

import java.util.LinkedList;

public class ModificationList {

    public static final String MOD_OPTION_DELIMITER_REGEX = "#[\\s]+";

    private LinkedList<Modification> listOfMods;

    ModificationList(LinkedList<Modification> listOfMods) {
        this.listOfMods = listOfMods;
    }

    public static ModificationList parseListOfMods(String input) {
        if (!input.trim().equals("")) {
            LinkedList<Modification> listToAdd = new LinkedList<Modification>();
            String[] blocks = input.split(MOD_OPTION_DELIMITER_REGEX);
            for (String block : blocks) {
                Modification mod = Modification.parseMod(block);
                if (mod != null) {
                    listToAdd.add(mod);
                }
            }
            return new ModificationList(listToAdd);
        } else {
            return null;
        }
    }

    public LinkedList<Modification> getListOfMods() {
        return listOfMods;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ModificationList // instanceof handles nulls
                && this.listOfMods.equals(((ModificationList) other).listOfMods));
    }
}
