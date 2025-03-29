import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The JSONObject class represents a simple JSON object structure.
 * It allows adding key-value pairs and creating new entries.
 */
public class JSONObject {

    private final List<Map<String, Object>> objectLogs = new ArrayList<>();

    /**
     * Adds a key-value pair to the current JSON object entry.
     * If there are no entries, a new entry is created.
     *
     * @param key the key to be added
     * @param value the value to be added
     */
    public void put(String key, Object value) {
        if (objectLogs.isEmpty()) {
            objectLogs.add(new HashMap<>());
        }
        objectLogs.get(objectLogs.size() - 1).put(key, value);
    }

    /**
     * Creates a new JSON object entry.
     */
    public void newEntry() {
        objectLogs.add(new HashMap<>());
    }

    /**
     * Returns a string representation of the JSON object.
     *
     * @return a string representation of the JSON object
     */
    @Override
    public String toString() {
        StringBuilder json = new StringBuilder("{\n");
        for (Map<String, Object> log : objectLogs) {
            if (!log.isEmpty()) {
                json.append("  ");
                for (Map.Entry<String, Object> entry : log.entrySet()) {
                    json.append("\"").append(entry.getKey()).append("\": ");
                    if (entry.getValue() instanceof String) {
                        json.append("\"").append(entry.getValue()).append("\"");
                    } else {
                        json.append(entry.getValue());
                    }
                    json.append(", ");
                }
                // Remove the last comma and extra space
                json.delete(json.length() - 2, json.length());
                json.append("\n");
            }
        }
        json.append("}");
        return json.toString();
    }
}