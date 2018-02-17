package doge;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parse a dump.cs file and return the addresses of the functions needed to mod
 * 
 * @author Koko
 *
 */
public class DumpParser {

	private static final Pattern FUNCTION_NAME_REGEX = Pattern.compile("(?:^.* \\s*(.*))(?=\\()");
	private static final Pattern FUNCTION_ADDRESS_REGEX = Pattern.compile("(?<=\\/\\/ 0x)[\\dA-F]*");
	private static final String FUNCTION_NAME_NOT_FOUND_DEFAULT_VALUE = "";
	private static final int FUNCTION_ADDRESS_NOT_FOUND_DEFAULT_VALUE = -1;
	private static final String DEFAULT_DUMP_FILE_PATH = "dump.cs";

	private String filePath;
	private BufferedReader br;
	private HashMap<Integer, String> map;
	public HashSet<String> functionsNeeded;

	/**
	 * Constructor
	 * 
	 * @param filePath
	 *            filePath of dump.cs file
	 */
	DumpParser(String filePath) {
		this.filePath = filePath;
		setMap(new HashMap<Integer, String>());
		functionsNeeded = new HashSet<String>();
	}

	/**
	 * Lazy constructor
	 */
	DumpParser() {
		this(DEFAULT_DUMP_FILE_PATH);
	}

	/**
	 * Initialize the parser
	 */
	public void initialize() {
		try {
			populateFunctionsNeeded();
			populateMap(new BufferedReader(new InputStreamReader(new FileInputStream(filePath))));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add all function names needed to mod into functionsNeeded Set
	 */
	public void populateFunctionsNeeded() {
		functionsNeeded.add("get_ActivateLimit");
		functionsNeeded.add("get_CoolTime");
		functionsNeeded.add("get_EffectTime");
		functionsNeeded.add("get_SkillPointNormalizedRatio");
		functionsNeeded.add("CalcDamage");
		functionsNeeded.add("Shot");
	}

	/**
	 * Read the dump file, add function name - address pairs to the HashMap
	 * 
	 * @param br
	 *            the BufferedReader from the dump file
	 * @throws IOException
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
	 * Add a function name - address pair to the HashMap
	 * 
	 * @param line
	 *            a string that might contain a method
	 */
	public void addFunction(String line) {
		String functionName = DumpParser.parseFunctionName(line);
		int functionAddress = DumpParser.parseFunctionAddress(line);

		if (functionsNeeded.contains(functionName)) {
			getMap().put(functionAddress, functionName);
		}
	}

	/**
	 * Return a function name from a string
	 * 
	 * @param line
	 *            a string that might contain a method
	 * @return a function name if the string is valid, else a given default
	 *         return value
	 */
	public static String parseFunctionName(String line) {
		Matcher functionNameMatcher = FUNCTION_NAME_REGEX.matcher(line);
		if (functionNameMatcher.find()) {
			return functionNameMatcher.group(1);
		} else {
			return FUNCTION_NAME_NOT_FOUND_DEFAULT_VALUE;
		}
	}

	/**
	 * Return the start address of a function
	 * 
	 * @param line
	 *            a string that might contain a method
	 * @return the start address of a function if the string is valid, else a
	 *         given default return value
	 */
	public static int parseFunctionAddress(String line) {
		Matcher functionAddressMatcher = FUNCTION_ADDRESS_REGEX.matcher(line);
		if (functionAddressMatcher.find()) {
			String output = functionAddressMatcher.group(0);
			if (!output.equals("FFFFFFFFFFFFFFFF")) {
				return Integer.parseInt(output, 16);
			} else {
				return FUNCTION_ADDRESS_NOT_FOUND_DEFAULT_VALUE;
			}
		} else {
			return FUNCTION_ADDRESS_NOT_FOUND_DEFAULT_VALUE;
		}
	}

	
	public HashMap<Integer, String> getMap() {
		return map;
	}

	public void setMap(HashMap<Integer, String> map) {
		this.map = map;
	}
}
