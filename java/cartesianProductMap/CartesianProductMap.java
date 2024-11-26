import java.util.*;

public class CartesianProductMap {

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
        if (value.startsWith("[") && value.endsWith("]")) {
            return Arrays.asList(value.substring(1, value.length() - 1).split(","));
        } else {
            return Collections.singletonList(value); // If not array-like, return single value as a list
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
        input.put("orderID", "order123");
        input.put("jobApplicationID", "72635472653");

        List<Map<String, String>> result = cartesianProduct(input);
        
        for (Map<String, String> map : result) {
            System.out.println(map);
        }
    }
}
