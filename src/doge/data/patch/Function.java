package doge.data.patch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Function {

    public static final Pattern FUNCTION_NAME_REGEX = Pattern.compile("\\s*(.*\\(.*\\).*?);");
    public static final Pattern FUNCTION_ADDRESS_REGEX = Pattern.compile("(?<=\\/\\/ 0x)[\\dA-F]*");
    public static final String FUNCTION_NAME_NOT_FOUND_DEFAULT_VALUE = "";
    public static final int FUNCTION_ADDRESS_NOT_FOUND_DEFAULT_VALUE = -1;

    private String functionName;
    private int functionOffset;

    Function(String functionName, int functionOffset) {
        this.functionName = functionName;
        this.functionOffset = functionOffset;
    }

    public static Function parseFunction(String line) {
        String parsedName = Function.parseFunctionName(line);
        int parsedAddress = Function.parseFunctionAddress(line);

        if (!parsedName.equals(FUNCTION_NAME_NOT_FOUND_DEFAULT_VALUE)) {
            return new Function(parsedName, parsedAddress);
        } else {
            return null;
        }
    }

    /**
     * Return a function name from a string
     *
     * @param line a string that might contain a method
     * @return a function name if the string is valid, else a given default
     * return value
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
     * @param line a string that might contain a method
     * @return the start address of a function if the string is valid, else a
     * given default return value
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

    public String getFunctionName() {
        return functionName;
    }

    public int getFunctionOffset() {
        return functionOffset;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Function // instanceof handles nulls
                && this.functionName.equals(((Function) other).functionName)
                && this.functionOffset == ((Function) other).functionOffset);
    }
}
