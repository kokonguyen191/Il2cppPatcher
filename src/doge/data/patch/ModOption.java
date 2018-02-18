package doge.data.patch;

import java.util.ArrayList;

public class ModOption {

    private String modName;
    private ArrayList<Patch> patches;

    public ModOption(String modName, ArrayList<Patch> patches) {
        setModName(modName);
        setPatches(patches);
    }

    public String getModName() {
        return modName;
    }

    public void setModName(String modName) {
        this.modName = modName;
    }

    public ArrayList<Patch> getPatches() {
        return patches;
    }

    public void setPatches(ArrayList<Patch> patches) {
        this.patches = patches;
    }

}
