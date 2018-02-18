package doge.parser;

import doge.data.Function;
import doge.data.ModificationList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Parse a dump.cs file
 */
public class DumpParser {

    private String filePath;
    private HashMap<String, Function> map;
    private ModificationList listOfMods;

    /**
     * Constructor
     *
     * @param filePath filePath of dump.cs file
     */
    DumpParser(String filePath, ModificationList listOfMods) {
        this.filePath = filePath;
        this.map = new HashMap<String, Function>();
        this.listOfMods = listOfMods;
    }

    /**
     * Initialize the parser
     */
    public void initialize() {
        try {
            populateMap(new BufferedReader(new InputStreamReader(new FileInputStream(filePath))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get all mods needed to do
     */
    public ModificationList getListOfModsNeeded() {
        return listOfMods;
    }

    /**
     * Read the dump file, add function name - address pairs to the HashMap
     *
     * @param br the BufferedReader from the dump file
     */
    public void populateMap(BufferedReader br) throws IOException {
        try {
            String line;
            while ((line = br.readLine()) != null) {
                addFunction(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a function the HashMap
     *
     * @param line a string that might contain a method
     */
    public void addFunction(String line) {
        Function functionToAdd = Function.parseFunction(line);

        if (functionToAdd != null && listOfMods.containsFunction(functionToAdd.getFunctionName())) {
            map.put(functionToAdd.getFunctionName(), functionToAdd);
        }
    }

    public HashMap<String, Function> getMap() {
        return map;
    }

}
