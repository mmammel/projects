import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class CartesianProductMap {


    // Regular expression pattern to match array-like strings (e.g., "[a,b,c]" or "a,b,c")
    // the first group (group(1)) will be the values separated by commas whether or not 
    // the string is enclosed in square brackets
    private static Pattern arrayValuePattern = Pattern.compile("\\[?(.*?(?:,[^\\],]+)+)\\]?");

    // Main function to process the input map and return the cartesian product list of maps
    public static List<Map<String, String>> cartesianProduct(Map<String, String> inputMap) {
        // Parse and expand the input map values
        Map<String, List<String>> parsedMap = new HashMap<>();
        for (Map.Entry<String, String> entry : inputMap.entrySet()) {
            parsedMap.put(entry.getKey(), parseArrayString(entry.getValue()));
        }

        // Generate cartesian product
        List<Map<String, String>> result = new ArrayList<>();
        generateCartesianProduct(parsedMap, new HashMap<>(), result);
        
        return result;
    }

    // Helper function to parse array-like strings (e.g., "[a,b,c]") into a List of strings
    private static List<String> parseArrayString(String value) {
        Matcher matcher = null;
        if( value == null) {
            return Collections.singletonList(null);
        } else {
            if ((matcher = arrayValuePattern.matcher(value)).matches()) {
                return Arrays.asList(matcher.group(1).trim().split("\\s*,\\s*"));
            } else {
                return Collections.singletonList(value); // If not array-like, return single value as a list
            }
        }
    }

    // Recursive function to generate the cartesian product
    private static void generateCartesianProduct(Map<String, List<String>> parsedMap, Map<String, String> current, List<Map<String, String>> result) {
        if (current.size() == parsedMap.size()) {
            result.add(new HashMap<>(current)); // Add a copy of the current combination
            return;
        }

        // Recursively add each value from the parsed map into the current combination
        for (String key : parsedMap.keySet()) {
            if (!current.containsKey(key)) {
                for (String value : parsedMap.get(key)) {
                    current.put(key, value);
                    generateCartesianProduct(parsedMap, current, result);
                    current.remove(key);
                }
                break; // Only handle one key at a time
            }
        }
    }

    // Example usage
    public static void main(String[] args) {
        Map<String, String> input = new HashMap<>();
        input.put("key1", "a");
        input.put("key2", "[b,c]");
        input.put("key3", "[d, e ]");
        input.put("key4", "f, g , h"); 

        List<Map<String, String>> result = cartesianProduct(input);
        
        for (Map<String, String> map : result) {
            System.out.println(map);
        }
    }
}
